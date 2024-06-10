package com.unir.microservicio.productos.microsevicio_productos.services;

import com.unir.microservicio.productos.microsevicio_productos.dao.IProductoDAO;
import com.unir.microservicio.productos.microsevicio_productos.entities.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoServiceImpl implements IProductoService{

    @Autowired
    private IProductoDAO productoDAO;

    @Override
    @Transactional(readOnly = true)
    public List<Producto> findAll() {
        return (List<Producto>) productoDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Producto> findAll(Pageable pageable) {
        return productoDAO.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Producto findById(Integer id) {
        return productoDAO.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Producto save(Producto producto) {
        return productoDAO.save(producto);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        productoDAO.deleteById(id);
    }

    //johnp
    @Override
    public List<Producto> findByNombreContaining(String nombre) {
        return productoDAO.findByNombreContaining(nombre);
    }

    @Override
    public List<Producto> findByCategoriaNombre(String categoriaNombre) {
        return productoDAO.findByCategoriaNombre(categoriaNombre);
    }

    @Override
    public List<Producto> findByPrecioBetween(Double precioMin, Double precioMax) {
        return productoDAO.findByPrecioBetween(precioMin, precioMax);
    }

    @Override
    public List<Producto> findByDisponible(Boolean disponible) {
        return productoDAO.findByDisponible(disponible);
    }

    @Override
    public List<Producto> searchProductos(String nombre, String categoriaNombre, Double precioMin, Double precioMax, Boolean disponible) {
        return productoDAO.searchProductos(nombre, categoriaNombre, precioMin, precioMax, disponible);
    }
}
