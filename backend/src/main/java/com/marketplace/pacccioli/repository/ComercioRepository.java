package com.marketplace.pacccioli.repository;

import com.marketplace.pacccioli.model.Comercio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComercioRepository extends JpaRepository<Comercio, Long> {
    
    // Búsqueda por nombre
    List<Comercio> findByNombreContainingIgnoreCase(String nombre);
    
    // Comercios activos
    List<Comercio> findByActivoTrue();
    
    // Comercios por propietario
    List<Comercio> findByPropietarioId(Long propietarioId);
    
    // Comercios por categoría
    List<Comercio> findByCategoriaAndActivoTrue(Comercio.Categoria categoria);
    
    // Búsqueda por ubicación (Haversine) - Retorna comercios dentro de un radio
    @Query(value = "SELECT c.* FROM comercio c WHERE " +
           "( 6371 * acos( cos( radians(:latitud) ) * cos( radians( c.latitud ) ) * " +
           "cos( radians( c.longitud ) - radians(:longitud) ) + " +
           "sin( radians(:latitud) ) * sin( radians( c.latitud ) ) ) ) <= :radioKm AND " +
           "c.activo = true", 
           nativeQuery = true)
    List<Comercio> buscarComerciosCercanos(
            @Param("latitud") Double latitud,
            @Param("longitud") Double longitud,
            @Param("radioKm") Double radioKm
    );
    
    // Comercios mejor calificados
    List<Comercio> findByActivoTrueOrderByCalificacionDesc();
    
    // Búsqueda combinada: nombre + ubicación
    @Query(value = "SELECT c.* FROM comercio c WHERE " +
           "LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) AND " +
           "( 6371 * acos( cos( radians(:latitud) ) * cos( radians( c.latitud ) ) * " +
           "cos( radians( c.longitud ) - radians(:longitud) ) + " +
           "sin( radians(:latitud) ) * sin( radians( c.latitud ) ) ) ) <= :radioKm AND " +
           "c.activo = true",
           nativeQuery = true)
    List<Comercio> buscarComerciosAvanzado(
            @Param("nombre") String nombre,
            @Param("latitud") Double latitud,
            @Param("longitud") Double longitud,
            @Param("radioKm") Double radioKm
    );
}
