package com.marketplace.pacccioli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDTO<T> {
    private boolean exito;
    private String mensaje;
    private T datos;
    private List<String> errores;
    private LocalDateTime timestamp;
    
    // Constructor para respuesta exitosa
    public ApiResponseDTO(boolean exito, String mensaje, T datos) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.datos = datos;
        this.timestamp = LocalDateTime.now();
    }
    
    // Constructor para respuesta con error
    public ApiResponseDTO(boolean exito, String mensaje, List<String> errores) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.errores = errores;
        this.timestamp = LocalDateTime.now();
    }
}
