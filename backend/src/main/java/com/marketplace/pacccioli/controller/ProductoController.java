package com.marketplace.pacccioli.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.pacccioli.dto.ApiResponseDTO;
import com.marketplace.pacccioli.dto.ProductoDTO;
import com.marketplace.pacccioli.model.Comercio;
import com.marketplace.pacccioli.model.Producto;
import com.marketplace.pacccioli.model.Producto.Categoria;
import com.marketplace.pacccioli.model.Producto.Estado;
import com.marketplace.pacccioli.model.Usuario;
import com.marketplace.pacccioli.repository.ComercioRepository;
import com.marketplace.pacccioli.repository.ProductoRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private ComercioRepository comercioRepository;
    
    /**
     * GET /api/productos - Obtener todos los productos
     */
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<ProductoDTO>>> obtenerTodos() {
        try {
            List<ProductoDTO> productos = productoRepository.findAll()
                    .stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
            
            ApiResponseDTO<List<ProductoDTO>> respuesta = new ApiResponseDTO<>(
                    true,
                    "Productos obtenidos exitosamente",
                    productos
            );
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al obtener productos", 
                            List.of(e.getMessage())));
        }
    }
    
    /**
     * GET /api/productos/{id} - Obtener un producto por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ProductoDTO>> obtenerPorId(@PathVariable Long id) {
        try {
            var producto = productoRepository.findById(id);
            if (producto.isPresent()) {
                ProductoDTO dto = convertirADTO(producto.get());
                ApiResponseDTO<ProductoDTO> respuesta = new ApiResponseDTO<>(
                        true,
                        "Producto obtenido exitosamente",
                        dto
                );
                return ResponseEntity.ok(respuesta);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(false, "Producto no encontrado", 
                                List.of("El producto con ID " + id + " no existe")));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al obtener producto", 
                            List.of(e.getMessage())));
        }
    }
    
    /**
     * GET /api/productos/buscar?nombre=X - Buscar productos por nombre
     */
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponseDTO<List<ProductoDTO>>> buscarPorNombre(
            @RequestParam String nombre) {
        try {
            List<ProductoDTO> productos = productoRepository.findByNombreContainingIgnoreCase(nombre)
                    .stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
            
            ApiResponseDTO<List<ProductoDTO>> respuesta = new ApiResponseDTO<>(
                    true,
                    "Búsqueda realizada exitosamente",
                    productos
            );
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error en búsqueda", 
                            List.of(e.getMessage())));
        }
    }
    
    /**
     * GET /api/productos/comercio/{comercioId} - Obtener productos de un comercio
     */
    @GetMapping("/comercio/{comercioId}")
    public ResponseEntity<ApiResponseDTO<List<ProductoDTO>>> obtenerPorComercio(
            @PathVariable Long comercioId) {
        try {
            List<ProductoDTO> productos = productoRepository.findByComercioIdAndActivoTrue(comercioId)
                    .stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
            
            ApiResponseDTO<List<ProductoDTO>> respuesta = new ApiResponseDTO<>(
                    true,
                    "Productos del comercio obtenidos exitosamente",
                    productos
            );
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al obtener productos", 
                            List.of(e.getMessage())));
        }
    }
    
    /**
     * GET /api/productos/populares - Obtener productos más vistos
     */
    @GetMapping("/populares")
    public ResponseEntity<ApiResponseDTO<List<ProductoDTO>>> obtenerPopulares() {
        try {
            List<ProductoDTO> productos = productoRepository.findByActivoTrueOrderByConteoVisualizacionesDesc()
                    .stream()
                    .limit(10)
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
            
            ApiResponseDTO<List<ProductoDTO>> respuesta = new ApiResponseDTO<>(
                    true,
                    "Productos populares obtenidos exitosamente",
                    productos
            );
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al obtener productos populares", 
                            List.of(e.getMessage())));
        }
    }
    
    /**
     * POST /api/productos - Crear un nuevo producto (solo COMERCIANTE)
     */
    @PostMapping
    public ResponseEntity<ApiResponseDTO<ProductoDTO>> crear(
            @RequestBody Map<String, Object> body,
            HttpServletRequest request) {
        try {
            Long usuarioId = (Long) request.getAttribute("usuarioId");
            String rol = (String) request.getAttribute("rol");
            
            if (usuarioId == null || !"COMERCIANTE".equals(rol)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponseDTO<>(false, "Solo comerciantes pueden crear productos",
                                List.of("Se requiere rol COMERCIANTE")));
            }
            
            // Buscar el comercio del usuario
            List<Comercio> comercios = comercioRepository.findByPropietarioId(usuarioId);
            if (comercios.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponseDTO<>(false, "No tienes un comercio registrado",
                                List.of("Primero debes crear un comercio")));
            }
            
            Comercio comercio = comercios.get(0);
            
            Producto producto = new Producto();
            producto.setNombre((String) body.get("nombre"));
            producto.setDescripcion((String) body.get("descripcion"));
            
            Object precioObj = body.get("precio");
            if (precioObj instanceof Number) {
                producto.setPrecio(BigDecimal.valueOf(((Number) precioObj).doubleValue()));
            }
            
            Object stockObj = body.get("stock");
            if (stockObj instanceof Number) {
                producto.setStock(((Number) stockObj).intValue());
            }
            
            producto.setUrlImagen((String) body.get("urlImagen"));
            
            // Procesar etiquetas inteligentes
            Object etiquetas = body.get("etiquetasInteligentes");
            if (etiquetas instanceof List) {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    List<String> lista = mapper.convertValue(etiquetas, new TypeReference<List<String>>() {});
                    producto.setEtiquetasInteligentes(mapper.writeValueAsString(lista));
                } catch (Exception e) {
                    producto.setEtiquetasInteligentes("[]");
                }
            } else if (etiquetas instanceof String) {
                producto.setEtiquetasInteligentes((String) etiquetas);
            } else {
                producto.setEtiquetasInteligentes("[]");
            }
            
            // Categoría
            String catStr = (String) body.get("categoria");
            try {
                producto.setCategoria(catStr != null ? Categoria.valueOf(catStr.toUpperCase()) : Categoria.OTROS);
            } catch (IllegalArgumentException e) {
                producto.setCategoria(Categoria.OTROS);
            }
            
            // Estado
            String estStr = (String) body.get("estado");
            try {
                producto.setEstado(estStr != null ? Estado.valueOf(estStr.toUpperCase()) : Estado.DISPONIBLE);
            } catch (IllegalArgumentException e) {
                producto.setEstado(Estado.DISPONIBLE);
            }
            
            producto.setActivo(true);
            producto.setComercio(comercio);
            producto.setFechaCreacion(LocalDateTime.now());
            producto.setFechaActualizacion(LocalDateTime.now());
            
            Producto guardado = productoRepository.save(producto);
            
            ApiResponseDTO<ProductoDTO> respuesta = new ApiResponseDTO<>(
                    true,
                    "Producto creado exitosamente",
                    convertirADTO(guardado)
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        } catch (Exception e) {
            log.error("Error al crear producto", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al crear producto",
                            List.of(e.getMessage())));
        }
    }
    
    /**
     * PUT /api/productos/{id} - Actualizar un producto (solo dueño del comercio)
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ProductoDTO>> actualizar(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body,
            HttpServletRequest request) {
        try {
            Long usuarioId = (Long) request.getAttribute("usuarioId");
            if (usuarioId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponseDTO<>(false, "Autenticación requerida", null));
            }
            
            var opt = productoRepository.findById(id);
            if (opt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(false, "Producto no encontrado", null));
            }
            
            Producto producto = opt.get();
            
            // Verificar que el producto pertenece al usuario
            if (!producto.getComercio().getPropietario().getId().equals(usuarioId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponseDTO<>(false, "No tienes permiso para modificar este producto", null));
            }
            
            // Actualizar campos
            if (body.containsKey("nombre")) producto.setNombre((String) body.get("nombre"));
            if (body.containsKey("descripcion")) producto.setDescripcion((String) body.get("descripcion"));
            
            Object precioObj = body.get("precio");
            if (precioObj instanceof Number) {
                producto.setPrecio(BigDecimal.valueOf(((Number) precioObj).doubleValue()));
            }
            
            Object stockObj = body.get("stock");
            if (stockObj instanceof Number) {
                producto.setStock(((Number) stockObj).intValue());
            }
            
            if (body.containsKey("urlImagen")) producto.setUrlImagen((String) body.get("urlImagen"));
            
            Object etiquetas = body.get("etiquetasInteligentes");
            if (etiquetas instanceof List) {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    List<String> lista = mapper.convertValue(etiquetas, new TypeReference<List<String>>() {});
                    producto.setEtiquetasInteligentes(mapper.writeValueAsString(lista));
                } catch (Exception e) {
                    // mantener valor actual
                }
            } else if (etiquetas instanceof String) {
                producto.setEtiquetasInteligentes((String) etiquetas);
            }
            
            if (body.containsKey("categoria")) {
                try {
                    producto.setCategoria(Categoria.valueOf(((String) body.get("categoria")).toUpperCase()));
                } catch (IllegalArgumentException ignored) {}
            }
            
            if (body.containsKey("estado")) {
                try {
                    producto.setEstado(Estado.valueOf(((String) body.get("estado")).toUpperCase()));
                } catch (IllegalArgumentException ignored) {}
            }
            
            producto.setFechaActualizacion(LocalDateTime.now());
            Producto guardado = productoRepository.save(producto);
            
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Producto actualizado", convertirADTO(guardado)));
        } catch (Exception e) {
            log.error("Error al actualizar producto", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al actualizar producto",
                            List.of(e.getMessage())));
        }
    }
    
    /**
     * DELETE /api/productos/{id} - Eliminar un producto (solo dueño del comercio)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> eliminar(
            @PathVariable Long id,
            HttpServletRequest request) {
        try {
            Long usuarioId = (Long) request.getAttribute("usuarioId");
            if (usuarioId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponseDTO<>(false, "Autenticación requerida", null));
            }
            
            var opt = productoRepository.findById(id);
            if (opt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(false, "Producto no encontrado", null));
            }
            
            Producto producto = opt.get();
            if (!producto.getComercio().getPropietario().getId().equals(usuarioId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponseDTO<>(false, "No tienes permiso para eliminar este producto", null));
            }
            
            productoRepository.delete(producto);
            
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Producto eliminado", null));
        } catch (Exception e) {
            log.error("Error al eliminar producto", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al eliminar producto",
                            List.of(e.getMessage())));
        }
    }
    
    /**
     * GET /api/productos/calificados - Obtener productos mejor calificados
     */
    @GetMapping("/calificados")
    public ResponseEntity<ApiResponseDTO<List<ProductoDTO>>> obtenerMejoresCalificados() {
        try {
            List<ProductoDTO> productos = productoRepository.findByActivoTrueOrderByCalificacionPromedioDesc()
                    .stream()
                    .limit(10)
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
            
            ApiResponseDTO<List<ProductoDTO>> respuesta = new ApiResponseDTO<>(
                    true,
                    "Productos mejor calificados obtenidos exitosamente",
                    productos
            );
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al obtener productos calificados", 
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
        dto.setCategoria(producto.getCategoria() != null ? producto.getCategoria().name() : null);
        dto.setEstado(producto.getEstado() != null ? producto.getEstado().name() : null);
        dto.setCalificacionPromedio(producto.getCalificacionPromedio());
        dto.setConteoVisualizaciones(producto.getConteoVisualizaciones());
        dto.setConteoCompras(producto.getConteoCompras());
        dto.setActivo(producto.getActivo());
        dto.setFechaCreacion(producto.getFechaCreacion());
        dto.setFechaActualizacion(producto.getFechaActualizacion());
        
        // Convertir etiquetasInteligentes de JSON string a List<String>
        if (producto.getEtiquetasInteligentes() != null && !producto.getEtiquetasInteligentes().isEmpty()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                List<String> etiquetas = mapper.readValue(producto.getEtiquetasInteligentes(),
                        new TypeReference<List<String>>() {});
                dto.setEtiquetasInteligentes(etiquetas);
            } catch (Exception e) {
                dto.setEtiquetasInteligentes(List.of());
            }
        } else {
            dto.setEtiquetasInteligentes(List.of());
        }
        
        if (producto.getComercio() != null) {
            dto.setComercioId(producto.getComercio().getId());
            dto.setNombreComercio(producto.getComercio().getNombre());
            dto.setLatitudComercio(producto.getComercio().getLatitud());
            dto.setLongitudComercio(producto.getComercio().getLongitud());
        }
        
        return dto;
    }
}
