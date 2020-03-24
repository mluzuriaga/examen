package ar.com.flexibility;

import ar.com.flexibility.exceptions.CarritoCompraVacioException;
import ar.com.flexibility.exceptions.ElementNotFoundException;
import ar.com.flexibility.model.Cliente;
import ar.com.flexibility.model.Compra;
import ar.com.flexibility.model.Producto;
import ar.com.flexibility.model.dto.ItemDTO;
import ar.com.flexibility.repositories.ClienteRepository;
import ar.com.flexibility.repositories.CompraRepository;
import ar.com.flexibility.repositories.ProductoRepository;
import ar.com.flexibility.services.ClienteService;
import ar.com.flexibility.services.impl.ClienteServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ClienteServiceImplTest {

    @TestConfiguration
    static class ClienteServiceImplTestContextConfiguration {

        @Bean
        public ClienteService clienteService() {
            return new ClienteServiceImpl();
        }
    }

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CompraRepository compraRepository;

    @Test
    public void givenClientWithId_whenFindById_thenReturnClient() throws ElementNotFoundException {

        // Given
        Cliente cliente = new Cliente();
        cliente.setNombre("Matias");

        Cliente clientePersistido = this.clienteRepository.save(cliente);

        // When
        Cliente clienteRetornado = this.clienteService.getCliente(clientePersistido.getId());

        // Then
        Assert.assertEquals(clientePersistido, clienteRetornado);

    }

    @Test(expected = ElementNotFoundException.class)
    public void givenClientWithId_whenFindByDifferentId_thenReturnException() throws ElementNotFoundException {

        // Given
        Cliente cliente = new Cliente();
        cliente.setNombre("Matias");

        Cliente clientePersistido = this.clienteRepository.save(cliente);

        // When
        Long idClienteDiferente = clientePersistido.getId() + 1;

        // Then
        this.clienteService.getCliente(idClienteDiferente);
        // Assert Exception

    }

    @Test
    public void givenClients_whenFindAll_thenReturnClients() {

        // Given
        List<Cliente> listaClientes = new ArrayList<>();

        Cliente clienteMatias = new Cliente();
        clienteMatias.setNombre("Matias");
        this.clienteRepository.save(clienteMatias);
        listaClientes.add(clienteMatias);

        Cliente clienteEnrique = new Cliente();
        clienteEnrique.setNombre("Enrique");
        this.clienteRepository.save(clienteEnrique);
        listaClientes.add(clienteEnrique);

        // When
        List<Cliente> clientes = this.clienteService.getClientes();

        // Then
        Assert.assertEquals(2, clientes.size());
        Assert.assertEquals(listaClientes, clientes);

    }

    @Test
    public void givenNewClient_whenFindFindById_thenReturnNewClient() {

        // Given
        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setNombre("Matias");
        Long idClientePersistido = this.clienteService.nuevoCliente(nuevoCliente);

        // When
        Optional<Cliente> clienteOptional = this.clienteRepository.findById(idClientePersistido);

        // Then
        Assert.assertEquals(nuevoCliente, clienteOptional.get());

    }

    @Test(expected = ElementNotFoundException.class)
    public void givenClient_whenDeleted_thenReturnException() throws ElementNotFoundException {

        // Given
        Cliente clienteMatias = new Cliente();
        clienteMatias.setNombre("Matias");
        Long idClientePersistido = this.clienteService.nuevoCliente(clienteMatias);

        // When
        this.clienteService.eliminarCliente(idClientePersistido);
        this.clienteService.getCliente(idClientePersistido);

        // Then
        // Exception

    }

    @Test
    public void givenClient_whenAddProductos_thenReturnClientWithProducts() throws ElementNotFoundException, CarritoCompraVacioException {

        // Given
        Cliente clienteMatias = new Cliente();
        clienteMatias.setNombre("Matias");
        Long idClientePersistido = this.clienteService.nuevoCliente(clienteMatias);

        Producto producto = new Producto("Pan", 15);
        Producto productoPersistido = this.productoRepository.save(producto);

        List<ItemDTO> listaItems = new ArrayList<>();
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setIdProducto(productoPersistido.getId());
        itemDTO.setCantidad(2);
        listaItems.add(itemDTO);

        // When
        Long idCompra = this.clienteService.addProductsToClient(idClientePersistido, listaItems);
        Optional<Compra> compraOptional = this.compraRepository.findById(idCompra);
        Cliente cliente = this.clienteService.getCliente(idClientePersistido);

        // Then
        Assert.assertTrue(cliente.getCompras().contains(compraOptional.get()));

    }

}
