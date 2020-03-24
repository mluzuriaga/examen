package ar.com.flexibility.controllers;

import ar.com.flexibility.exceptions.ElementNotFoundException;
import ar.com.flexibility.model.Producto;
import ar.com.flexibility.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("productos")
public class ProductoController {

    private ProductoService productoService;

    /**
     * Obtiene un producto por id
     *
     * @param idProducto - id del producto buscado
     * @return -
     */
    @GetMapping("/{idProducto}")
    public ResponseEntity getProducto(@PathVariable("idProducto") Long idProducto) {

        try {

            Producto producto = this.productoService.getProducto(idProducto);
            return new ResponseEntity<>(producto, HttpStatus.OK);

        } catch (ElementNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Obtiene todos los productos
     *
     * @return -
     */
    @GetMapping
    public ResponseEntity getProductos() {

        List<Producto> productos = this.productoService.getProductos();
        return new ResponseEntity<>(productos, HttpStatus.OK);

    }

    /**
     * Da de alta un nuevo producto
     *
     * @return -
     */
    @PostMapping
    public ResponseEntity<Long> nuevoProducto(@RequestBody Producto producto) {

        Long productoId = this.productoService.nuevoProducto(producto);
        return new ResponseEntity<>(productoId, HttpStatus.OK);

    }

    /**
     * Elimina un producto
     *
     * @param idProducto - id del producto a eliminar
     * @return -
     */
    @DeleteMapping("/{idProducto}")
    public ResponseEntity eliminarProducto(@PathVariable("idProducto") Long idProducto) {

        try {

            this.productoService.eliminarProducto(idProducto);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (ElementNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Modifica un producto dado de alta
     *
     * @param idProducto - id del producto a modificar
     * @param producto   - producto modificado
     * @return -
     */
    @PutMapping("/{idProducto}")
    public ResponseEntity modificarProducto(@PathVariable("idProducto") Long idProducto, @RequestBody Producto producto) {

        try {

            this.productoService.modificarProducto(idProducto, producto);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (ElementNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Autowired
    private void setProductosService(ProductoService productosService) {

        this.productoService = productosService;

    }

}
