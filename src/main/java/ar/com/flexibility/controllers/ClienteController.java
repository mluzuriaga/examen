package ar.com.flexibility.controllers;

import ar.com.flexibility.exceptions.CarritoCompraVacioException;
import ar.com.flexibility.exceptions.ElementNotFoundException;
import ar.com.flexibility.exceptions.OutOfStockException;
import ar.com.flexibility.model.Cliente;
import ar.com.flexibility.model.dto.ItemDTO;
import ar.com.flexibility.services.ClienteService;
import ar.com.flexibility.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("clientes")
public class ClienteController {

    private ClienteService clienteService;
    private ProductoService productoService;

    /**
     * Obtiene un cliente por id
     *
     * @param idCliente - id del cliente buscado
     * @return -
     */
    @GetMapping("/{idCliente}")
    public ResponseEntity getCliente(@PathVariable("idCliente") Long idCliente) {

        try {

            Cliente cliente = this.clienteService.getCliente(idCliente);
            return new ResponseEntity<>(cliente, HttpStatus.OK);

        } catch (ElementNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Obtiene todos los clientes
     *
     * @return -
     */
    @GetMapping
    public ResponseEntity getClientes() {

        List<Cliente> clientes = this.clienteService.getClientes();
        return new ResponseEntity<>(clientes, HttpStatus.OK);

    }

    /**
     * Da de alta un nuevo cliente
     *
     * @return -
     */
    @PostMapping
    public ResponseEntity<Long> nuevoCliente(@RequestBody Cliente cliente) {

        Long clienteId = this.clienteService.nuevoCliente(cliente);
        return new ResponseEntity<>(clienteId, HttpStatus.OK);

    }

    /**
     * Elimina un cliente
     *
     * @param idCliente - id del cliente a eliminar
     * @return -
     */
    @DeleteMapping("/{idCliente}")
    public ResponseEntity eliminarCliente(@PathVariable("idCliente") Long idCliente) {

        try {

            this.clienteService.eliminarCliente(idCliente);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (ElementNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Modifica un cliente dado de alta
     *
     * @param idCliente - id del cliente a modificar
     * @param cliente   - cliente modificado
     * @return -
     */
    @PutMapping("/{idCliente}")
    public ResponseEntity modificarCliente(@PathVariable("idCliente") Long idCliente, @RequestBody Cliente cliente) {

        try {

            this.clienteService.modificarCliente(idCliente, cliente);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (ElementNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Compra de productos
     *
     * @param idCliente   - id del cliente que efectua la compra
     * @param itemDTOList - lista de productos a comprar
     * @return -
     */
    @PostMapping("/{idCliente}/compra")
    public ResponseEntity comprarProductos(@PathVariable("idCliente") Long idCliente, @RequestBody List<ItemDTO> itemDTOList) {

        try {

            this.productoService.validarDisponibilidad(itemDTOList);
            this.productoService.modifyStock(itemDTOList);
            Long idCompra = this.clienteService.addProductsToClient(idCliente, itemDTOList);

            return new ResponseEntity<>(idCompra, HttpStatus.OK);

        } catch (ElementNotFoundException | OutOfStockException | CarritoCompraVacioException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Autowired
    private void setClienteService(ClienteService clienteService) {

        this.clienteService = clienteService;

    }

    @Autowired
    private void setProductoService(ProductoService productoService) {

        this.productoService = productoService;

    }

}
