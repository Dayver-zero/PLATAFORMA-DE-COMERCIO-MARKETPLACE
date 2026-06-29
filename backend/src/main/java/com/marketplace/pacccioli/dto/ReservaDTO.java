package com.marketplace.pacccioli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaDTO {
    private Long id;
    private Long usuarioId;
    private String nombreUsuario;
    private Long productoId;
    private String nombreProducto;
    private String urlImagen;
    private Long comercioId;
    private String nombreComercio;
    private Integer cantidad;
    private String estado;
    private LocalDateTime fechaReserva;
    private LocalDateTime fechaExpiracion;
    private String notas;
}
