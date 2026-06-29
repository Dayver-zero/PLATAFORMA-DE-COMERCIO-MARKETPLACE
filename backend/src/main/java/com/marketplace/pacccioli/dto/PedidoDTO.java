package com.marketplace.pacccioli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {
    private Long id;
    private Long usuarioId;
    private String nombreUsuario;
    private String estado;
    private BigDecimal total;
    private String metodoPago;
    private String codigoPago;
    private String comprobanteUrl;
    private String referenciaPago;
    private String direccionEnvio;
    private String notas;
    private List<PedidoItemDTO> items;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaPago;
}
