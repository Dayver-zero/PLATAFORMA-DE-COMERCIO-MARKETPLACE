package com.marketplace.pacccioli.controller;

import com.marketplace.pacccioli.dto.ApiResponseDTO;
import com.marketplace.pacccioli.dto.InteraccionDTO;
import com.marketplace.pacccioli.model.Interaccion;
import com.marketplace.pacccioli.model.Interaccion.FuenteInteraccion;
import com.marketplace.pacccioli.model.Interaccion.TipoInteraccion;
import com.marketplace.pacccioli.model.Producto;
import com.marketplace.pacccioli.model.Usuario;
import com.marketplace.pacccioli.repository.InteraccionRepository;
import com.marketplace.pacccioli.repository.ProductoRepository;
import com.marketplace.pacccioli.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/interacciones")
public class InteraccionController {
    
    @Autowired
    private InteraccionRepository interaccionRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private ProductoRepository productoRepository;
    
    /**
     * POST /api/interacciones - Registrar una interacción (visualización, compra, etc.)
     */
    @PostMapping
    public ResponseEntity<ApiResponseDTO<InteraccionDTO>> registrarInteraccion(
            @RequestBody InteraccionDTO interaccionDTO) {
        try {
            // Validar que existan usuario y producto
            var usuario = usuarioRepository.findById(interaccionDTO.getUsuarioId());
            var producto = productoRepository.findById(interaccionDTO.getProductoId());
            
            if (usuario.isEmpty() || producto.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponseDTO<>(false, "Usuario o producto no encontrado", 
                                List.of("Verifique los IDs proporcionados")));
            }
            
            // Crear la interacción
            Interaccion interaccion = new Interaccion();
            interaccion.setUsuario(usuario.get());
            interaccion.setProducto(producto.get());
            interaccion.setTipoInteraccion(TipoInteraccion.valueOf(interaccionDTO.getTipo().toUpperCase()));
            interaccion.setFuente(FuenteInteraccion.valueOf(interaccionDTO.getFuente().toUpperCase()));
            interaccion.setLatitudUsuario(interaccionDTO.getLatitudUsuario());
            interaccion.setLongitudUsuario(interaccionDTO.getLongitudUsuario());
            interaccion.setContextoClima(interaccionDTO.getClimaContexto());
            if (interaccionDTO.getPrecioEnInteraccion() != null) {
                interaccion.setPrecioMomento(BigDecimal.valueOf(interaccionDTO.getPrecioEnInteraccion()));
            }
            
            Interaccion guardada = interaccionRepository.save(interaccion);
            InteraccionDTO dto = convertirADTO(guardada);
            
            ApiResponseDTO<InteraccionDTO> respuesta = new ApiResponseDTO<>(
                    true,
                    "Interacción registrada exitosamente",
                    dto
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al registrar interacción", 
                            List.of(e.getMessage())));
        }
    }
    
    /**
     * GET /api/interacciones/usuario/{usuarioId} - Obtener historial de interacciones de un usuario
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<ApiResponseDTO<List<InteraccionDTO>>> obtenerPorUsuario(
            @PathVariable Long usuarioId) {
        try {
            List<InteraccionDTO> interacciones = interaccionRepository.findByUsuarioId(usuarioId)
                    .stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
            
            ApiResponseDTO<List<InteraccionDTO>> respuesta = new ApiResponseDTO<>(
                    true,
                    "Interacciones obtenidas exitosamente",
                    interacciones
            );
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al obtener interacciones", 
                            List.of(e.getMessage())));
        }
    }
    
    /**
     * GET /api/interacciones/producto/{productoId} - Obtener interacciones de un producto
     */
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<ApiResponseDTO<List<InteraccionDTO>>> obtenerPorProducto(
            @PathVariable Long productoId) {
        try {
            List<InteraccionDTO> interacciones = interaccionRepository.findByProductoId(productoId)
                    .stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
            
            ApiResponseDTO<List<InteraccionDTO>> respuesta = new ApiResponseDTO<>(
                    true,
                    "Interacciones obtenidas exitosamente",
                    interacciones
            );
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al obtener interacciones", 
                            List.of(e.getMessage())));
        }
    }
    
    /**
     * GET /api/interacciones/tipo/{tipo} - Obtener interacciones por tipo
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<ApiResponseDTO<List<InteraccionDTO>>> obtenerPorTipo(
            @PathVariable String tipo) {
        try {
            List<InteraccionDTO> interacciones = interaccionRepository.findByTipoInteraccion(TipoInteraccion.valueOf(tipo.toUpperCase()))
                    .stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
            
            ApiResponseDTO<List<InteraccionDTO>> respuesta = new ApiResponseDTO<>(
                    true,
                    "Interacciones obtenidas exitosamente",
                    interacciones
            );
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al obtener interacciones", 
                            List.of(e.getMessage())));
        }
    }
    
    /**
     * GET /api/interacciones/usuario/{usuarioId}/compras - Obtener historial de compras
     */
    @GetMapping("/usuario/{usuarioId}/compras")
    public ResponseEntity<ApiResponseDTO<List<InteraccionDTO>>> obtenerCompras(
            @PathVariable Long usuarioId) {
        try {
            List<InteraccionDTO> compras = interaccionRepository.findByUsuarioIdAndTipoInteraccion(usuarioId, TipoInteraccion.COMPRA)
                    .stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
            
            ApiResponseDTO<List<InteraccionDTO>> respuesta = new ApiResponseDTO<>(
                    true,
                    "Compras obtenidas exitosamente",
                    compras
            );
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al obtener compras", 
                            List.of(e.getMessage())));
        }
    }
    
    /**
     * GET /api/interacciones/estadisticas/producto/{productoId} - Estadísticas de un producto
     */
    @GetMapping("/estadisticas/producto/{productoId}")
    public ResponseEntity<ApiResponseDTO<String>> estadisticasProducto(
            @PathVariable Long productoId) {
        try {
            Long visualizaciones = interaccionRepository.countByProductoIdAndTipoInteraccion(productoId, TipoInteraccion.VISUALIZACION);
            Long compras = interaccionRepository.countByProductoIdAndTipoInteraccion(productoId, TipoInteraccion.COMPRA);
            
            String stats = String.format("Visualizaciones: %d, Compras: %d", visualizaciones, compras);
            
            ApiResponseDTO<String> respuesta = new ApiResponseDTO<>(
                    true,
                    "Estadísticas obtenidas exitosamente",
                    stats
            );
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al obtener estadísticas", 
                            List.of(e.getMessage())));
        }
    }
    
    // Método auxiliar para convertir Interaccion a InteraccionDTO
    private InteraccionDTO convertirADTO(Interaccion interaccion) {
        InteraccionDTO dto = new InteraccionDTO();
        dto.setId(interaccion.getId());
        dto.setTipo(interaccion.getTipoInteraccion() != null ? interaccion.getTipoInteraccion().name() : null);
        dto.setFuente(interaccion.getFuente() != null ? interaccion.getFuente().name() : null);
        dto.setLatitudUsuario(interaccion.getLatitudUsuario());
        dto.setLongitudUsuario(interaccion.getLongitudUsuario());
        dto.setClimaContexto(interaccion.getContextoClima());
        dto.setPrecioEnInteraccion(interaccion.getPrecioMomento() != null ? interaccion.getPrecioMomento().doubleValue() : null);
        dto.setFecha(interaccion.getFechaInteraccion());
        
        if (interaccion.getUsuario() != null) {
            dto.setUsuarioId(interaccion.getUsuario().getId());
            dto.setNombreUsuario(interaccion.getUsuario().getNombre());
        }
        
        if (interaccion.getProducto() != null) {
            dto.setProductoId(interaccion.getProducto().getId());
            dto.setNombreProducto(interaccion.getProducto().getNombre());
            if (interaccion.getProducto().getComercio() != null) {
                dto.setComercioId(interaccion.getProducto().getComercio().getId());
            }
        }
        
        return dto;
    }
}
