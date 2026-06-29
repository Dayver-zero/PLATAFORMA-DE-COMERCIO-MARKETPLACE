package com.marketplace.pacccioli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String email;
    private String rol; // CLIENTE, COMERCIANTE, ADMIN
    private Double latitud;
    private Double longitud;
    private Integer radioBusquedaKm;
    private String preferencias; // JSON
    private String historialBusqueda; // JSON
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
