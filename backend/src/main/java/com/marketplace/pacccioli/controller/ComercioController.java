package com.marketplace.pacccioli.controller;

import com.marketplace.pacccioli.dto.ApiResponseDTO;
import com.marketplace.pacccioli.dto.ComercioDTO;
import com.marketplace.pacccioli.model.Comercio;
import com.marketplace.pacccioli.repository.ComercioRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j

@RestController
@RequestMapping("/api/comercios")
public class ComercioController {
    
    @Autowired
    private ComercioRepository comercioRepository;
    
    /**
     * GET /api/comercios - Obtener todos los comercios activos
     */
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<ComercioDTO>>> obtenerTodos() {
        try {
            List<ComercioDTO> comercios = comercioRepository.findByActivoTrue()
                    .stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
            
            ApiResponseDTO<List<ComercioDTO>> respuesta = new ApiResponseDTO<>(
                    true,
                    "Comercios obtenidos exitosamente",
                    comercios
            );
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al obtener comercios", 
                            List.of(e.getMessage())));
        }
    }
    
    /**
     * GET /api/comercios/{id} - Obtener un comercio por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ComercioDTO>> obtenerPorId(@PathVariable Long id) {
        try {
            var comercio = comercioRepository.findById(id);
            if (comercio.isPresent()) {
                ComercioDTO dto = convertirADTO(comercio.get());
                ApiResponseDTO<ComercioDTO> respuesta = new ApiResponseDTO<>(
                        true,
                        "Comercio obtenido exitosamente",
                        dto
                );
                return ResponseEntity.ok(respuesta);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(false, "Comercio no encontrado", 
                                List.of("El comercio con ID " + id + " no existe")));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al obtener comercio", 
                            List.of(e.getMessage())));
        }
    }
    
    /**
     * GET /api/comercios/buscar?nombre=X - Buscar comercios por nombre
     */
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponseDTO<List<ComercioDTO>>> buscarPorNombre(
            @RequestParam String nombre) {
        try {
            List<ComercioDTO> comercios = comercioRepository.findByNombreContainingIgnoreCase(nombre)
                    .stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
            
            ApiResponseDTO<List<ComercioDTO>> respuesta = new ApiResponseDTO<>(
                    true,
                    "Búsqueda realizada exitosamente",
                    comercios
            );
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error en búsqueda", 
                            List.of(e.getMessage())));
        }
    }
    
    /**
     * GET /api/comercios/categoria/{categoria} - Obtener comercios por categoría
     */
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<ApiResponseDTO<List<ComercioDTO>>> obtenerPorCategoria(
            @PathVariable String categoria) {
        try {
            List<ComercioDTO> comercios = comercioRepository.findByCategoriaAndActivoTrue(Comercio.Categoria.valueOf(categoria.toUpperCase()))
                    .stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
            
            ApiResponseDTO<List<ComercioDTO>> respuesta = new ApiResponseDTO<>(
                    true,
                    "Comercios obtenidos por categoría exitosamente",
                    comercios
            );
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al obtener comercios", 
                            List.of(e.getMessage())));
        }
    }
    
    /**
     * GET /api/comercios/cercanos?latitud=X&longitud=Y&radio=Z
     * Obtener comercios cercanos a una ubicación
     */
    @GetMapping("/cercanos")
    public ResponseEntity<ApiResponseDTO<List<ComercioDTO>>> buscarCercanos(
            @RequestParam Double latitud,
            @RequestParam Double longitud,
            @RequestParam(defaultValue = "5") Double radioKm) {
        try {
            List<ComercioDTO> comercios = comercioRepository.buscarComerciosCercanos(latitud, longitud, radioKm)
                    .stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
            
            ApiResponseDTO<List<ComercioDTO>> respuesta = new ApiResponseDTO<>(
                    true,
                    "Comercios cercanos obtenidos exitosamente",
                    comercios
            );
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al buscar comercios cercanos", 
                            List.of(e.getMessage())));
        }
    }
    
    /**
     * GET /api/comercios/mis-comercios - Obtener comercios del usuario autenticado
     */
    @GetMapping("/mis-comercios")
    public ResponseEntity<ApiResponseDTO<List<ComercioDTO>>> obtenerMisComercios(HttpServletRequest request) {
        try {
            Long usuarioId = (Long) request.getAttribute("usuarioId");
            if (usuarioId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponseDTO<>(false, "Autenticación requerida", null));
            }
            
            List<ComercioDTO> comercios = comercioRepository.findByPropietarioId(usuarioId)
                    .stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
            
            ApiResponseDTO<List<ComercioDTO>> respuesta = new ApiResponseDTO<>(
                    true,
                    "Comercios obtenidos exitosamente",
                    comercios
            );
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            log.error("Error al obtener comercios del usuario", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al obtener comercios",
                            List.of(e.getMessage())));
        }
    }
    
    /**
     * GET /api/comercios/mejores - Obtener comercios mejor calificados
     */
    @GetMapping("/mejores")
    public ResponseEntity<ApiResponseDTO<List<ComercioDTO>>> obtenerMejoresCalificados() {
        try {
            List<ComercioDTO> comercios = comercioRepository.findByActivoTrueOrderByCalificacionDesc()
                    .stream()
                    .limit(10)
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
            
            ApiResponseDTO<List<ComercioDTO>> respuesta = new ApiResponseDTO<>(
                    true,
                    "Comercios mejor calificados obtenidos exitosamente",
                    comercios
            );
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al obtener comercios calificados", 
                            List.of(e.getMessage())));
        }
    }
    
    /**
     * GET /api/comercios/buscar-avanzado?nombre=X&latitud=Y&longitud=Z&radio=R
     * Búsqueda avanzada de comercios
     */
    @GetMapping("/buscar-avanzado")
    public ResponseEntity<ApiResponseDTO<List<ComercioDTO>>> buscarAvanzado(
            @RequestParam String nombre,
            @RequestParam Double latitud,
            @RequestParam Double longitud,
            @RequestParam(defaultValue = "5") Double radioKm) {
        try {
            List<ComercioDTO> comercios = comercioRepository.buscarComerciosAvanzado(nombre, latitud, longitud, radioKm)
                    .stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
            
            ApiResponseDTO<List<ComercioDTO>> respuesta = new ApiResponseDTO<>(
                    true,
                    "Búsqueda avanzada realizada exitosamente",
                    comercios
            );
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error en búsqueda avanzada", 
                            List.of(e.getMessage())));
        }
    }
    
    // Método auxiliar para convertir Comercio a ComercioDTO
    private ComercioDTO convertirADTO(Comercio comercio) {
        ComercioDTO dto = new ComercioDTO();
        dto.setId(comercio.getId());
        dto.setNombre(comercio.getNombre());
        dto.setDescripcion(comercio.getDescripcion());
        dto.setDireccion(comercio.getDireccion());
        dto.setTelefono(comercio.getTelefono());
        dto.setHorarioAtencion(comercio.getHorarioAtencion());
        dto.setCategoria(comercio.getCategoria() != null ? comercio.getCategoria().name() : null);
        dto.setLatitud(comercio.getLatitud());
        dto.setLongitud(comercio.getLongitud());
        dto.setCalificacionPromedio(comercio.getCalificacion());
        dto.setConteoResenas(comercio.getNumeroReseñas());
        dto.setActivo(comercio.getActivo());
        dto.setFechaCreacion(comercio.getFechaCreacion());
        dto.setFechaActualizacion(comercio.getFechaActualizacion());
        
        if (comercio.getPropietario() != null) {
            dto.setPropietarioId(comercio.getPropietario().getId());
            dto.setNombrePropietario(comercio.getPropietario().getNombre());
        }
        
        return dto;
    }
}
