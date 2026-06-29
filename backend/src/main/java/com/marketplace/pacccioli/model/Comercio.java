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
 * Entidad JPA que representa un comercio/local en el sistema.
 * Contiene información del local, descripción y coordenadas GPS.
 */
@Entity
@Table(name = "comercios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comercio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(nullable = false, length = 500)
    private String descripcion;
    
    @Column(length = 255)
    private String direccion;
    
    @Column(length = 20)
    private String telefono;
    
    @Column(length = 100)
    private String email;
    
    @Column(length = 100)
    private String horarioAtencion;
    
    /**
     * Categoría del comercio: RESTAURANTE, TIENDA, SUPERMERCADO, FARMACIA, etc.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Categoria categoria;
    
    /**
     * Coordenadas GPS del comercio para geolocalización
     */
    @Column(nullable = false)
    private Double latitud;
    
    @Column(nullable = false)
    private Double longitud;
    
    /**
     * URL de la imagen del comercio
     */
    @Column(length = 500)
    private String imagenUrl;
    
    /**
     * Calificación promedio del comercio (1-5)
     */
    @Column
    private Double calificacion = 0.0;
    
    /**
     * Número total de reseñas
     */
    private Integer numeroReseñas = 0;
    
    @Column(nullable = false)
    private Boolean activo = true;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime fechaActualizacion;
    
    /**
     * Propietario del comercio (Usuario con rol COMERCIANTE)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propietario_id", nullable = false)
    private Usuario propietario;
    
    /**
     * Lista de productos del comercio
     */
    @OneToMany(mappedBy = "comercio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Producto> productos;
    
    /**
     * Enumeración de categorías de comercios
     */
    public enum Categoria {
        RESTAURANTE,
        CAFE,
        TIENDA_ROPA,
        SUPERMERCADO,
        FARMACIA,
        LIBRERIA,
        ELECTRONICA,
        HOGAR,
        DEPORTES,
        BELLEZA,
        OTROS
    }
}
