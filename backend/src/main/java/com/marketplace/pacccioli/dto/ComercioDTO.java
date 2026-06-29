package com.marketplace.pacccioli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComercioDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String direccion;
    private String telefono;
    private String horarioAtencion;
    private String categoria;
    private Double latitud;
    private Double longitud;
    private Double calificacionPromedio;
    private Integer conteoResenas;
    private Long propietarioId;
    private String nombrePropietario;
    private String urlImagen;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
