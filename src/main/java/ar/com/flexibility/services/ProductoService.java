package ar.com.flexibility.services;

import ar.com.flexibility.exceptions.ElementNotFoundException;
import ar.com.flexibility.exceptions.OutOfStockException;
import ar.com.flexibility.model.Producto;
import ar.com.flexibility.model.dto.ItemDTO;

import java.util.List;

public interface ProductoService {

    Producto getProducto(Long idProducto) throws ElementNotFoundException;

    List<Producto> getProductos();

    Long nuevoProducto(Producto producto);

    void eliminarProducto(Long idProducto) throws ElementNotFoundException;

    void modificarProducto(Long idProducto, Producto producto) throws ElementNotFoundException;

    void validarDisponibilidad(List<ItemDTO> itemDTOList) throws ElementNotFoundException, OutOfStockException;

    void modifyStock(List<ItemDTO> itemDTOList) throws ElementNotFoundException;

}
