package com.marketplace.pacccioli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InteraccionDTO {
    private Long id;
    private Long usuarioId;
    private String nombreUsuario;
    private Long productoId;
    private String nombreProducto;
    private Long comercioId;
    private String tipo; // VISUALIZACION, CLICK, COMPRA, FAVORITO
    private String fuente; // BUSQUEDA, RECOMENDACION, DIRECTO
    private Double latitudUsuario;
    private Double longitudUsuario;
    private String climaContexto; // JSON con datos de clima
    private Double precioEnInteraccion;
    private LocalDateTime fecha;
}
