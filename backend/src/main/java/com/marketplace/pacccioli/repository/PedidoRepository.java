package com.marketplace.pacccioli.repository;

import com.marketplace.pacccioli.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuarioIdOrderByFechaCreacionDesc(Long usuarioId);

    @Query("SELECT DISTINCT p FROM Pedido p JOIN p.items i WHERE i.producto.comercio.propietario.id = :comercianteId ORDER BY p.fechaCreacion DESC")
    List<Pedido> findByComercianteId(@Param("comercianteId") Long comercianteId);
}
