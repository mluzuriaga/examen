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
import ar.com.flexibility.services.CompraService;
import ar.com.flexibility.services.impl.ClienteServiceImpl;
import ar.com.flexibility.services.impl.CompraServiceImpl;
import org.junit.Assert;
import org.junit.Before;
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
public class CompraServiceImplTest {

    @TestConfiguration
    static class CompraServiceImplTestContextConfiguration {

        @Bean
        public CompraService compraService() {
            return new CompraServiceImpl();
        }
    }

    @Autowired
    private CompraService compraService;

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

    private Long idCliente;
    private Long idCompra;

    @Before
    public void setUp() throws ElementNotFoundException, CarritoCompraVacioException {

        Cliente clienteMatias = new Cliente();
        clienteMatias.setNombre("Matias");
        Cliente cliente = this.clienteRepository.save(clienteMatias);
        this.idCliente = cliente.getId();

        Producto producto = new Producto("Pan", 15);
        Producto productoPersistido = this.productoRepository.save(producto);

        List<ItemDTO> listaItems = new ArrayList<>();
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setIdProducto(productoPersistido.getId());
        itemDTO.setCantidad(2);
        listaItems.add(itemDTO);

        this.idCompra = this.clienteService.addProductsToClient(cliente.getId(), listaItems);

    }

    @Test
    public void givenClientesConCompras_whenFindAll_thenReturnCompras() {

        // Given
        // Clientes con compras

        // When
        List<Compra> compras = this.compraService.getCompras();

        // Then
        Assert.assertEquals(1, compras.size());

    }

    @Test
    public void givenCompraNoAprobada_whenAprobarCompra_thenReturnCompraAprobada() throws ElementNotFoundException {

        // Given
        Long idDeCompra = this.idCompra;

        // When
        this.compraService.aprobarCompra(idDeCompra);

        // Then
        Cliente cliente = this.clienteService.getCliente(this.idCliente);
        Assert.assertTrue(cliente.getCompras().get(0).isApproval());

    }

}
