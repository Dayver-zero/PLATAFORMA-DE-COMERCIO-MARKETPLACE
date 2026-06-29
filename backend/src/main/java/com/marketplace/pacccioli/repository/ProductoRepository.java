package com.marketplace.pacccioli.repository;

import com.marketplace.pacccioli.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    // Búsqueda por nombre (LIKE)
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    
    // Productos de un comercio específico
    List<Producto> findByComercioId(Long comercioId);
    
    // Productos activos de un comercio
    List<Producto> findByComercioIdAndActivoTrue(Long comercioId);
    
    // Búsqueda por etiqueta inteligente
    @Query(value = "SELECT * FROM productos p WHERE JSON_CONTAINS(p.etiquetas_inteligentes, :etiqueta) = 1 AND p.activo = true", nativeQuery = true)
    List<Producto> findByEtiqueta(@Param("etiqueta") String etiqueta);
    
    // Productos con stock disponible
    List<Producto> findByStockGreaterThanAndActivoTrue(Integer stock);
    
    // Búsqueda avanzada: nombre, comercio, con etiqueta y stock
    @Query("SELECT p FROM Producto p WHERE " +
           "LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) AND " +
           "p.comercio.id = :comercioId AND " +
           "p.stock > 0 AND " +
           "p.activo = true")
    List<Producto> buscarProductos(
            @Param("nombre") String nombre,
            @Param("comercioId") Long comercioId
    );
    
    // Productos más vistos
    List<Producto> findByActivoTrueOrderByConteoVisualizacionesDesc();
    
    // Productos mejor calificados
    List<Producto> findByActivoTrueOrderByCalificacionPromedioDesc();
}
