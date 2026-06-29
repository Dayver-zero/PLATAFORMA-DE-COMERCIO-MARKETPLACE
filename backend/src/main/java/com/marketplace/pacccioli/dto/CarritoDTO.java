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
public class CarritoDTO {
    private Long id;
    private Long usuarioId;
    private List<CarritoItemDTO> items;
    private Integer totalItems;
    private BigDecimal subtotal;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
