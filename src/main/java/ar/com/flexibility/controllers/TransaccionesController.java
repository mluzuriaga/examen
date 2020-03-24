package ar.com.flexibility.controllers;

import ar.com.flexibility.model.Compra;
import ar.com.flexibility.services.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("transacciones")
public class TransaccionesController {

    private CompraService compraService;

    /**
     * Obtiene todas las transacciones realizadas
     *
     * @return -
     */
    @GetMapping
    public ResponseEntity<List<Compra>> getTransacciones() {

        List<Compra> compras = this.compraService.getCompras();
        return new ResponseEntity<>(compras, HttpStatus.OK);

    }

    @Autowired
    private void setCompraService(CompraService compraService) {

        this.compraService = compraService;

    }

}
