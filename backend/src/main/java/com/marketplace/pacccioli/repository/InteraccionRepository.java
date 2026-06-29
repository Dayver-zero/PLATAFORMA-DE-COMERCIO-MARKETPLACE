package com.marketplace.pacccioli.repository;

import com.marketplace.pacccioli.model.Interaccion;
import com.marketplace.pacccioli.model.Interaccion.FuenteInteraccion;
import com.marketplace.pacccioli.model.Interaccion.TipoInteraccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InteraccionRepository extends JpaRepository<Interaccion, Long> {
    
    // Interacciones de un usuario
    List<Interaccion> findByUsuarioId(Long usuarioId);
    
    // Interacciones de un producto
    List<Interaccion> findByProductoId(Long productoId);
    
    // Interacciones por tipo
    List<Interaccion> findByTipoInteraccion(TipoInteraccion tipoInteraccion);
    
    // Interacciones por fuente
    List<Interaccion> findByFuente(FuenteInteraccion fuente);
    
    // Interacciones de un usuario en un período
    List<Interaccion> findByUsuarioIdAndFechaInteraccionGreaterThanEqual(Long usuarioId, LocalDateTime fechaInteraccion);
    
    // Productos más interactuados por un usuario
    @Query("SELECT i.producto.id, COUNT(i) as conteo FROM Interaccion i " +
           "WHERE i.usuario.id = :usuarioId " +
           "GROUP BY i.producto.id " +
           "ORDER BY conteo DESC")
    List<Object[]> productosInteractuadosPorUsuario(@Param("usuarioId") Long usuarioId);
    
    // Historial de compras de un usuario
    List<Interaccion> findByUsuarioIdAndTipoInteraccion(Long usuarioId, TipoInteraccion tipoInteraccion);
    
    // Contar visualizaciones de un producto
    Long countByProductoIdAndTipoInteraccion(Long productoId, TipoInteraccion tipoInteraccion);
}
