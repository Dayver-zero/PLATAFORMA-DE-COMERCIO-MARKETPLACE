package com.marketplace.pacccioli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private String tipo; // Bearer
    private Long usuarioId;
    private String nombre;
    private String email;
    private String rol; // CLIENTE, COMERCIANTE, ADMIN
    
    public LoginResponseDTO(String token, String tipo, UsuarioDTO usuario) {
        this.token = token;
        this.tipo = tipo;
        this.usuarioId = usuario.getId();
        this.nombre = usuario.getNombre();
        this.email = usuario.getEmail();
        this.rol = usuario.getRol();
    }
}
