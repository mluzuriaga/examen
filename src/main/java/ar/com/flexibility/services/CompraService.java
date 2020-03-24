package ar.com.flexibility.services;

import ar.com.flexibility.exceptions.ElementNotFoundException;
import ar.com.flexibility.model.Compra;

import java.util.List;

public interface CompraService {

    List<Compra> getCompras();

    void aprobarCompra(Long idCompra) throws ElementNotFoundException;

}
