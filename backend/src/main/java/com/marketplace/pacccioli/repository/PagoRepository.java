package com.marketplace.pacccioli.repository;

import com.marketplace.pacccioli.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findByPedidoIdOrderByFechaCreacionDesc(Long pedidoId);
}
