package com.marketplace.pacccioli.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Componente para generar y validar tokens JWT
 */
@Slf4j
@Component
public class JwtProvider {
    
    @Value("${jwt.secret:miClaveSecretaParaFirmarTokensJWT2024PlataformaComercioLocal}")
    private String jwtSecret;
    
    @Value("${jwt.expiration:86400000}")
    private long jwtExpirationMs;
    
    /**
     * Generar token JWT para un usuario
     * @param usuarioId ID del usuario
     * @param email Email del usuario
     * @param rol Rol del usuario (CLIENTE, COMERCIANTE, ADMIN)
     * @return Token JWT
     */
    public String generateToken(Long usuarioId, String email, String rol) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + jwtExpirationMs);
            
            String token = Jwts.builder()
                    .subject(usuarioId.toString())
                    .claim("email", email)
                    .claim("rol", rol)
                    .issuedAt(now)
                    .expiration(expiryDate)
                    .signWith(key)
                    .compact();
            
            log.info("Token generado para usuario: {}", usuarioId);
            return token;
        } catch (Exception e) {
            log.error("Error generando token JWT", e);
            throw new RuntimeException("Error generando token JWT", e);
        }
    }
    
    /**
     * Validar token JWT
     * @param token Token a validar
     * @return true si el token es válido
     */
    public boolean validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            
            return true;
        } catch (Exception e) {
            log.error("Token JWT inválido o expirado: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Extraer ID de usuario del token
     * @param token Token JWT
     * @return ID del usuario
     */
    public Long extractUsuarioId(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            
            return Long.parseLong(claims.getSubject());
        } catch (Exception e) {
            log.error("Error extrayendo usuario del token: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * Extraer email del token
     * @param token Token JWT
     * @return Email del usuario
     */
    public String extractEmail(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            
            return claims.get("email", String.class);
        } catch (Exception e) {
            log.error("Error extrayendo email del token: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * Extraer rol del token
     * @param token Token JWT
     * @return Rol del usuario
     */
    public String extractRol(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            
            return claims.get("rol", String.class);
        } catch (Exception e) {
            log.error("Error extrayendo rol del token: {}", e.getMessage());
            return null;
        }
    }
}
