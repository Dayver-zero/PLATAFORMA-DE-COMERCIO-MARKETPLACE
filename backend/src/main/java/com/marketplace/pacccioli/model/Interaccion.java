package com.marketplace.pacccioli.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad JPA que representa una interacción del usuario con un producto.
 * Registra clics, visualizaciones y compras para el historial de comportamiento.
 */
@Entity
@Table(name = "interacciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Interaccion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Tipo de interacción: VISUALIZACION, CLICK, COMPRA
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoInteraccion tipoInteraccion;
    
    /**
     * Usuario que realizó la interacción
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    /**
     * Producto con el que se interactuó
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
    
    /**
     * Cantidad de productos en caso de compra
     */
    private Integer cantidad = 1;
    
    /**
     * Precio al momento de la interacción
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal precioMomento;
    
    /**
     * Contexto del clima en el momento de la interacción
     * Formato JSON: {"temperatura": 15, "condicion": "lluvia", "humedad": 80}
     */
    @Column(columnDefinition = "TEXT")
    private String contextoClima;
    
    /**
     * Ubicación del usuario en el momento de la interacción
     */
    @Column
    private Double latitudUsuario;
    
    @Column
    private Double longitudUsuario;
    
    /**
     * Fuente de la interacción: BUSQUEDA, RECOMENDACION, DIRECTO
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private FuenteInteraccion fuente;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaInteraccion;
    
    /**
     * Enumeración de tipos de interacción
     */
    public enum TipoInteraccion {
        VISUALIZACION,
        CLICK,
        COMPRA,
        FAVORITO
    }
    
    /**
     * Enumeración de fuentes de interacción
     */
    public enum FuenteInteraccion {
        BUSQUEDA,
        RECOMENDACION,
        DIRECTO,
        PROMOCION,
        FEED
    }
}
