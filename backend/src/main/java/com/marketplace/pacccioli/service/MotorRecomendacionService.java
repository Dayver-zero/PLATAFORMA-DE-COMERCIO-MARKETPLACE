package com.marketplace.pacccioli.service;

import com.marketplace.pacccioli.dto.ProductoDTO;
import com.marketplace.pacccioli.model.Interaccion;
import com.marketplace.pacccioli.model.Producto;
import com.marketplace.pacccioli.model.Usuario;
import com.marketplace.pacccioli.repository.InteraccionRepository;
import com.marketplace.pacccioli.repository.ProductoRepository;
import com.marketplace.pacccioli.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Motor de recomendaciones inteligente que combina múltiples factores:
 * - Clima actual (etiquetas inteligentes de productos)
 * - Ubicación del usuario (comercios cercanos)
 * - Historial de interacciones (comportamiento del usuario)
 */
@Service
@Slf4j
public class MotorRecomendacionService {
    
    @Autowired
    private GeolocalizacionService geolocalizacionService;
    
    @Autowired
    private ClimaService climaService;
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private InteraccionRepository interaccionRepository;
    
    // Pesos para algoritmo híbrido (configurables via application.properties)
    @Value("${recomendacion.peso-clima:0.3}")
    private double pesoClima;

    @Value("${recomendacion.peso-ubicacion:0.3}")
    private double pesoUbicacion;

    @Value("${recomendacion.peso-historial:0.4}")
    private double pesoHistorial;
    
    /**
     * Genera recomendaciones personalizadas combinando clima, ubicación e historial
     * 
     * @param usuarioId ID del usuario
     * @param latitud Latitud actual del usuario
     * @param longitud Longitud actual del usuario
     * @return Lista de RecomendacionDTO ordenada por relevancia
     */
    public List<RecomendacionDTO> generarRecomendaciones(Long usuarioId, Double latitud, Double longitud) {
        try {
            log.info("Generando recomendaciones para usuario {} en ubicación ({}, {})", 
                    usuarioId, latitud, longitud);
            
            var usuario = usuarioRepository.findById(usuarioId);
            if (usuario.isEmpty()) {
                log.warn("Usuario {} no encontrado", usuarioId);
                return Collections.emptyList();
            }
            
            // Obtener todos los productos disponibles
            List<Producto> productosDisponibles = productoRepository.findByActivoTrueOrderByConteoVisualizacionesDesc();
            
            // Calcular puntaje para cada producto
            List<RecomendacionDTO> recomendaciones = productosDisponibles.stream()
                    .map(producto -> calcularRecomendacion(usuario.get(), producto, latitud, longitud))
                    .filter(r -> r.getPuntajeTotal() > 0)
                    .sorted(Comparator.comparingDouble(RecomendacionDTO::getPuntajeTotal).reversed())
                    .limit(20)
                    .collect(Collectors.toList());
            
            log.info("Se generaron {} recomendaciones para el usuario {}", recomendaciones.size(), usuarioId);
            return recomendaciones;
            
        } catch (Exception e) {
            log.error("Error al generar recomendaciones", e);
            return Collections.emptyList();
        }
    }
    
    /**
     * Recomendaciones basadas únicamente en clima actual
     */
    public List<ProductoDTO> recomendarPorClima(Double latitud, Double longitud) {
        try {
            log.info("Generando recomendaciones por clima para ubicación ({}, {})", latitud, longitud);
            
            ClimaService.ClimaDTO clima = climaService.obtenerClimaPorCoordenadas(latitud, longitud);
            if (clima == null) {
                log.warn("No se pudo obtener datos de clima");
                return Collections.emptyList();
            }
            
            // Buscar productos con etiquetas coincidentes
            List<Producto> productos = productoRepository.findByActivoTrueOrderByConteoVisualizacionesDesc();
            
            return productos.stream()
                    .filter(p -> coincideConClima(p, clima))
                    .limit(15)
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
            
        } catch (Exception e) {
            log.error("Error al recomendar por clima", e);
            return Collections.emptyList();
        }
    }
    
