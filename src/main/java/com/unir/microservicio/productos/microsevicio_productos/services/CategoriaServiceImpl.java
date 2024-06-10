package com.unir.microservicio.productos.microsevicio_productos.services;

import com.unir.microservicio.productos.microsevicio_productos.dao.ICategoriaDAO;
import com.unir.microservicio.productos.microsevicio_productos.entities.Categoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class CategoriaServiceImpl implements ICategoriaService {

    @Autowired
    private ICategoriaDAO categoriaDAO;
    @Override
    @Transactional(readOnly = true)
    public List<Categoria> findAll() {
        return (List<Categoria>) categoriaDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Categoria> findAll(Pageable pageable) {
        return categoriaDAO.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Categoria findById(Integer id) {
        return categoriaDAO.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Categoria save(Categoria categoria) {
        return categoriaDAO.save(categoria);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        categoriaDAO.deleteById(id);
    }
}
