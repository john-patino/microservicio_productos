package com.unir.microservicio.productos.microsevicio_productos.controllers;

import com.unir.microservicio.productos.microsevicio_productos.entities.Categoria;
import com.unir.microservicio.productos.microsevicio_productos.services.ICategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class CategoriaController {

    @Autowired
    private ICategoriaService categoriaService;

    @GetMapping("/categorias")
    public List<Categoria> ListarCategorias() {
        return categoriaService.findAll();
    }

    @GetMapping("/categorias/page/{page}")
    public Page<Categoria> index(@PathVariable Integer page) {
        Pageable pageable = PageRequest.of(page, 4);
        return categoriaService.findAll(pageable);
    }

    @GetMapping("/categorias/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> show(@PathVariable Integer id) {

        Map<String, Object> response = new HashMap<>();
        Categoria categoria = null;
        try {
            categoria = categoriaService.findById(id);
        } catch (DataAccessException ex) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", ex.getMessage().concat(": ").concat(ex.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        if (categoria == null) {
            response.put("mensaje", "La Categoria ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Categoria>(categoria, HttpStatus.OK);
    }


    @PostMapping("/categoria")
    public ResponseEntity<?> create(@Valid @RequestBody Categoria categoria, BindingResult result) {

        Categoria nuevaCategoria = null;
        Map<String, Object> response = new HashMap<>();

        if(result.hasErrors()) {
            List<String> errors = new ArrayList<>();

            for(FieldError error: result.getFieldErrors()) {
                errors.add("El campo"+ " " + error.getField() + " " + error.getDefaultMessage());
            }
            response.put("errors", errors);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            nuevaCategoria = categoriaService.save(categoria);
        } catch (DataAccessException ex) {
            response.put("mensaje", "Error al insertar el registro en la base de datos");
            response.put("error", ex.getMessage().concat(": ").concat(ex.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "La categoria ha sido creada con exito!");
        response.put("categoria", nuevaCategoria);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }


    @PutMapping("/categoria/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Categoria categoria, BindingResult result, @PathVariable Integer id) {

        Map<String, Object> response = new HashMap<>();
        Categoria categoriaActual = categoriaService.findById(id);
        Categoria categoriaActualizada = null;

        if(result.hasErrors()) {
            List<String> errors = new ArrayList<>();

            for(FieldError error: result.getFieldErrors()) {
                errors.add("El campo"+ " " + error.getField() + " " + error.getDefaultMessage());
            }
            response.put("errors", errors);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (categoriaActual == null) {
            response.put("mensaje", "Error, no se pudo editar el horario ID: "
                    .concat(id.toString().concat(" no existe en la base de datos!")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {
            categoriaActual.setNombre(categoria.getNombre());
            categoriaActual.setDescripcion(categoria.getDescripcion());
            categoriaActualizada = categoriaService.save(categoriaActual);

        } catch (DataAccessException ex) {
            response.put("mensaje", "Error al actualizar el registro en la base de datos");
            response.put("error", ex.getMessage().concat(": ").concat(ex.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "La categoria ha sido actualizada con exito!");
        response.put("categoria", categoriaActualizada);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }


    @DeleteMapping("categoria/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Categoria categoria = categoriaService.findById(id);
            categoriaService.delete(id);
        } catch (DataAccessException ex) {
            response.put("mensaje", "Error al eliminar el registro en la base de datos");
            response.put("error", ex.getMessage().concat(": ").concat(ex.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "La categoría ha sido eliminada con exito!");
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
}
