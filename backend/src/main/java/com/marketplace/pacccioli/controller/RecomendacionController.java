package com.marketplace.pacccioli.controller;

import com.marketplace.pacccioli.dto.ApiResponseDTO;
import com.marketplace.pacccioli.dto.ProductoDTO;
import com.marketplace.pacccioli.model.Producto;
import com.marketplace.pacccioli.repository.ProductoRepository;
import com.marketplace.pacccioli.repository.UsuarioRepository;
import com.marketplace.pacccioli.service.MotorRecomendacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recomendaciones")
public class RecomendacionController {
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private MotorRecomendacionService motorRecomendacion;
    
    /**
     * GET /api/recomendaciones?usuarioId=X&latitud=Y&longitud=Z&clima=W
     * Obtener recomendaciones personalizadas (híbridas) para un usuario
     */
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<ProductoDTO>>> obtenerRecomendaciones(
            @RequestParam Long usuarioId,
            @RequestParam Double latitud,
            @RequestParam Double longitud,
            @RequestParam(required = false) String clima) {
        try {
            var usuario = usuarioRepository.findById(usuarioId);
            if (usuario.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(false, "Usuario no encontrado", 
                                List.of("El usuario con ID " + usuarioId + " no existe")));
            }
            
            // Usar MotorRecomendacionService para obtener recomendaciones híbridas
            List<MotorRecomendacionService.RecomendacionDTO> recomendaciones = 
                    motorRecomendacion.generarRecomendaciones(usuarioId, latitud, longitud);
            
            // Convertir RecomendacionDTO a ProductoDTO para la respuesta
            List<ProductoDTO> resultado = recomendaciones.stream()
                    .map(this::convertirDesdeRecomendacion)
                    .collect(Collectors.toList());
            
            ApiResponseDTO<List<ProductoDTO>> respuesta = new ApiResponseDTO<>(
                    true,
                    "Recomendaciones obtenidas exitosamente (híbridas)",
                    resultado
            );
            return ResponseEntity.ok(respuesta);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al obtener recomendaciones", 
                            List.of(e.getMessage())));
        }
    }
    
    /**
     * GET /api/recomendaciones/clima?latitud=X&longitud=Y
     * Obtener recomendaciones filtradas por clima actual
     */
    @GetMapping("/clima")
    public ResponseEntity<ApiResponseDTO<List<ProductoDTO>>> recomendacionesPorClima(
            @RequestParam Double latitud,
            @RequestParam Double longitud) {
        try {
            // Usar MotorRecomendacionService para obtener recomendaciones por clima
            List<ProductoDTO> recomendaciones = motorRecomendacion.recomendarPorClima(latitud, longitud);
            
            ApiResponseDTO<List<ProductoDTO>> respuesta = new ApiResponseDTO<>(
                    true,
                    "Recomendaciones por clima obtenidas exitosamente",
                    recomendaciones
            );
            return ResponseEntity.ok(respuesta);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al obtener recomendaciones", 
                            List.of(e.getMessage())));
        }
    }
    
    /**
     * GET /api/recomendaciones/ubicacion?latitud=X&longitud=Y&radio=Z
     * Obtener recomendaciones por ubicación geográfica
     */
    @GetMapping("/ubicacion")
    public ResponseEntity<ApiResponseDTO<List<ProductoDTO>>> recomendacionesPorUbicacion(
            @RequestParam Double latitud,
            @RequestParam Double longitud,
            @RequestParam(defaultValue = "5") Integer radioKm) {
        try {
            // Usar MotorRecomendacionService para obtener recomendaciones por ubicación
            List<ProductoDTO> recomendaciones = motorRecomendacion.recomendarPorUbicacion(latitud, longitud, radioKm.doubleValue());
            
            ApiResponseDTO<List<ProductoDTO>> respuesta = new ApiResponseDTO<>(
                    true,
                    "Recomendaciones por ubicación obtenidas exitosamente",
                    recomendaciones
            );
            return ResponseEntity.ok(respuesta);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al obtener recomendaciones", 
                            List.of(e.getMessage())));
        }
    }
    
    /**
     * GET /api/recomendaciones/historial?usuarioId=X
     * Obtener recomendaciones basadas en historial del usuario
     */
    @GetMapping("/historial")
    public ResponseEntity<ApiResponseDTO<List<ProductoDTO>>> recomendacionesPorHistorial(
            @RequestParam Long usuarioId) {
        try {
            var usuario = usuarioRepository.findById(usuarioId);
            if (usuario.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(false, "Usuario no encontrado", 
                                List.of("El usuario con ID " + usuarioId + " no existe")));
            }
            
            // Usar MotorRecomendacionService para obtener recomendaciones por historial
            List<ProductoDTO> recomendaciones = motorRecomendacion.recomendarPorHistorial(usuarioId);
            
            ApiResponseDTO<List<ProductoDTO>> respuesta = new ApiResponseDTO<>(
                    true,
                    "Recomendaciones por historial obtenidas exitosamente",
                    recomendaciones
            );
            return ResponseEntity.ok(respuesta);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al obtener recomendaciones", 
                            List.of(e.getMessage())));
        }
    }
    
    // Método auxiliar para convertir Producto a ProductoDTO
    private ProductoDTO convertirADTO(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio() != null ? producto.getPrecio().doubleValue() : null);
        dto.setStock(producto.getStock());
        dto.setUrlImagen(producto.getUrlImagen());
        dto.setCalificacionPromedio(producto.getCalificacionPromedio());
        dto.setConteoVisualizaciones(producto.getConteoVisualizaciones());
        dto.setConteoCompras(producto.getConteoCompras());
        dto.setActivo(producto.getActivo());
        dto.setFechaCreacion(producto.getFechaCreacion());
        dto.setFechaActualizacion(producto.getFechaActualizacion());
        
        if (producto.getComercio() != null) {
            dto.setComercioId(producto.getComercio().getId());
            dto.setNombreComercio(producto.getComercio().getNombre());
            dto.setLatitudComercio(producto.getComercio().getLatitud());
            dto.setLongitudComercio(producto.getComercio().getLongitud());
        }
        
        return dto;
    }
    
    // Método auxiliar para convertir RecomendacionDTO a ProductoDTO
    private ProductoDTO convertirDesdeRecomendacion(MotorRecomendacionService.RecomendacionDTO recomendacion) {
        // Obtener el producto completo desde la BD
        var productoOpt = productoRepository.findById(recomendacion.getProductoId());
        if (productoOpt.isPresent()) {
            ProductoDTO dto = convertirADTO(productoOpt.get());
            // Opcionalmente, agregar información de puntajes de recomendación en otros campos si es necesario
            return dto;
        }
        return null;
    }
}
