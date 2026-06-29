package com.marketplace.pacccioli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarritoItemDTO {
    private Long id;
    private Long productoId;
    private String nombreProducto;
    private BigDecimal precioUnitario;
    private String urlImagen;
    private Integer cantidad;
    private BigDecimal subtotal;
    private Long comercioId;
    private String nombreComercio;
    private Integer stockDisponible;
    private LocalDateTime fechaAgregado;
}
