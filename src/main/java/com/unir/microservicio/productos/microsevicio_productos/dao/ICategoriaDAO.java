package com.unir.microservicio.productos.microsevicio_productos.dao;

import com.unir.microservicio.productos.microsevicio_productos.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICategoriaDAO extends JpaRepository<Categoria, Integer> {
}
