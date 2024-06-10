package com.unir.microservicio.productos.microsevicio_productos.dao;

import com.unir.microservicio.productos.microsevicio_productos.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface IProductoDAO extends JpaRepository<Producto, Integer> {
    // Buscar productos por nombre
    List<Producto> findByNombreContaining(String nombre);
    
    // Buscar productos por categoría
    List<Producto> findByCategoriaNombre(String categoriaNombre);
    
    // Buscar productos por rango de precio
    List<Producto> findByPrecioBetween(Double precioMin, Double precioMax);

    // Buscar productos por disponibilidad
    List<Producto> findByDisponible(Boolean disponible);

    // Buscar productos por varias opciones usando @Query
    @Query("SELECT p FROM Producto p WHERE " +
           "(:nombre IS NULL OR p.nombre LIKE %:nombre%) AND " +
           "(:categoriaNombre IS NULL OR p.categoria.nombre = :categoriaNombre) AND " +
           "(:precioMin IS NULL OR p.precio >= :precioMin) AND " +
           "(:precioMax IS NULL OR p.precio <= :precioMax) AND " +
           "(:disponible IS NULL OR p.disponible = :disponible)")
    List<Producto> searchProductos(@Param("nombre") String nombre,
                                   @Param("categoriaNombre") String categoriaNombre,
                                   @Param("precioMin") Double precioMin,
                                   @Param("precioMax") Double precioMax,
                                   @Param("disponible") Boolean disponible);
}
