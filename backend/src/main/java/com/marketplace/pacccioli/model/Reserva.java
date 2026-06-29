package com.marketplace.pacccioli.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

    public enum EstadoReserva {
        PENDIENTE, CONFIRMADA, CANCELADA, COMPLETADA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comercio_id", nullable = false)
    private Comercio comercio;

    @Column(nullable = false)
    private Integer cantidad = 1;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoReserva estado = EstadoReserva.PENDIENTE;

    @Column(nullable = false)
    private LocalDateTime fechaExpiracion;

    @Column(columnDefinition = "TEXT")
    private String notas;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaReserva;
}
