package com.marketplace.pacccioli.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad JPA que representa un producto en el sistema.
 * Contiene información del producto, precio, stock y etiquetas inteligentes.
 */
@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(nullable = false, length = 500)
    private String descripcion;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;
    
    @Column(nullable = false)
    private Integer stock = 0;
    
    /**
     * URL de la imagen del producto
     */
    @Column(length = 500)
    private String urlImagen;
    
    /**
     * Etiquetas inteligentes para recomendaciones contextuales
     * Ejemplos: "lluvia", "frío", "calor", "oficina", "casa", "deporte"
     * Formato JSON: ["lluvia", "frío", "invierno"]
     */
    @Column(columnDefinition = "TEXT")
    private String etiquetasInteligentes;
    
    /**
     * Categoría del producto
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Categoria categoria;
    
    /**
     * Estado del producto: DISPONIBLE, AGOTADO, DESCONTINUADO
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Estado estado = Estado.DISPONIBLE;
    
    /**
     * Número de veces que el producto ha sido visto
     */
    private Integer conteoVisualizaciones = 0;
    
    /**
     * Número de veces que el producto ha sido comprado
     */
    private Integer conteoCompras = 0;
    
    /**
     * Calificación promedio del producto (1-5)
     */
    @Column
    private Double calificacionPromedio = 0.0;
    
    @Column(nullable = false)
    private Boolean activo = true;

    @Column(nullable = false)
    private Boolean permiteReserva = false;

    @Column(nullable = false)
    private Boolean permitePagoAdelantado = false;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime fechaActualizacion;
    
    /**
     * Comercio al que pertenece el producto
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comercio_id", nullable = false)
    private Comercio comercio;
    
    /**
     * Lista de interacciones con este producto
     */
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Interaccion> interacciones;
    
    /**
     * Enumeración de categorías de productos
     */
    public enum Categoria {
        ROPA,
        ACCESORIOS,
        COMIDA,
        BEBIDA,
        ELECTRONICA,
        HOGAR,
        DEPORTES,
        BELLEZA,
        SALUD,
        LIBROS,
        JUGUETES,
        OTROS
    }
    
    /**
     * Enumeración de estados del producto
     */
    public enum Estado {
        DISPONIBLE,
        AGOTADO,
        DESCONTINUADO
    }
}
