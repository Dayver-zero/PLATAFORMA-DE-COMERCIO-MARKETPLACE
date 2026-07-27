package com.marketplace.pacccioli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoDTO {
    private Long id;
    private Long pedidoId;
    private String metodo;
    private String estado;
    private BigDecimal monto;
    private String codigoReferencia;
    private String comprobanteUrl;
    private String metadata;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaPago;
}
