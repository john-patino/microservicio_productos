package com.unir.microservicio.productos.microsevicio_productos.controllers;

import com.unir.microservicio.productos.microsevicio_productos.entities.Categoria;
import com.unir.microservicio.productos.microsevicio_productos.entities.Producto;
import com.unir.microservicio.productos.microsevicio_productos.services.ICategoriaService;
import com.unir.microservicio.productos.microsevicio_productos.services.IProductoService;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class ProductoController {

    @Autowired
    private IProductoService productoService;
    private ICategoriaService categoriaService;

    @GetMapping("/productos")
    public List<Producto> ListarProductos() {
        return productoService.findAll();
    }

    @GetMapping("/productos/page/{page}")
    public Page<Producto> index(@PathVariable Integer page) {
        Pageable pageable = PageRequest.of(page, 4);
        return productoService.findAll(pageable);
    }

    @GetMapping("/producto/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> show(@PathVariable Integer id) {

        Map<String, Object> response = new HashMap<>();
        Producto producto = null;
        try {
            producto = productoService.findById(id);
        } catch (DataAccessException ex) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", ex.getMessage().concat(": ").concat(ex.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        if (producto == null) {
            response.put("mensaje", "El Producto ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Producto>(producto, HttpStatus.OK);
    }


    @PostMapping("/producto")
    public ResponseEntity<?> create(@Valid @RequestBody Producto producto, BindingResult result) {

        Producto nuevoProducto = null;
        Map<String, Object> response = new HashMap<>();

        if(result.hasErrors()) {
            List<String> errors = new ArrayList<>();

            for(FieldError error: result.getFieldErrors()) {
                errors.add("El campo " + error.getField() + " " + error.getDefaultMessage());
            }
            response.put("errors", errors);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            // Establecer la fecha de creación y modificación
            producto.setFechaCreacion(LocalDateTime.now());
            producto.setFechaModificacion(LocalDateTime.now());
            // Guardar el producto
            nuevoProducto = productoService.save(producto);
        } catch (DataAccessException ex) {
            response.put("mensaje", "Error al insertar el registro en la base de datos");
            response.put("error", ex.getMessage().concat(": ").concat(ex.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "El producto ha sido creado con éxito!");
        response.put("producto", nuevoProducto);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }



    @PutMapping("/producto/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Producto producto, BindingResult result, @PathVariable Integer id) {

        Map<String, Object> response = new HashMap<>();
        Producto productoActual = productoService.findById(id);
        Producto productoActualizado = null;

        if(result.hasErrors()) {
            List<String> errors = new ArrayList<>();

            for(FieldError error: result.getFieldErrors()) {
                errors.add("El campo"+ " " + error.getField() + " " + error.getDefaultMessage());
            }
            response.put("errors", errors);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (productoActual == null) {
            response.put("mensaje", "Error, no se pudo editar el horario ID: "
                    .concat(id.toString().concat(" no existe en la base de datos!")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {
            productoActual.setCodigo(producto.getCodigo());
            productoActual.setNombre(producto.getNombre());
            productoActual.setDescripcion(producto.getDescripcion());
            productoActual.setPrecio(producto.getPrecio());
            productoActual.setFechaModificacion(LocalDateTime.now());
            productoActual.setCategoria(producto.getCategoria());
            productoActualizado = productoService.save(productoActual);

        } catch (DataAccessException ex) {
            response.put("mensaje", "Error al actualizar el registro en la base de datos");
            response.put("error", ex.getMessage().concat(": ").concat(ex.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El producto ha sido actualizado con exito!");
        response.put("categoria", productoActualizado);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }


    @DeleteMapping("producto/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Producto producto = productoService.findById(id);
            productoService.delete(id);
        } catch (DataAccessException ex) {
            response.put("mensaje", "Error al eliminar el registro en la base de datos");
            response.put("error", ex.getMessage().concat(": ").concat(ex.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "El producto ha sido eliminado con exito!");
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    //johnp
    @GetMapping("/productos/search")
    public List<Producto> searchProductos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String categoriaNombre,
            @RequestParam(required = false) Double precioMin,
            @RequestParam(required = false) Double precioMax,
            @RequestParam(required = false) Boolean disponible) {
        return productoService.searchProductos(nombre, categoriaNombre, precioMin, precioMax, disponible);
    }
}
