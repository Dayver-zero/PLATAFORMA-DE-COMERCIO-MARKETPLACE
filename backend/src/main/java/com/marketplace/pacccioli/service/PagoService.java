package com.marketplace.pacccioli.service;

import com.marketplace.pacccioli.dto.PagoDTO;
import com.marketplace.pacccioli.model.Pago;
import com.marketplace.pacccioli.model.Pago.EstadoPago;
import com.marketplace.pacccioli.model.Pedido;
import com.marketplace.pacccioli.model.Pedido.EstadoPedido;
import com.marketplace.pacccioli.model.Pedido.MetodoPago;
import com.marketplace.pacccioli.repository.PagoRepository;
import com.marketplace.pacccioli.repository.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PagoService {

    private final PagoRepository pagoRepository;
    private final PedidoRepository pedidoRepository;

    @Transactional
    public PagoDTO registrarPago(Long pedidoId, String metodoStr, String codigoReferencia,
                                  String comprobanteUrl, String metadataJson) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Pedido no encontrado: " + pedidoId));

        Pedido.MetodoPago metodo = Pedido.MetodoPago.valueOf(metodoStr.toUpperCase());

        Pago pago = new Pago();
        pago.setPedido(pedido);
        pago.setMetodo(metodo);
        pago.setEstado(Pago.EstadoPago.PAGADO);
        pago.setMonto(pedido.getTotal());
        pago.setCodigoReferencia(pedido.getCodigoPago());
        pago.setComprobanteUrl(pedido.getComprobanteUrl());
        pago.setMetadata(metadataJson);
        pago.setFechaCreacion(LocalDateTime.now());
        pago.setFechaPago(LocalDateTime.now());
        pago = pagoRepository.save(pago);

        pedido.setEstado(Pedido.EstadoPedido.PAGADO);
        pedido.setFechaPago(LocalDateTime.now());
        pedidoRepository.save(pedido);

        return convertirADTO(pago);
    }

    public List<PagoDTO> obtenerPagosPorPedido(Long pedidoId) {
        return pagoRepository.findByPedidoIdOrderByFechaCreacionDesc(pedidoId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private PagoDTO convertirADTO(Pago pago) {
        PagoDTO dto = new PagoDTO();
        dto.setId(pago.getId());
        dto.setPedidoId(pago.getPedido().getId());
        dto.setMetodo(pago.getMetodo().name());
        dto.setEstado(pago.getEstado().name());
        dto.setMonto(pago.getMonto());
        dto.setCodigoReferencia(pago.getCodigoReferencia());
        dto.setComprobanteUrl(pago.getComprobanteUrl());
        dto.setMetadata(pago.getMetadata());
        dto.setFechaCreacion(pago.getFechaCreacion());
        dto.setFechaPago(pago.getFechaPago());
        return dto;
    }
}