    /**
     * Recomendaciones basadas en historial de interacciones del usuario
     */
    public List<ProductoDTO> recomendarPorHistorial(Long usuarioId) {
        try {
            log.info("Generando recomendaciones por historial para usuario {}", usuarioId);
            
            // Obtener productos más interactuados
            List<Interaccion> interacciones = interaccionRepository.findByUsuarioId(usuarioId);
            
            if (interacciones.isEmpty()) {
                log.info("Usuario {} sin historial, retornando productos populares", usuarioId);
                return productoRepository.findByActivoTrueOrderByConteoVisualizacionesDesc()
                        .stream()
                        .limit(15)
                        .map(this::convertirADTO)
                        .collect(Collectors.toList());
            }
            
            // Extraer IDs de productos del historial
            Set<Long> productosVistos = interacciones.stream()
                    .map(i -> i.getProducto().getId())
                    .collect(Collectors.toSet());
            
            // Obtener productos similares a los vistos
            return productoRepository.findByActivoTrueOrderByCalificacionPromedioDesc()
                    .stream()
                    .filter(p -> !productosVistos.contains(p.getId()))
                    .limit(15)
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
            
        } catch (Exception e) {
            log.error("Error al recomendar por historial", e);
            return Collections.emptyList();
        }
    }
    
    /**
     * Recomendaciones basadas en proximidad geográfica
     */
    public List<ProductoDTO> recomendarPorUbicacion(Double latitud, Double longitud, Double radioKm) {
        try {
            log.info("Generando recomendaciones por ubicación (radio {} km)", radioKm);
            
            List<Producto> productos = productoRepository.findByActivoTrueOrderByConteoVisualizacionesDesc();
            
            return productos.stream()
                    .filter(p -> p.getComercio() != null)
                    .filter(p -> geolocalizacionService.estaEnRadio(
                            latitud, longitud,
                            p.getComercio().getLatitud(),
                            p.getComercio().getLongitud(),
                            radioKm))
                    .limit(15)
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
            
        } catch (Exception e) {
            log.error("Error al recomendar por ubicación", e);
            return Collections.emptyList();
        }
    }
    
    /**
     * Calcula el puntaje de relevancia de un producto para un usuario
     */
    private RecomendacionDTO calcularRecomendacion(Usuario usuario, Producto producto, Double latitud, Double longitud) {
        RecomendacionDTO recomendacion = new RecomendacionDTO();
        recomendacion.setProductoId(producto.getId());
        recomendacion.setNombreProducto(producto.getNombre());
        recomendacion.setPrecio(producto.getPrecio() != null ? producto.getPrecio().doubleValue() : null);
        
        // Puntaje por clima
        double puntajeClima = calcularPuntajeClima(producto, latitud, longitud);
        recomendacion.setPuntajeClima(puntajeClima);
        
        // Puntaje por ubicación
        double puntajeUbicacion = calcularPuntajeUbicacion(producto, latitud, longitud);
        recomendacion.setPuntajeUbicacion(puntajeUbicacion);
        
        // Puntaje por historial
        double puntajeHistorial = calcularPuntajeHistorial(usuario, producto);
        recomendacion.setPuntajeHistorial(puntajeHistorial);
        
        // Puntaje total (promedio ponderado)
        double puntajeTotal = (puntajeClima * pesoClima) +
                            (puntajeUbicacion * pesoUbicacion) +
                            (puntajeHistorial * pesoHistorial);
        
        recomendacion.setPuntajeTotal(Math.round(puntajeTotal * 100.0) / 100.0);
        
        return recomendacion;
    }
    
