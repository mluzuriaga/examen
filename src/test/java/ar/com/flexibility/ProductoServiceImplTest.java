package ar.com.flexibility;

import ar.com.flexibility.exceptions.ElementNotFoundException;
import ar.com.flexibility.exceptions.OutOfStockException;
import ar.com.flexibility.model.Producto;
import ar.com.flexibility.model.dto.ItemDTO;
import ar.com.flexibility.repositories.ProductoRepository;
import ar.com.flexibility.services.ProductoService;
import ar.com.flexibility.services.impl.ProductoServiceImpl;
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
public class ProductoServiceImplTest {

    @TestConfiguration
    static class ProductoServiceImplTestContextConfiguration {

        @Bean
        public ProductoService productoService() {
            return new ProductoServiceImpl();
        }
    }

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoRepository productoRepository;

    @Test
    public void givenProductWithId_whenFindById_thenReturnProduct() throws ElementNotFoundException {

        // Given
        Producto producto = new Producto();
        producto.setNombre("Pan");
        producto.setStock(10);

        Producto productoPersistido = this.productoRepository.save(producto);

        // When
        Producto productoRetornado = this.productoService.getProducto(productoPersistido.getId());

        // Then
        Assert.assertEquals(productoPersistido, productoRetornado);

    }

    @Test(expected = ElementNotFoundException.class)
    public void givenProductoWithId_whenFindByDifferentId_thenReturnException() throws ElementNotFoundException {

        // Given
        Producto producto = new Producto();
        producto.setNombre("Pan");
        producto.setStock(10);

        Producto productoPersistido = this.productoRepository.save(producto);

        // When
        Long idProductoDiferente = productoPersistido.getId() + 1;

        // Then
        this.productoService.getProducto(idProductoDiferente);
        // Assert Exception

    }

    @Test
    public void givenProducts_whenFindAll_thenReturnProducts() {

        // Given
        List<Producto> listaProductos = new ArrayList<>();

        Producto productoPan = new Producto();
        productoPan.setNombre("Pan");
        productoPan.setStock(10);
        this.productoRepository.save(productoPan);
        listaProductos.add(productoPan);

        Producto productoTv = new Producto();
        productoTv.setNombre("Tv");
        productoTv.setStock(5);
        this.productoRepository.save(productoTv);
        listaProductos.add(productoTv);

        // When
        List<Producto> productos = this.productoService.getProductos();

        // Then
        Assert.assertEquals(2, productos.size());
        Assert.assertEquals(listaProductos, productos);

    }

    @Test
    public void givenNewProduct_whenFindFindById_thenReturnNewProduct() {

        // Given
        Producto nuevoProducto = new Producto();
        nuevoProducto.setNombre("Matias");
        Long idProductoPersistido = this.productoService.nuevoProducto(nuevoProducto);

        // When
        Optional<Producto> productoOptional = this.productoRepository.findById(idProductoPersistido);

        // Then
        Assert.assertEquals(nuevoProducto, productoOptional.get());

    }

    @Test(expected = ElementNotFoundException.class)
    public void givenProduct_whenDeleted_thenReturnException() throws ElementNotFoundException {

        // Given
        Producto nuevoProducto = new Producto();
        nuevoProducto.setNombre("Matias");
        Long idProductoPersistido = this.productoService.nuevoProducto(nuevoProducto);

        // When
        this.productoService.eliminarProducto(idProductoPersistido);
        this.productoService.getProducto(idProductoPersistido);

        // Then
        // Exception

    }

    @Test(expected = OutOfStockException.class)
    public void givenProductsWithoutStock_whenValidateStock_thenReturnOutOfStockException() throws ElementNotFoundException, OutOfStockException {

        // Given
        Producto productoPan = new Producto();
        productoPan.setNombre("Pan");
        productoPan.setStock(10);
        Producto productoPanPersistido = this.productoRepository.save(productoPan);

        List<ItemDTO> listaItems = new ArrayList<>();
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setIdProducto(productoPanPersistido.getId());
        itemDTO.setCantidad(25);
        listaItems.add(itemDTO);

        // When
        this.productoService.validarDisponibilidad(listaItems);

        // Then
        // Exception

    }

}
