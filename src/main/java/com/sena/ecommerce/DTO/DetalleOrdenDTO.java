package com.sena.ecommerce.dto;

public class DetalleOrdenDTO {

    private Integer id;
    private String nombre;
    private Double cantidad;
    private Double precio;
    private Double total;
    private Integer productoId;

    public DetalleOrdenDTO() {
    }

    public DetalleOrdenDTO(Integer id, String nombre, Double cantidad, Double precio, Double total,
            Integer productoId) {
        this.id = id;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
        this.total = total;
        this.productoId = productoId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Integer getProductoId() {
        return productoId;
    }

    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }
}
