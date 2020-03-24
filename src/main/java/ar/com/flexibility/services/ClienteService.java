package ar.com.flexibility.services;

import ar.com.flexibility.exceptions.CarritoCompraVacioException;
import ar.com.flexibility.exceptions.ElementNotFoundException;
import ar.com.flexibility.model.Cliente;
import ar.com.flexibility.model.dto.ItemDTO;

import java.util.List;

public interface ClienteService {

    Cliente getCliente(Long idCliente) throws ElementNotFoundException;

    List<Cliente> getClientes();

    Long nuevoCliente(Cliente producto);

    void eliminarCliente(Long idCliente) throws ElementNotFoundException;

    void modificarCliente(Long idCliente, Cliente cliente) throws ElementNotFoundException;

    Long addProductsToClient(Long idCliente, List<ItemDTO> itemDTOList) throws ElementNotFoundException, CarritoCompraVacioException;

}
