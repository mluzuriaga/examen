package ar.com.flexibility.model.dto;

public class ItemDTO {

    private Integer cantidad;
    private Long idProducto;

    public ItemDTO() {

    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

}
