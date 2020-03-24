package ar.com.flexibility.repositories;

import ar.com.flexibility.model.Producto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductoRepository extends CrudRepository<Producto, Long> {

    Optional<Producto> findByNombre(String nombre);

}
