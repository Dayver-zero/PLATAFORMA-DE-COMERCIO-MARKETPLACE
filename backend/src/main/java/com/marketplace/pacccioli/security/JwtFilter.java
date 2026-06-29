package com.marketplace.pacccioli.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Filtro para validar JWT en cada petición a /api/**
 */
@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtProvider jwtProvider;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String path = request.getRequestURI();
            
            // Solo procesar rutas /api/
            if (path.startsWith("/api/")) {
                String authHeader = request.getHeader("Authorization");
                
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    String token = authHeader.substring(7);
                    
                    if (jwtProvider.validateToken(token)) {
                        // Agregar info del usuario al request para uso posterior
                        Long usuarioId = jwtProvider.extractUsuarioId(token);
                        String email = jwtProvider.extractEmail(token);
                        String rol = jwtProvider.extractRol(token);
                        
                        request.setAttribute("usuarioId", usuarioId);
                        request.setAttribute("email", email);
                        request.setAttribute("rol", rol);
                        
                        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                    email, null, List.of(new SimpleGrantedAuthority("ROLE_" + rol))
                            );
                            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                        
                        log.debug("Token válido para usuario: {} ({})", usuarioId, email);
                    } else {
                        log.warn("Token JWT inválido o expirado para: {}", path);
                    }
                }
                // Si no hay token, simplemente pasamos (SecurityConfig decidirá acceso)
            }
            
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Error en JwtFilter", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"exito\":false,\"mensaje\":\"Error interno del servidor\"}");
            response.setContentType("application/json");
        }
    }
}