    /**
     * Calcula puntaje basado en coincidencia con clima (0.0 a 1.0)
     */
    private double calcularPuntajeClima(Producto producto, Double latitud, Double longitud) {
        try {
            ClimaService.ClimaDTO clima = climaService.obtenerClimaPorCoordenadas(latitud, longitud);
            if (clima == null) {
                return 0.5; // Valor neutral si no hay datos de clima
            }
            
            if (coincideConClima(producto, clima)) {
                return 1.0;
            }
            return 0.5;
            
        } catch (Exception e) {
            log.debug("Error al calcular puntaje de clima: {}", e.getMessage());
            return 0.5;
        }
    }
    
    /**
     * Calcula puntaje basado en ubicación del comercio (0.0 a 1.0)
     */
    private double calcularPuntajeUbicacion(Producto producto, Double latitud, Double longitud) {
        try {
            if (producto.getComercio() == null) {
                return 0.0;
            }
            
            Double distancia = geolocalizacionService.calcularDistancia(
                    latitud, longitud,
                    producto.getComercio().getLatitud(),
                    producto.getComercio().getLongitud());
            
            if (distancia == null) {
                return 0.5;
            }
            
            // Máximo 5 km para puntuación completa
            return Math.max(0, 1.0 - (distancia / 5.0));
            
        } catch (Exception e) {
            log.debug("Error al calcular puntaje de ubicación: {}", e.getMessage());
            return 0.5;
        }
    }
    
    /**
     * Calcula puntaje basado en historial del usuario (0.0 a 1.0)
     */
    private double calcularPuntajeHistorial(Usuario usuario, Producto producto) {
        try {
            List<Interaccion> interacciones = interaccionRepository.findByUsuarioId(usuario.getId());
            
            // Contar interacciones con el producto
            long interaccionesProducto = interacciones.stream()
                    .filter(i -> i.getProducto().getId().equals(producto.getId()))
                    .count();
            
            if (interaccionesProducto > 0) {
                return 0.8; // Ya lo vio, probablemente le interese
            }
            
            // Calificar por popularidad del producto
            double calificacion = (producto.getCalificacionPromedio() != null) ? 
                    producto.getCalificacionPromedio() / 5.0 : 0.5;
            
            return Math.min(1.0, calificacion);
            
        } catch (Exception e) {
            log.debug("Error al calcular puntaje de historial: {}", e.getMessage());
            return 0.5;
        }
    }
    
    /**
     * Verifica si un producto coincide con las condiciones climáticas actuales
     */
    private boolean coincideConClima(Producto producto, ClimaService.ClimaDTO clima) {
        if (producto.getEtiquetasInteligentes() == null) {
            return false;
        }
        
        String etiquetas = producto.getEtiquetasInteligentes().toLowerCase();
        String condicion = (clima.getCondicion() != null) ? clima.getCondicion().toLowerCase() : "";
        
        // Mapeos de coincidencia
        if (etiquetas.contains("lluvia") && condicion.contains("rain")) return true;
        if (etiquetas.contains("frío") && clima.getTemperatura() < 10) return true;
        if (etiquetas.contains("calor") && clima.getTemperatura() > 25) return true;
        if (etiquetas.contains("soleado") && condicion.contains("clear")) return true;
        
        return false;
    }
    
    /**
     * Convierte Producto a ProductoDTO
     */
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
        
        if (producto.getComercio() != null) {
            dto.setComercioId(producto.getComercio().getId());
            dto.setNombreComercio(producto.getComercio().getNombre());
            dto.setLatitudComercio(producto.getComercio().getLatitud());
            dto.setLongitudComercio(producto.getComercio().getLongitud());
        }
        
        return dto;
    }
    
    /**
     * DTO para una recomendación con desglose de puntajes
     */
    @Data
    @AllArgsConstructor
    public static class RecomendacionDTO {
        private Long productoId;
        private String nombreProducto;
        private Double precio;
        private Double puntajeClima;
        private Double puntajeUbicacion;
        private Double puntajeHistorial;
        private Double puntajeTotal;
        
        public RecomendacionDTO() {
            this.puntajeClima = 0.0;
            this.puntajeUbicacion = 0.0;
            this.puntajeHistorial = 0.0;
            this.puntajeTotal = 0.0;
        }
    }
}
