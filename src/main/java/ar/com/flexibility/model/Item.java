package ar.com.flexibility.model;

import ar.com.flexibility.model.dto.ItemDTO;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Item {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Producto producto;
    private Integer cantidad;

    public Item() {

    }

    public Item(ItemDTO itemDTO) {

        this.cantidad = itemDTO.getCantidad();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

}
