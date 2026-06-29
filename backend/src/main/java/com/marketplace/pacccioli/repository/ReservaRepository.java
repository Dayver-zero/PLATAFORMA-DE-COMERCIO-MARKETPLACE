package com.marketplace.pacccioli.repository;

import com.marketplace.pacccioli.model.Reserva;
import com.marketplace.pacccioli.model.Reserva.EstadoReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByUsuarioIdOrderByFechaReservaDesc(Long usuarioId);
    List<Reserva> findByComercioIdOrderByFechaReservaDesc(Long comercioId);
    List<Reserva> findByProductoIdAndEstado(Long productoId, EstadoReserva estado);
    long countByProductoIdAndEstado(Long productoId, EstadoReserva estado);
}
