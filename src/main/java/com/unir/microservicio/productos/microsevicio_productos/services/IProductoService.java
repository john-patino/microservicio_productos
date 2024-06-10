package com.unir.microservicio.productos.microsevicio_productos.services;

import com.unir.microservicio.productos.microsevicio_productos.entities.Categoria;
import com.unir.microservicio.productos.microsevicio_productos.entities.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductoService {

    public List<Producto> findAll();

    public Page<Producto> findAll(Pageable pageable);

    public Producto findById(Integer id);

    public Producto save(Producto producto);

    public void delete(Integer id);

    //johnp
    List<Producto> findByNombreContaining(String nombre);

    List<Producto> findByCategoriaNombre(String categoriaNombre);

    List<Producto> findByPrecioBetween(Double precioMin, Double precioMax);

    List<Producto> findByDisponible(Boolean disponible);

    List<Producto> searchProductos(String nombre, String categoriaNombre, Double precioMin, Double precioMax, Boolean disponible);
}
