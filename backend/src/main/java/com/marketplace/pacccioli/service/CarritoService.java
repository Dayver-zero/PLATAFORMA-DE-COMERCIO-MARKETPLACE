package com.marketplace.pacccioli.service;

import com.marketplace.pacccioli.dto.CarritoDTO;
import com.marketplace.pacccioli.dto.CarritoItemDTO;
import com.marketplace.pacccioli.model.Carrito;
import com.marketplace.pacccioli.model.CarritoItem;
import com.marketplace.pacccioli.model.Producto;
import com.marketplace.pacccioli.model.Usuario;
import com.marketplace.pacccioli.repository.CarritoItemRepository;
import com.marketplace.pacccioli.repository.CarritoRepository;
import com.marketplace.pacccioli.repository.ProductoRepository;
import com.marketplace.pacccioli.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final CarritoItemRepository carritoItemRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    @Transactional
    public Carrito obtenerOcrearCarrito(Long usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId)
                .orElseGet(() -> {
                    Usuario usuario = usuarioRepository.findById(usuarioId)
                            .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + usuarioId));
                    Carrito carrito = new Carrito();
                    carrito.setUsuario(usuario);
                    return carritoRepository.save(carrito);
                });
    }

    @Transactional
    public CarritoItemDTO agregarItem(Long usuarioId, Long productoId, Integer cantidad) {
        Carrito carrito = obtenerOcrearCarrito(usuarioId);
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado: " + productoId));

        if (!producto.getActivo() || producto.getStock() < cantidad) {
            throw new IllegalStateException("Stock insuficiente para: " + producto.getNombre());
        }

        Optional<CarritoItem> existente = carritoItemRepository.findByCarritoIdAndProductoId(carrito.getId(), productoId);
        CarritoItem item;
        if (existente.isPresent()) {
            item = existente.get();
            item.setCantidad(item.getCantidad() + cantidad);
        } else {
            item = new CarritoItem();
            item.setCarrito(carrito);
            item.setProducto(producto);
            item.setCantidad(cantidad);
        }
        item = carritoItemRepository.save(item);
        return convertirItemADTO(item);
    }

    @Transactional
    public CarritoItemDTO actualizarCantidad(Long usuarioId, Long itemId, Integer cantidad) {
        CarritoItem item = carritoItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item no encontrado: " + itemId));

        if (!item.getCarrito().getUsuario().getId().equals(usuarioId)) {
            throw new SecurityException("No tienes permiso para modificar este item");
        }

        if (cantidad <= 0) {
            carritoItemRepository.delete(item);
            return null;
        }

        if (item.getProducto().getStock() < cantidad) {
            throw new IllegalStateException("Stock insuficiente");
        }

        item.setCantidad(cantidad);
        item = carritoItemRepository.save(item);
        return convertirItemADTO(item);
    }

    @Transactional
    public void eliminarItem(Long usuarioId, Long itemId) {
        CarritoItem item = carritoItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item no encontrado: " + itemId));

        if (!item.getCarrito().getUsuario().getId().equals(usuarioId)) {
            throw new SecurityException("No tienes permiso para modificar este item");
        }

        carritoItemRepository.delete(item);
    }

    @Transactional
    public void limpiarCarrito(Long usuarioId) {
        Carrito carrito = obtenerOcrearCarrito(usuarioId);
        carritoItemRepository.deleteByCarritoId(carrito.getId());
    }

    public CarritoDTO obtenerCarrito(Long usuarioId) {
        Carrito carrito = obtenerOcrearCarrito(usuarioId);
        return convertirADTO(carrito);
    }

    public int contarItems(Long usuarioId) {
        Carrito carrito = obtenerOcrearCarrito(usuarioId);
        return carrito.getItems().stream().mapToInt(CarritoItem::getCantidad).sum();
    }

    private CarritoDTO convertirADTO(Carrito carrito) {
        CarritoDTO dto = new CarritoDTO();
        dto.setId(carrito.getId());
        dto.setUsuarioId(carrito.getUsuario().getId());
        dto.setFechaCreacion(carrito.getFechaCreacion());
        dto.setFechaActualizacion(carrito.getFechaActualizacion());

        List<CarritoItemDTO> itemsDTO = carrito.getItems().stream()
                .map(this::convertirItemADTO)
                .collect(Collectors.toList());
        dto.setItems(itemsDTO);
        dto.setTotalItems(itemsDTO.stream().mapToInt(CarritoItemDTO::getCantidad).sum());
        dto.setSubtotal(itemsDTO.stream()
                .map(i -> i.getSubtotal() != null ? i.getSubtotal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        return dto;
    }

    private CarritoItemDTO convertirItemADTO(CarritoItem item) {
        CarritoItemDTO dto = new CarritoItemDTO();
        dto.setId(item.getId());
        dto.setProductoId(item.getProducto().getId());
        dto.setNombreProducto(item.getProducto().getNombre());
        dto.setPrecioUnitario(item.getProducto().getPrecio());
        dto.setUrlImagen(item.getProducto().getUrlImagen());
        dto.setCantidad(item.getCantidad());
        dto.setSubtotal(item.getSubtotal());
        dto.setComercioId(item.getProducto().getComercio().getId());
        dto.setNombreComercio(item.getProducto().getComercio().getNombre());
        dto.setStockDisponible(item.getProducto().getStock());
        dto.setFechaAgregado(item.getFechaAgregado());
        return dto;
    }
}
