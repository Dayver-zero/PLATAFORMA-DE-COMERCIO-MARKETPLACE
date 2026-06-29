package com.marketplace.pacccioli.controller;

import com.marketplace.pacccioli.dto.ApiResponseDTO;
import com.marketplace.pacccioli.dto.LoginRequestDTO;
import com.marketplace.pacccioli.dto.LoginResponseDTO;
import com.marketplace.pacccioli.dto.UsuarioDTO;
import com.marketplace.pacccioli.model.Comercio;
import com.marketplace.pacccioli.model.Usuario;
import com.marketplace.pacccioli.repository.ComercioRepository;
import com.marketplace.pacccioli.repository.UsuarioRepository;
import com.marketplace.pacccioli.security.JwtProvider;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private JwtProvider jwtProvider;
    
    @Autowired
    private ComercioRepository comercioRepository;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    /**
     * POST /api/auth/login - Autenticación de usuario
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<LoginResponseDTO>> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        try {
            // Buscar usuario por email
            var usuarioOpt = usuarioRepository.findByEmailIgnoreCase(loginRequest.getEmail());
            
            if (usuarioOpt.isEmpty()) {
                log.warn("Intento de login fallido: email no encontrado: {}", loginRequest.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponseDTO<>(false, "Credenciales inválidas", 
                                List.of("Email o contraseña incorrectos")));
            }
            
            Usuario usuario = usuarioOpt.get();
            
            // Verificar contraseña (usando BCrypt)
            if (!passwordEncoder.matches(loginRequest.getContrasena(), usuario.getPassword())) {
                log.warn("Intento de login fallido: contraseña incorrecta para: {}", loginRequest.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponseDTO<>(false, "Credenciales inválidas", 
                                List.of("Email o contraseña incorrectos")));
            }
            
            if (!usuario.getActivo()) {
                log.warn("Intento de login fallido: usuario inactivo: {}", loginRequest.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponseDTO<>(false, "Usuario inactivo", 
                                List.of("La cuenta ha sido desactivada")));
            }
            
            // Generar JWT token
            String token = jwtProvider.generateToken(usuario.getId(), usuario.getEmail(), usuario.getRol().name());
            
            LoginResponseDTO loginResponse = new LoginResponseDTO(
                    token,
                    "Bearer",
                    convertirADTO(usuario)
            );
            
            ApiResponseDTO<LoginResponseDTO> respuesta = new ApiResponseDTO<>(
                    true,
                    "Login exitoso",
                    loginResponse
            );
            
            log.info("Login exitoso para usuario: {}", usuario.getEmail());
            return ResponseEntity.ok(respuesta);
            
        } catch (Exception e) {
            log.error("Error en login", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error en autenticación", 
                            List.of(e.getMessage())));
        }
    }
    
    /**
     * POST /api/auth/registro - Registro de nuevo usuario
     */
    @PostMapping("/registro")
    public ResponseEntity<ApiResponseDTO<LoginResponseDTO>> registro(@Valid @RequestBody LoginRequestDTO registroRequest) {
        try {
            // Verificar si el email ya existe
            if (usuarioRepository.existsByEmail(registroRequest.getEmail())) {
                log.warn("Intento de registro con email duplicado: {}", registroRequest.getEmail());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponseDTO<>(false, "Email ya registrado", 
                                List.of("Este email ya está asociado a una cuenta")));
            }
            
            // Crear nuevo usuario
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setEmail(registroRequest.getEmail());
            nuevoUsuario.setPassword(passwordEncoder.encode(registroRequest.getContrasena()));
            // Usar parte local del email como username (antes del @), truncado a 50 chars
            String usernameBase = registroRequest.getEmail().split("@")[0];
            nuevoUsuario.setUsername(usernameBase.length() > 50 ? usernameBase.substring(0, 50) : usernameBase);
            nuevoUsuario.setNombre(registroRequest.getEmail().split("@")[0]); // Usar parte del email como nombre inicial
            
            // Determinar rol: si se envió COMERCIANTE, usarlo; por defecto CLIENTE
            String rolStr = registroRequest.getRol();
            Usuario.Rol rol = Usuario.Rol.CLIENTE;
            if (rolStr != null && rolStr.equalsIgnoreCase("COMERCIANTE")) {
                rol = Usuario.Rol.COMERCIANTE;
            }
            nuevoUsuario.setRol(rol);
            nuevoUsuario.setLatitud(-17.5528); // Coordenadas de Punata
            nuevoUsuario.setLongitud(-65.8756);
            nuevoUsuario.setRadioBusquedaKm(5);
            nuevoUsuario.setActivo(true);
            nuevoUsuario.setFechaCreacion(LocalDateTime.now());
            nuevoUsuario.setFechaActualizacion(LocalDateTime.now());
            
            Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);
            
            // Si es COMERCIANTE, crear un comercio por defecto
            if (rol == Usuario.Rol.COMERCIANTE) {
                Comercio nuevoComercio = new Comercio();
                nuevoComercio.setNombre("Tienda de " + nuevoUsuario.getNombre());
                nuevoComercio.setDescripcion("Comercio registrado en Mercado Punata");
                nuevoComercio.setDireccion("Punata, Cochabamba, Bolivia");
                nuevoComercio.setCategoria(Comercio.Categoria.OTROS);
                nuevoComercio.setLatitud(nuevoUsuario.getLatitud());
                nuevoComercio.setLongitud(nuevoUsuario.getLongitud());
                nuevoComercio.setActivo(true);
                nuevoComercio.setPropietario(usuarioGuardado);
                nuevoComercio.setFechaCreacion(LocalDateTime.now());
                nuevoComercio.setFechaActualizacion(LocalDateTime.now());
                comercioRepository.save(nuevoComercio);
            }
            
            // Generar JWT token
            String token = jwtProvider.generateToken(usuarioGuardado.getId(), usuarioGuardado.getEmail(), usuarioGuardado.getRol().name());
            
            LoginResponseDTO loginResponse = new LoginResponseDTO(
                    token,
                    "Bearer",
                    convertirADTO(usuarioGuardado)
            );
            
            ApiResponseDTO<LoginResponseDTO> respuesta = new ApiResponseDTO<>(
                    true,
                    "Registro exitoso",
                    loginResponse
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error en registro", 
                            List.of(e.getMessage())));
        }
    }
    
    /**
     * POST /api/auth/logout - Logout de usuario (opcional)
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDTO<String>> logout() {
        try {
            ApiResponseDTO<String> respuesta = new ApiResponseDTO<>(
                    true,
                    "Logout exitoso",
                    "Usuario desconectado correctamente"
            );
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error en logout", 
                            List.of(e.getMessage())));
        }
    }
    
    /**
     * POST /api/auth/verificar-email - Verificar si un email está disponible
     */
    @PostMapping("/verificar-email")
    public ResponseEntity<ApiResponseDTO<Boolean>> verificarEmail(@RequestParam String email) {
        try {
            boolean existe = usuarioRepository.existsByEmail(email);
            
            ApiResponseDTO<Boolean> respuesta = new ApiResponseDTO<>(
                    true,
                    existe ? "Email ya registrado" : "Email disponible",
                    !existe
            );
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al verificar email", 
                            List.of(e.getMessage())));
        }
    }
    
    // Método auxiliar para convertir Usuario a UsuarioDTO
    private UsuarioDTO convertirADTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setEmail(usuario.getEmail());
        dto.setRol(usuario.getRol() != null ? usuario.getRol().name() : null);
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
