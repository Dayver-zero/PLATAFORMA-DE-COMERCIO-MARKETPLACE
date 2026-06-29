package com.marketplace.pacccioli.controller;

import com.marketplace.pacccioli.dto.ApiResponseDTO;
import com.marketplace.pacccioli.dto.UsuarioDTO;
import com.marketplace.pacccioli.model.Comercio;
import com.marketplace.pacccioli.model.Producto;
import com.marketplace.pacccioli.model.Usuario;
import com.marketplace.pacccioli.repository.ComercioRepository;
import com.marketplace.pacccioli.repository.ProductoRepository;
import com.marketplace.pacccioli.repository.UsuarioRepository;
import com.marketplace.pacccioli.security.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private ComercioRepository comercioRepository;
    
    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private JwtProvider jwtProvider;
    
    /**
     * GET /api/usuarios/{id} - Obtener un usuario por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<UsuarioDTO>> obtenerPorId(@PathVariable Long id) {
        try {
            var usuario = usuarioRepository.findById(id);
            if (usuario.isPresent()) {
                UsuarioDTO dto = convertirADTO(usuario.get());
                ApiResponseDTO<UsuarioDTO> respuesta = new ApiResponseDTO<>(
                        true,
                        "Usuario obtenido exitosamente",
                        dto
                );
                return ResponseEntity.ok(respuesta);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(false, "Usuario no encontrado", 
                                List.of("El usuario con ID " + id + " no existe")));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al obtener usuario", 
                            List.of(e.getMessage())));
        }
    }
    
    /**
     * GET /api/usuarios/email/{email} - Obtener usuario por email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponseDTO<UsuarioDTO>> obtenerPorEmail(@PathVariable String email) {
        try {
            var usuario = usuarioRepository.findByEmailIgnoreCase(email);
            if (usuario.isPresent()) {
                UsuarioDTO dto = convertirADTO(usuario.get());
                ApiResponseDTO<UsuarioDTO> respuesta = new ApiResponseDTO<>(
                        true,
                        "Usuario obtenido exitosamente",
                        dto
                );
                return ResponseEntity.ok(respuesta);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(false, "Usuario no encontrado", 
                                List.of("No existe usuario con email " + email)));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al obtener usuario", 
                            List.of(e.getMessage())));
        }
    }
    
    /**
     * GET /api/usuarios/rol/{rol} - Obtener usuarios activos por rol
     */
    @GetMapping("/rol/{rol}")
    public ResponseEntity<ApiResponseDTO<List<UsuarioDTO>>> obtenerPorRol(@PathVariable String rol) {
        try {
            Usuario.Rol enumRol = Usuario.Rol.valueOf(rol.toUpperCase());
            List<UsuarioDTO> usuarios = usuarioRepository.findByRolAndActivoTrue(enumRol)
                    .stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
            
            ApiResponseDTO<List<UsuarioDTO>> respuesta = new ApiResponseDTO<>(
                    true,
                    "Usuarios obtenidos por rol exitosamente",
                    usuarios
            );
            return ResponseEntity.ok(respuesta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDTO<>(false, "Rol inválido: " + rol, 
                            List.of("Los roles válidos son: CLIENTE, COMERCIANTE, ADMIN")));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al obtener usuarios", 
                            List.of(e.getMessage())));
        }
    }
    
    /**
     * GET /api/usuarios - Obtener todos los usuarios activos (solo admin)
     */
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<UsuarioDTO>>> obtenerActivos() {
        try {
            List<UsuarioDTO> usuarios = usuarioRepository.findByActivoTrue()
                    .stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
            
            ApiResponseDTO<List<UsuarioDTO>> respuesta = new ApiResponseDTO<>(
                    true,
                    "Usuarios obtenidos exitosamente",
                    usuarios
            );
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al obtener usuarios", 
                            List.of(e.getMessage())));
        }
    }
    
    /**
     * PUT /api/usuarios/{id} - Actualizar perfil de usuario
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<UsuarioDTO>> actualizar(
            @PathVariable Long id,
            @RequestBody UsuarioDTO usuarioDTO,
            HttpServletRequest request) {
        try {
            Long usuarioAutenticado = (Long) request.getAttribute("usuarioId");
            if (usuarioAutenticado == null || !usuarioAutenticado.equals(id)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponseDTO<>(false, "No tienes permiso para modificar este usuario", null));
            }
            
            var usuario = usuarioRepository.findById(id);
            if (usuario.isPresent()) {
                Usuario u = usuario.get();
                
                // Actualizar solo los campos permitidos
                if (usuarioDTO.getNombre() != null) {
                    u.setNombre(usuarioDTO.getNombre());
                }
                if (usuarioDTO.getLatitud() != null) {
                    u.setLatitud(usuarioDTO.getLatitud());
                }
                if (usuarioDTO.getLongitud() != null) {
                    u.setLongitud(usuarioDTO.getLongitud());
                }
                if (usuarioDTO.getRadioBusquedaKm() != null) {
                    u.setRadioBusquedaKm(usuarioDTO.getRadioBusquedaKm());
                }
                if (usuarioDTO.getPreferencias() != null) {
                    u.setPreferencias(usuarioDTO.getPreferencias());
                }
                
                Usuario actualizado = usuarioRepository.save(u);
                UsuarioDTO dto = convertirADTO(actualizado);
                
                ApiResponseDTO<UsuarioDTO> respuesta = new ApiResponseDTO<>(
                        true,
                        "Usuario actualizado exitosamente",
                        dto
                );
                return ResponseEntity.ok(respuesta);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(false, "Usuario no encontrado", 
                                List.of("El usuario con ID " + id + " no existe")));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al actualizar usuario", 
                            List.of(e.getMessage())));
        }
    }
    
    /**
     * DELETE /api/usuarios/cuenta - Eliminar (desactivar) la cuenta del usuario autenticado
     * Realiza soft-delete: marca como inactivo el usuario, sus comercios y productos
     */
    @DeleteMapping("/cuenta")
    public ResponseEntity<ApiResponseDTO<Void>> eliminarCuenta(HttpServletRequest request) {
        try {
            Long usuarioId = (Long) request.getAttribute("usuarioId");
            if (usuarioId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponseDTO<>(false, "Autenticacion requerida", null));
            }

            var opt = usuarioRepository.findById(usuarioId);
            if (opt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(false, "Usuario no encontrado", null));
            }

            Usuario usuario = opt.get();
            if (!usuario.getActivo()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponseDTO<>(false, "La cuenta ya esta desactivada", null));
            }

            // Soft-delete: desactivar comercios y productos del usuario si es COMERCIANTE
            if (usuario.getRol() == Usuario.Rol.COMERCIANTE) {
                List<Comercio> comercios = comercioRepository.findByPropietarioId(usuarioId);
                for (Comercio c : comercios) {
                    List<Producto> productos = productoRepository.findByComercioId(c.getId());
                    for (Producto p : productos) {
                        p.setActivo(false);
                        p.setFechaActualizacion(LocalDateTime.now());
                    }
                    productoRepository.saveAll(productos);
                    c.setActivo(false);
                    c.setFechaActualizacion(LocalDateTime.now());
                }
                comercioRepository.saveAll(comercios);
            }

            // Desactivar el usuario
            usuario.setActivo(false);
            usuario.setFechaActualizacion(LocalDateTime.now());
            usuarioRepository.save(usuario);

            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Cuenta eliminada exitosamente", null));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al eliminar cuenta",
                            List.of(e.getMessage())));
        }
    }

    /**
     * PUT /api/usuarios/{id}/rol - Cambiar el rol del usuario autenticado
     * CLIENTE -> COMERCIANTE: crea un comercio por defecto
     * COMERCIANTE -> CLIENTE: desactiva todos sus comercios y productos
     */
    @PutMapping("/{id}/rol")
    public ResponseEntity<ApiResponseDTO<UsuarioDTO>> cambiarRol(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            HttpServletRequest request) {
        try {
            Long usuarioAutenticado = (Long) request.getAttribute("usuarioId");
            if (usuarioAutenticado == null || !usuarioAutenticado.equals(id)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponseDTO<>(false, "No tienes permiso para modificar este usuario", null));
            }

            var opt = usuarioRepository.findById(id);
            if (opt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(false, "Usuario no encontrado", null));
            }

            Usuario usuario = opt.get();
            String nuevoRolStr = body.get("rol");
            if (nuevoRolStr == null || nuevoRolStr.isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponseDTO<>(false, "Debes especificar un rol", null));
            }

            Usuario.Rol nuevoRol;
            try {
                nuevoRol = Usuario.Rol.valueOf(nuevoRolStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponseDTO<>(false, "Rol invalido: " + nuevoRolStr,
                                List.of("Los roles validos son: CLIENTE, COMERCIANTE, ADMIN")));
            }

            Usuario.Rol rolActual = usuario.getRol();

            if (rolActual == nuevoRol) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponseDTO<>(false, "El usuario ya tiene el rol " + nuevoRol.name(), null));
            }

            // CLIENTE -> COMERCIANTE: crear comercio por defecto
            if (rolActual == Usuario.Rol.CLIENTE && nuevoRol == Usuario.Rol.COMERCIANTE) {
                Comercio nuevoComercio = new Comercio();
                nuevoComercio.setNombre("Tienda de " + (usuario.getNombre() != null ? usuario.getNombre() : usuario.getEmail().split("@")[0]));
                nuevoComercio.setDescripcion("Comercio registrado en Mercado Punata");
                nuevoComercio.setDireccion("Punata, Cochabamba, Bolivia");
                nuevoComercio.setCategoria(Comercio.Categoria.OTROS);
                nuevoComercio.setLatitud(usuario.getLatitud() != null ? usuario.getLatitud() : -17.5528);
                nuevoComercio.setLongitud(usuario.getLongitud() != null ? usuario.getLongitud() : -65.8756);
                nuevoComercio.setActivo(true);
                nuevoComercio.setPropietario(usuario);
                nuevoComercio.setFechaCreacion(LocalDateTime.now());
                nuevoComercio.setFechaActualizacion(LocalDateTime.now());
                comercioRepository.save(nuevoComercio);
            }

            // COMERCIANTE -> CLIENTE: desactivar comercios y productos
            if (rolActual == Usuario.Rol.COMERCIANTE && nuevoRol == Usuario.Rol.CLIENTE) {
                List<Comercio> comercios = comercioRepository.findByPropietarioId(id);
                for (Comercio c : comercios) {
                    List<Producto> productos = productoRepository.findByComercioId(c.getId());
                    for (Producto p : productos) {
                        p.setActivo(false);
                        p.setFechaActualizacion(LocalDateTime.now());
                    }
                    productoRepository.saveAll(productos);
                    c.setActivo(false);
                    c.setFechaActualizacion(LocalDateTime.now());
                }
                comercioRepository.saveAll(comercios);
            }

            usuario.setRol(nuevoRol);
            usuario.setFechaActualizacion(LocalDateTime.now());
            Usuario actualizado = usuarioRepository.save(usuario);

            UsuarioDTO dto = convertirADTO(actualizado);
            String nuevoToken = jwtProvider.generateToken(
                    actualizado.getId(), actualizado.getEmail(), nuevoRol.name());
            Map<String, Object> data = new HashMap<>();
            data.put("usuario", dto);
            data.put("token", nuevoToken);
            return ResponseEntity.ok(new ApiResponseDTO<>(true,
                    "Rol cambiado a " + nuevoRol.name() + " exitosamente", data));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al cambiar rol",
                            List.of(e.getMessage())));
        }
    }

    // Método auxiliar para convertir Usuario a UsuarioDTO
    private UsuarioDTO convertirADTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setEmail(usuario.getEmail());
        dto.setRol(usuario.getRol().name());
        dto.setLatitud(usuario.getLatitud());
        dto.setLongitud(usuario.getLongitud());
        dto.setRadioBusquedaKm(usuario.getRadioBusquedaKm());
        dto.setPreferencias(usuario.getPreferencias());
        dto.setHistorialBusqueda(usuario.getHistorialBusqueda());
        dto.setActivo(usuario.getActivo());
        dto.setFechaCreacion(usuario.getFechaCreacion());
        dto.setFechaActualizacion(usuario.getFechaActualizacion());
        return dto;
    }
}
