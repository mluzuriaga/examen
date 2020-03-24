package ar.com.flexibility.services.impl;

import ar.com.flexibility.exceptions.CarritoCompraVacioException;
import ar.com.flexibility.exceptions.ElementNotFoundException;
import ar.com.flexibility.model.Cliente;
import ar.com.flexibility.model.Compra;
import ar.com.flexibility.model.Item;
import ar.com.flexibility.model.Producto;
import ar.com.flexibility.model.dto.ItemDTO;
import ar.com.flexibility.repositories.ClienteRepository;
import ar.com.flexibility.repositories.CompraRepository;
import ar.com.flexibility.repositories.ProductoRepository;
import ar.com.flexibility.services.ClienteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    private Logger logger = LoggerFactory.getLogger(ClienteServiceImpl.class);

    private ClienteRepository clienteRepository;
    private ProductoRepository productoRepository;
    private CompraRepository compraRepository;

    /**
     * Obtiene un cliente por id
     *
     * @param idCliente - id del cliente a consultar
     * @return -
     * @throws ElementNotFoundException -
     */
    @Override
    public Cliente getCliente(Long idCliente) throws ElementNotFoundException {

        Optional<Cliente> clienteOptional = this.clienteRepository.findById(idCliente);
        if (clienteOptional.isPresent()) {
            logger.info("Se consulta informacion del cliente: " + idCliente);
            return clienteOptional.get();
        } else {
            logger.error("Error al consultad informacion del cliente: " + idCliente);
            throw new ElementNotFoundException("El cliente con id: " + idCliente + " no se encuentra.");
        }

    }

    /**
     * Obtengo todos los clientes dados de alta
     *
     * @return -
     */
    @Override
    public List<Cliente> getClientes() {

        logger.info("Consultando todos los clientes.");
        return (List<Cliente>) this.clienteRepository.findAll();

    }

    /**
     * Crea un nuevo cliente
     *
     * @param cliente - cliente a dar de alta
     * @return -
     */
    @Override
    public Long nuevoCliente(Cliente cliente) {

        Cliente clienteSaved = this.clienteRepository.save(cliente);
        logger.info("Nuevo cliente creado con id: " + clienteSaved.getId());
        return clienteSaved.getId();

    }

    /**
     * Elimina un cliente dado de alta
     *
     * @param idCliente - id del cliente a eliminar
     * @throws ElementNotFoundException -
     */
    @Override
    public void eliminarCliente(Long idCliente) throws ElementNotFoundException {

        Cliente cliente = this.getCliente(idCliente);
        this.clienteRepository.delete(cliente);

        logger.info("Cliente con id: " + idCliente + " eliminado.");

    }

    /**
     * Modifica un cliente dado de alta
     *
     * @param idCliente         - id del cliente a modificar
     * @param clienteModificado - datos del a modificar
     * @throws ElementNotFoundException -
     */
    @Override
    public void modificarCliente(Long idCliente, Cliente clienteModificado) throws ElementNotFoundException {

        Cliente cliente = this.getCliente(idCliente);
        cliente.setNombre(clienteModificado.getNombre());
        this.clienteRepository.save(cliente);

        logger.info("Cliente con id: " + idCliente + " modificado.");

    }

    /**
     * Agrega los productos comprados al cliente
     *
     * @param idCliente   - id del cliente que compro
     * @param itemDTOList - lista de items que compro
     * @return -
     * @throws ElementNotFoundException -
     */
    @Override
    public Long addProductsToClient(Long idCliente, List<ItemDTO> itemDTOList) throws ElementNotFoundException, CarritoCompraVacioException {

        if (itemDTOList.size() > 0) {

            // Obtengo el cliente que realizo la compra
            Cliente cliente = this.getCliente(idCliente);

            // Creo una compra
            Compra compra = new Compra();
            // Creo una lista de items para los productos
            List<Item> items = new ArrayList<>();

            for (ItemDTO itemDTO : itemDTOList) {

                // Por cada item busco el producto correspondiente
                Optional<Producto> productoOptional = this.productoRepository.findById(itemDTO.getIdProducto());
                if (productoOptional.isPresent()) {

                    // Creo el item
                    Item item = new Item(itemDTO);
                    item.setProducto(productoOptional.get());
                    items.add(item);

                } else {
                    logger.error("No se pudo realizar la compra. No se encuentran productos.");
                    throw new ElementNotFoundException("El producto con id: " + itemDTO.getIdProducto() + " no se encuentra.");
                }

            }

            // Seteo la lista de items a la compra
            compra.setItems(items);
            // Seteo el id del cliente a la compra
            compra.setIdCliente(idCliente);
            // La compra debe esperar autorizacion
            compra.setApproval(false);

            // Guardo la compra del cliente
            Compra savedCompra = this.compraRepository.save(compra);

            // Agrego la compra al cliente
            cliente.getCompras().add(compra);
            this.clienteRepository.save(cliente);

            logger.info("Compra realizada por el cliente con id: " + idCliente);

            return savedCompra.getId();

        }
        throw new CarritoCompraVacioException("No tiene elementos a comprar.");

    }

    @Autowired
    private void setClienteRepository(ClienteRepository clienteRepository) {

        this.clienteRepository = clienteRepository;

    }

    @Autowired
    private void setProductoRepository(ProductoRepository productoRepository) {

        this.productoRepository = productoRepository;

    }

    @Autowired
    private void setCompraRepository(CompraRepository compraRepository) {

        this.compraRepository = compraRepository;

    }

}
