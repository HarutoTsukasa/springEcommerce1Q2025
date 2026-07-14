package com.sena.ecommerce.dto;

import java.util.Date;
import java.util.List;

public class OrdenDTO {

    private Integer id;
    private String numero;
    private Date fechacreacion;
    private Date fecharecibida;
    private Double total;
    private Integer usuarioId;
    private String usuarioNombre;
    private List<DetalleOrdenDTO> detalle;

    public OrdenDTO() {
    }

    public OrdenDTO(Integer id, String numero, Date fechacreacion, Date fecharecibida, Double total,
            Integer usuarioId, String usuarioNombre, List<DetalleOrdenDTO> detalle) {
        this.id = id;
        this.numero = numero;
        this.fechacreacion = fechacreacion;
        this.fecharecibida = fecharecibida;
        this.total = total;
        this.usuarioId = usuarioId;
        this.usuarioNombre = usuarioNombre;
        this.detalle = detalle;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getFechacreacion() {
        return fechacreacion;
    }

    public void setFechacreacion(Date fechacreacion) {
        this.fechacreacion = fechacreacion;
    }

    public Date getFecharecibida() {
        return fecharecibida;
    }

    public void setFecharecibida(Date fecharecibida) {
        this.fecharecibida = fecharecibida;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public List<DetalleOrdenDTO> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<DetalleOrdenDTO> detalle) {
        this.detalle = detalle;
    }
}
