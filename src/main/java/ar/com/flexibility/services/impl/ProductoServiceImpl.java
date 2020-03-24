package ar.com.flexibility.services.impl;

import ar.com.flexibility.exceptions.ElementNotFoundException;
import ar.com.flexibility.exceptions.OutOfStockException;
import ar.com.flexibility.model.Producto;
import ar.com.flexibility.model.dto.ItemDTO;
import ar.com.flexibility.repositories.ProductoRepository;
import ar.com.flexibility.services.ProductoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoService {

    private Logger logger = LoggerFactory.getLogger(ProductoServiceImpl.class);

    private ProductoRepository productoRepository;

    /**
     * Obtengo un producto
     *
     * @param idProducto - id del producto a consultar
     * @return -
     * @throws ElementNotFoundException -
     */
    @Override
    public Producto getProducto(Long idProducto) throws ElementNotFoundException {

        Optional<Producto> productoOptional = this.productoRepository.findById(idProducto);
        if (productoOptional.isPresent()) {
            logger.info("Se consulta el producto con id: " + idProducto);
            return productoOptional.get();
        } else {
            logger.error("No se pudo consultar el producto. No se encuentra el producto con id: " + idProducto);
            throw new ElementNotFoundException("El producto con id: " + idProducto + " no se encuentra.");
        }

    }

    /**
     * Obtengo todos los productos
     *
     * @return -
     */
    @Override
    public List<Producto> getProductos() {

        logger.info("Obteniendo la lista de productos.");
        return (List<Producto>) this.productoRepository.findAll();

    }

    /**
     * Crea un nuevo producto
     *
     * @param producto - producto a dar de alta
     * @return -
     */
    @Override
    public Long nuevoProducto(Producto producto) {

        Producto productoSaved = this.productoRepository.save(producto);
        logger.info("Se crea nuevo producto.");
        return productoSaved.getId();

    }

    /**
     * Elimina un producto existente
     *
     * @param idProducto - id del producto a eliminar
     * @throws ElementNotFoundException -
     */
    @Override
    public void eliminarProducto(Long idProducto) throws ElementNotFoundException {

        Optional<Producto> productoOptional = this.productoRepository.findById(idProducto);
        if (productoOptional.isPresent()) {
            this.productoRepository.delete(productoOptional.get());
            logger.info("Se elimina el producto con id: " + idProducto);
        } else {
            logger.error("No se puede eliminar el producto. No existe el producto con id: " + idProducto);
            throw new ElementNotFoundException("El producto con id: " + idProducto + " no se encuentra.");
        }

    }

    /**
     * Modifica un producto dado de alta
     *
     * @param idProducto         - id del producto a modificar
     * @param productoModificado - datos del producto modificado
     * @throws ElementNotFoundException -
     */
    @Override
    public void modificarProducto(Long idProducto, Producto productoModificado) throws ElementNotFoundException {

        Optional<Producto> productoOptional = this.productoRepository.findById(idProducto);
        if (productoOptional.isPresent()) {

            Producto producto = productoOptional.get();
            producto.setNombre(productoModificado.getNombre());
            producto.setStock(productoModificado.getStock());

            this.productoRepository.save(producto);

            logger.info("Se modifica el producto con id: " + idProducto);

        } else {
            logger.info("No se puede modificar el producto. No existe el producto con id: " + idProducto);
            throw new ElementNotFoundException("El producto con id: " + idProducto + " no se encuentra.");
        }

    }

    /**
     * Valida la disponibilidad de los productos que un cliente quiere comprar
     *
     * @param itemDTOList - lista de productos a comprar
     * @throws ElementNotFoundException -
     * @throws OutOfStockException      -
     */
    @Override
    public void validarDisponibilidad(List<ItemDTO> itemDTOList) throws ElementNotFoundException, OutOfStockException {

        logger.info("Validando la disponibilidad de productos.");

        StringBuilder productosSinStock = new StringBuilder();

        for (ItemDTO itemDTO : itemDTOList) {

            Integer cantidadAComprar = itemDTO.getCantidad();

            boolean validacion = this.validarDisponibilidadProducto(itemDTO.getIdProducto(), cantidadAComprar);
            if (!validacion)
                productosSinStock.append("No hay ").append(cantidadAComprar).append(" del producto con id: ").append(itemDTO.getIdProducto()).append("\n");

        }

        if (productosSinStock.length() > 0) {
            logger.error("Fallo la validacion de productos.");
            throw new OutOfStockException(productosSinStock.toString());
        }

        logger.info("Se valido la disponibilidad de productos.");

    }

    /**
     * Modifica el stock de los productos a comprar
     *
     * @param itemDTOList - productos a comprar
     * @throws ElementNotFoundException -
     */
    @Override
    public void modifyStock(List<ItemDTO> itemDTOList) throws ElementNotFoundException {

        for (ItemDTO itemDTO : itemDTOList) {

            Integer cantidadAComprar = itemDTO.getCantidad();

            boolean validacion = this.validarDisponibilidadProducto(itemDTO.getIdProducto(), cantidadAComprar);
            if (validacion) {

                Producto producto = this.getProducto(itemDTO.getIdProducto());
                producto.setStock(producto.getStock() - cantidadAComprar);

                this.productoRepository.save(producto);

                logger.info("Se modifico la cantidad de productos de: " + producto.getNombre() + " del stock.");

            }

        }

    }

    /**
     * Valida el stock de un producto
     *
     * @param idProducto - id del producto a validar
     * @param cantidad   - cantidad que se quiere comprar
     * @return -
     * @throws ElementNotFoundException -
     */
    private boolean validarDisponibilidadProducto(Long idProducto, Integer cantidad) throws ElementNotFoundException {

        Producto producto = this.getProducto(idProducto);
        return producto.getStock() >= cantidad;

    }

    @Autowired
    private void setProductoRepository(ProductoRepository productoRepository) {

        this.productoRepository = productoRepository;

    }

}
