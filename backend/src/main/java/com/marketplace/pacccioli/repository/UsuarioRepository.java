package com.marketplace.pacccioli.repository;

import com.marketplace.pacccioli.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Buscar usuario por email
    Optional<Usuario> findByEmail(String email);
    
    // Buscar usuario por email ignoring case
    Optional<Usuario> findByEmailIgnoreCase(String email);
    
    // Verificar si existe email
    boolean existsByEmail(String email);
    
    // Buscar usuarios activos
    List<Usuario> findByActivoTrue();
    
    // Buscar usuarios por rol
    List<Usuario> findByRol(Usuario.Rol rol);
    
    // Buscar comerciantes activos
    List<Usuario> findByRolAndActivoTrue(Usuario.Rol rol);
}
