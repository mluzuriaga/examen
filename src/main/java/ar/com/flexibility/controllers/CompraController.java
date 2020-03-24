package ar.com.flexibility.controllers;

import ar.com.flexibility.exceptions.ElementNotFoundException;
import ar.com.flexibility.services.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("compras")
public class CompraController {

    private CompraService compraService;

    /**
     * Aprueba una compra realizada por un usuario
     *
     * @param idCompra - id de la compra
     * @return -
     */
    @PutMapping("/{idCompra}")
    public ResponseEntity aprobarCompra(@PathVariable("idCompra") Long idCompra) {

        try {

            this.compraService.aprobarCompra(idCompra);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (ElementNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Autowired
    private void setCompraService(CompraService compraService) {

        this.compraService = compraService;

    }

}
