package com.sena.ecommerce.dto;

import java.util.List;

import com.sena.ecommerce.model.DetalleOrden;
import com.sena.ecommerce.model.Orden;
import com.sena.ecommerce.model.Usuario;

public class OrdenMapper {

    private OrdenMapper() {
    }

    public static DetalleOrdenDTO toDTO(DetalleOrden detalle) {
        if (detalle == null) {
            return null;
        }
        return new DetalleOrdenDTO(
                detalle.getId(),
                detalle.getNombre(),
                detalle.getCantidad(),
                detalle.getPrecio(),
                detalle.getTotal(),
                detalle.getProducto() != null ? detalle.getProducto().getId() : null);
    }

    public static OrdenDTO toDTO(Orden orden) {
        if (orden == null) {
            return null;
        }
        Usuario usuario = orden.getUsuario();
        List<DetalleOrdenDTO> detalles = orden.getDetalle() == null
                ? List.of()
                : orden.getDetalle().stream().map(OrdenMapper::toDTO).toList();

        return new OrdenDTO(
                orden.getId(),
                orden.getNumero(),
                orden.getFechacreacion(),
                orden.getFecharecibida(),
                orden.getTotal(),
                usuario != null ? usuario.getId() : null,
                usuario != null ? usuario.getNombre() : null,
                detalles);
    }
}
