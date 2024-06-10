package com.unir.microservicio.productos.microsevicio_productos.services;

import com.unir.microservicio.productos.microsevicio_productos.entities.Categoria;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface ICategoriaService {

    public List<Categoria> findAll();

    public Page<Categoria> findAll(Pageable pageable);

    public Categoria findById(Integer id);

    public Categoria save(Categoria categoria);

    public void delete(Integer id);
}
