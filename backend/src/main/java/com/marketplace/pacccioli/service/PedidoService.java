package com.marketplace.pacccioli.service;

import com.marketplace.pacccioli.dto.CarritoDTO;
import com.marketplace.pacccioli.dto.CarritoItemDTO;
import com.marketplace.pacccioli.dto.PedidoDTO;
import com.marketplace.pacccioli.dto.PedidoItemDTO;
import com.marketplace.pacccioli.model.*;
import com.marketplace.pacccioli.model.Pedido.EstadoPedido;
import com.marketplace.pacccioli.model.Pedido.MetodoPago;
import com.marketplace.pacccioli.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PedidoItemRepository pedidoItemRepository;
    private final CarritoService carritoService;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    private static final String CARACTERES_CODIGO = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final SecureRandom random = new SecureRandom();

    @Transactional
    public PedidoDTO crearPedidoDesdeCarrito(Long usuarioId, String metodoPagoStr) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        CarritoDTO carrito = carritoService.obtenerCarrito(usuarioId);

        if (carrito.getItems() == null || carrito.getItems().isEmpty()) {
            throw new IllegalStateException("El carrito está vacío");
        }

        MetodoPago metodoPago = MetodoPago.valueOf(metodoPagoStr.toUpperCase());

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setMetodoPago(metodoPago);
        pedido.setTotal(carrito.getSubtotal());

        if (metodoPago == MetodoPago.YAPE) {
            pedido.setCodigoPago(generarCodigoPago());
        }

        pedido = pedidoRepository.save(pedido);

        for (CarritoItemDTO itemDTO : carrito.getItems()) {
            Producto producto = productoRepository.findById(itemDTO.getProductoId())
                    .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

            if (producto.getStock() < itemDTO.getCantidad()) {
                throw new IllegalStateException("Stock insuficiente para: " + producto.getNombre());
            }

            producto.setStock(producto.getStock() - itemDTO.getCantidad());
            producto.setConteoCompras(producto.getConteoCompras() != null ?
                    producto.getConteoCompras() + itemDTO.getCantidad() : itemDTO.getCantidad());
            productoRepository.save(producto);

            PedidoItem item = new PedidoItem();
            item.setPedido(pedido);
            item.setProducto(producto);
            item.setNombreProducto(producto.getNombre());
            item.setPrecioUnitario(producto.getPrecio());
            item.setCantidad(itemDTO.getCantidad());
            item.setSubtotal(itemDTO.getSubtotal());
            pedidoItemRepository.save(item);
        }

        carritoService.limpiarCarrito(usuarioId);

        pedido = pedidoRepository.findById(pedido.getId()).orElse(pedido);
        return convertirADTO(pedido);
    }

    public List<PedidoDTO> obtenerPedidosUsuario(Long usuarioId) {
        return pedidoRepository.findByUsuarioIdOrderByFechaCreacionDesc(usuarioId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public PedidoDTO obtenerPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado: " + id));
        return convertirADTO(pedido);
    }

    @Transactional
    public PedidoDTO actualizarEstado(Long id, String nuevoEstadoStr, Long usuarioId) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado: " + id));

        boolean esComerciante = pedido.getItems().stream()
                .anyMatch(i -> i.getProducto().getComercio().getPropietario().getId().equals(usuarioId));
        boolean esAdmin = pedido.getUsuario().getId().equals(usuarioId);

        if (!esComerciante && !esAdmin) {
            throw new SecurityException("No tienes permiso para cambiar el estado de este pedido");
        }

        EstadoPedido nuevoEstado = EstadoPedido.valueOf(nuevoEstadoStr.toUpperCase());
        pedido.setEstado(nuevoEstado);

        if (nuevoEstado == EstadoPedido.CONFIRMADO && pedido.getMetodoPago() == MetodoPago.YAPE) {
            pedido.setFechaPago(LocalDateTime.now());
        }

        pedido = pedidoRepository.save(pedido);
        return convertirADTO(pedido);
    }

    @Transactional
    public String generarCodigoPagoYape(Long pedidoId, Long usuarioId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado: " + pedidoId));

        if (!pedido.getUsuario().getId().equals(usuarioId)) {
            throw new SecurityException("No tienes permiso para generar código de este pedido");
        }

        if (pedido.getMetodoPago() != MetodoPago.YAPE) {
            throw new IllegalStateException("El método de pago no es YAPE");
        }

        String codigo = generarCodigoPago();
        pedido.setCodigoPago(codigo);
        pedidoRepository.save(pedido);
        return codigo;
    }

    @Transactional
    public PedidoDTO confirmarPagoYape(Long pedidoId, Long usuarioId, String referenciaPago, String comprobanteUrl) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado: " + pedidoId));

        if (!pedido.getUsuario().getId().equals(usuarioId)) {
            throw new SecurityException("No tienes permiso para confirmar pago de este pedido");
        }

        if (pedido.getMetodoPago() != MetodoPago.YAPE) {
            throw new IllegalStateException("El método de pago no es YAPE");
        }

        pedido.setReferenciaPago(referenciaPago);
        pedido.setComprobanteUrl(comprobanteUrl);
        pedido.setFechaPago(LocalDateTime.now());
        pedido = pedidoRepository.save(pedido);
        return convertirADTO(pedido);
    }

    public List<PedidoDTO> obtenerPedidosComerciante(Long comercianteId) {
        return pedidoRepository.findByComercianteId(comercianteId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private String generarCodigoPago() {
        StringBuilder codigo = new StringBuilder("YAPE-");
        for (int i = 0; i < 8; i++) {
            codigo.append(CARACTERES_CODIGO.charAt(random.nextInt(CARACTERES_CODIGO.length())));
        }
        return codigo.toString();
    }

    private PedidoDTO convertirADTO(Pedido pedido) {
        PedidoDTO dto = new PedidoDTO();
        dto.setId(pedido.getId());
        dto.setUsuarioId(pedido.getUsuario().getId());
        dto.setNombreUsuario(pedido.getUsuario().getNombre() != null ?
                pedido.getUsuario().getNombre() : pedido.getUsuario().getEmail());
        dto.setEstado(pedido.getEstado().name());
        dto.setTotal(pedido.getTotal());
        dto.setMetodoPago(pedido.getMetodoPago().name());
        dto.setCodigoPago(pedido.getCodigoPago());
        dto.setComprobanteUrl(pedido.getComprobanteUrl());
        dto.setReferenciaPago(pedido.getReferenciaPago());
        dto.setDireccionEnvio(pedido.getDireccionEnvio());
        dto.setNotas(pedido.getNotas());
        dto.setFechaCreacion(pedido.getFechaCreacion());
        dto.setFechaPago(pedido.getFechaPago());

        List<PedidoItemDTO> itemsDTO = pedido.getItems().stream()
                .map(this::convertirItemADTO)
                .collect(Collectors.toList());
        dto.setItems(itemsDTO);
        return dto;
    }

    private PedidoItemDTO convertirItemADTO(PedidoItem item) {
        PedidoItemDTO dto = new PedidoItemDTO();
        dto.setId(item.getId());
        dto.setProductoId(item.getProducto().getId());
        dto.setNombreProducto(item.getNombreProducto());
        dto.setPrecioUnitario(item.getPrecioUnitario());
        dto.setCantidad(item.getCantidad());
        dto.setSubtotal(item.getSubtotal());
        dto.setUrlImagen(item.getProducto().getUrlImagen());
        return dto;
    }
}
