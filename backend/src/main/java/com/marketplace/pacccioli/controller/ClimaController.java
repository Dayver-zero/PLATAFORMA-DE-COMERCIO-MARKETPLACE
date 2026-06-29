package com.marketplace.pacccioli.controller;

import com.marketplace.pacccioli.dto.ApiResponseDTO;
import com.marketplace.pacccioli.service.ClimaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/clima")
public class ClimaController {
    
    @Autowired
    private ClimaService climaService;
    
    /**
     * GET /api/clima?latitud=X&longitud=Y
     * Obtener clima actual por coordenadas
     */
    @GetMapping
    public ResponseEntity<ApiResponseDTO<ClimaService.ClimaDTO>> obtenerClima(
            @RequestParam Double latitud,
            @RequestParam Double longitud) {
        try {
            ClimaService.ClimaDTO clima = climaService.obtenerClimaPorCoordenadas(latitud, longitud);
            
            if (clima == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(false, "No se pudo obtener el clima",
                                List.of("Coordenadas inválidas o servicio no disponible")));
            }
            
            return ResponseEntity.ok(new ApiResponseDTO<>(
                    true,
                    "Clima obtenido exitosamente",
                    clima
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al obtener clima",
                            List.of(e.getMessage())));
        }
    }
    
    /**
     * GET /api/clima/ciudad?ciudad=X
     * Obtener clima actual por nombre de ciudad
     */
    @GetMapping("/ciudad")
    public ResponseEntity<ApiResponseDTO<ClimaService.ClimaDTO>> obtenerClimaPorCiudad(
            @RequestParam String ciudad) {
        try {
            ClimaService.ClimaDTO clima = climaService.obtenerClimaPorCiudad(ciudad);
            
            if (clima == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(false, "No se pudo obtener el clima para la ciudad",
                                List.of("Ciudad no encontrada o servicio no disponible")));
            }
            
            return ResponseEntity.ok(new ApiResponseDTO<>(
                    true,
                    "Clima obtenido exitosamente",
                    clima
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al obtener clima",
                            List.of(e.getMessage())));
        }
    }
    
    /**
     * GET /api/clima/pronostico?latitud=X&longitud=Y&dias=5
     * Obtener pronóstico del clima
     */
    @GetMapping("/pronostico")
    public ResponseEntity<ApiResponseDTO<List<ClimaService.ClimaDTO>>> obtenerPronostico(
            @RequestParam Double latitud,
            @RequestParam Double longitud,
            @RequestParam(defaultValue = "5") Integer dias) {
        try {
            ClimaService.ClimaDTO[] pronostico = climaService.obtenerPronostico(latitud, longitud, dias);
            
            if (pronostico == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(false, "No se pudo obtener el pronóstico",
                                List.of("Servicio no disponible")));
            }
            
            return ResponseEntity.ok(new ApiResponseDTO<>(
                    true,
                    "Pronóstico obtenido exitosamente",
                    Arrays.asList(pronostico)
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al obtener pronóstico",
                            List.of(e.getMessage())));
        }
    }
}
