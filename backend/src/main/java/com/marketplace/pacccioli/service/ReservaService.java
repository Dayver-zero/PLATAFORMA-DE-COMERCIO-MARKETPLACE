package com.marketplace.pacccioli.service;

import com.marketplace.pacccioli.dto.ReservaDTO;
import com.marketplace.pacccioli.model.*;
import com.marketplace.pacccioli.model.Reserva.EstadoReserva;
import com.marketplace.pacccioli.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final ComercioRepository comercioRepository;

    @Transactional
    public ReservaDTO crearReserva(Long usuarioId, Long productoId, Integer cantidad, String notas) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

        if (!producto.getActivo()) {
            throw new IllegalStateException("El producto no está disponible");
        }

        if (!Boolean.TRUE.equals(producto.getPermiteReserva())) {
            throw new IllegalStateException("Este producto no permite reserva");
        }

        long reservadas = reservaRepository.countByProductoIdAndEstado(productoId, EstadoReserva.PENDIENTE);
        long confirmadas = reservaRepository.countByProductoIdAndEstado(productoId, EstadoReserva.CONFIRMADA);
        long reservadasTotal = reservadas + confirmadas;

        if (producto.getStock() - reservadasTotal < cantidad) {
            throw new IllegalStateException("Stock insuficiente para reserva. Disponible: " +
                    (producto.getStock() - reservadasTotal));
        }

        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setProducto(producto);
        reserva.setComercio(producto.getComercio());
        reserva.setCantidad(cantidad);
        reserva.setEstado(EstadoReserva.PENDIENTE);
        reserva.setFechaExpiracion(LocalDateTime.now().plusHours(24));
        reserva.setNotas(notas);

        reserva = reservaRepository.save(reserva);
        return convertirADTO(reserva);
    }

    @Transactional
    public ReservaDTO cancelarReserva(Long id, Long usuarioId) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada: " + id));

        if (!reserva.getUsuario().getId().equals(usuarioId) &&
                !reserva.getComercio().getPropietario().getId().equals(usuarioId)) {
            throw new SecurityException("No tienes permiso para cancelar esta reserva");
        }

        if (reserva.getEstado() == EstadoReserva.COMPLETADA) {
            throw new IllegalStateException("No se puede cancelar una reserva completada");
        }

        reserva.setEstado(EstadoReserva.CANCELADA);
        reserva = reservaRepository.save(reserva);
        return convertirADTO(reserva);
    }

    @Transactional
    public ReservaDTO completarReserva(Long id, Long usuarioId) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada: " + id));

        boolean esComerciante = reserva.getComercio().getPropietario().getId().equals(usuarioId);
        if (!esComerciante) {
            throw new SecurityException("Solo el comerciante puede marcar una reserva como completada");
        }

        if (reserva.getEstado() != EstadoReserva.PENDIENTE && reserva.getEstado() != EstadoReserva.CONFIRMADA) {
            throw new IllegalStateException("La reserva no está activa");
        }

        Producto producto = reserva.getProducto();
        producto.setStock(producto.getStock() - reserva.getCantidad());
        productoRepository.save(producto);

        reserva.setEstado(EstadoReserva.COMPLETADA);
        reserva = reservaRepository.save(reserva);
        return convertirADTO(reserva);
    }

    public List<ReservaDTO> obtenerReservasUsuario(Long usuarioId) {
        return reservaRepository.findByUsuarioIdOrderByFechaReservaDesc(usuarioId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<ReservaDTO> obtenerReservasComercio(Long comercioId) {
        return reservaRepository.findByComercioIdOrderByFechaReservaDesc(comercioId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public ReservaDTO obtenerReserva(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada: " + id));
        return convertirADTO(reserva);
    }

    private ReservaDTO convertirADTO(Reserva reserva) {
        ReservaDTO dto = new ReservaDTO();
        dto.setId(reserva.getId());
        dto.setUsuarioId(reserva.getUsuario().getId());
        dto.setNombreUsuario(reserva.getUsuario().getNombre() != null ?
                reserva.getUsuario().getNombre() : reserva.getUsuario().getEmail());
        dto.setProductoId(reserva.getProducto().getId());
        dto.setNombreProducto(reserva.getProducto().getNombre());
        dto.setUrlImagen(reserva.getProducto().getUrlImagen());
        dto.setComercioId(reserva.getComercio().getId());
        dto.setNombreComercio(reserva.getComercio().getNombre());
        dto.setCantidad(reserva.getCantidad());
        dto.setEstado(reserva.getEstado().name());
        dto.setFechaReserva(reserva.getFechaReserva());
        dto.setFechaExpiracion(reserva.getFechaExpiracion());
        dto.setNotas(reserva.getNotas());
        return dto;
    }
}
