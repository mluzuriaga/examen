package ar.com.flexibility.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Compra {

    @Id
    @GeneratedValue
    private Long id;

    private Long idCliente;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Item> items;

    private boolean approval;

    public Compra() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public boolean isApproval() {
        return approval;
    }

    public void setApproval(boolean approval) {
        this.approval = approval;
    }

}
