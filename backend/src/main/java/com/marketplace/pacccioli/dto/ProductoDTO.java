package com.marketplace.pacccioli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer stock;
    private String urlImagen;
    private List<String> etiquetasInteligentes;
    private String categoria;
    private String estado;
    private Double calificacionPromedio;
    private Integer conteoVisualizaciones;
    private Integer conteoCompras;
    private Long comercioId;
    private String nombreComercio;
    private Double latitudComercio;
    private Double longitudComercio;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
