package com.marketplace.pacccioli.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad JPA que representa a un usuario del sistema.
 * Puede ser Cliente o Comerciante.
 */
@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 50)
    private String username;
    
    @Column(nullable = false, length = 100)
    private String password;
    
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    
    @Column(length = 100)
    private String nombre;
    
    @Column(length = 100)
    private String apellidos;
    
    @Column(length = 20)
    private String telefono;
    
    @Column(length = 255)
    private String direccion;
    
    /**
     * Rol del usuario: CLIENTE o COMERCIANTE
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Rol rol;
    
    /**
     * Preferencias del usuario para recomendaciones personalizadas
     * Formato JSON: {"categorias": ["ropa", "comida"], "rangoPrecio": "medio"}
     */
    @Column(columnDefinition = "TEXT")
    private String preferencias;
    
    /**
     * Historial de búsquedas del usuario
     * Formato JSON: [{"termino": "paraguas", "fecha": "2024-01-15"}]
     */
    @Column(columnDefinition = "TEXT")
    private String historialBusqueda;
    
    /**
     * Coordenadas GPS del usuario para geolocalización
     */
    @Column
    private Double latitud;
    
    @Column
    private Double longitud;
    
    /**
     * Radio de búsqueda en kilómetros
     */
    @Column(name = "radio_busqueda_km")
    private Integer radioBusquedaKm = 5;
    
    @Column(nullable = false)
    private Boolean activo = true;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime fechaActualizacion;
    
    /**
     * Lista de comercios asociados (solo para comerciantes)
     */
    @OneToMany(mappedBy = "propietario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comercio> comercios;
    
    /**
     * Lista de interacciones del usuario
     */
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Interaccion> interacciones;
    
    /**
     * Enumeración de roles posibles en el sistema
     */
    public enum Rol {
        CLIENTE,
        COMERCIANTE,
        ADMIN
    }
}
