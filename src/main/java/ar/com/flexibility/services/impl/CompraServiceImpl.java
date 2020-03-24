package ar.com.flexibility.services.impl;

import ar.com.flexibility.exceptions.ElementNotFoundException;
import ar.com.flexibility.model.Compra;
import ar.com.flexibility.repositories.CompraRepository;
import ar.com.flexibility.services.CompraService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompraServiceImpl implements CompraService {

    private Logger logger = LoggerFactory.getLogger(CompraServiceImpl.class);

    private CompraRepository compraRepository;

    /**
     * Obtengo todas las compras realizadas
     *
     * @return -
     */
    @Override
    public List<Compra> getCompras() {

        logger.info("Consultando todas las compras realizadas.");
        return (List<Compra>) this.compraRepository.findAll();

    }

    /**
     * Aprueba una compra realizada por un cliente
     *
     * @param idCompra - id de la compra a autorizar
     * @throws ElementNotFoundException -
     */
    @Override
    public void aprobarCompra(Long idCompra) throws ElementNotFoundException {

        Optional<Compra> compraOptional = this.compraRepository.findById(idCompra);
        if (compraOptional.isPresent()) {

            Compra compra = compraOptional.get();
            compra.setApproval(true);

            this.compraRepository.save(compra);

            logger.info("Compra con id: " + idCompra + " aprobada.");

        } else {
            logger.error("No se pudo aprobar la compra. No se encuentra la compra con id: " + idCompra);
            throw new ElementNotFoundException("La compra con id: " + idCompra + " no se encuentra.");
        }

    }

    @Autowired
    private void setCompraRepository(CompraRepository compraRepository) {

        this.compraRepository = compraRepository;

    }

}
