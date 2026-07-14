package com.sena.ecommerce.dto;

import com.sena.ecommerce.model.Producto;
import com.sena.ecommerce.model.Usuario;

public class ProductoMapper {

	private ProductoMapper() {
	}

	public static ProductoDTO toDTO(Producto producto) {
		if (producto == null) {
			return null;
		}
		Usuario usuario = producto.getUsuario();
		return new ProductoDTO(producto.getId(), producto.getNombre(), producto.getDescripcion(), producto.getImagen(),
				producto.getPrecio(), producto.getCantidad(), usuario != null ? usuario.getId() : null,
				usuario != null ? usuario.getNombre() : null);
	}

	public static Producto toEntity(ProductoRequestDTO dto) {
		if (dto == null) {
			return null;
		}
		Producto producto = new Producto();
		producto.setNombre(dto.getNombre());
		producto.setDescripcion(dto.getDescripcion());
		producto.setPrecio(dto.getPrecio());
		producto.setCantidad(dto.getCantidad());
		producto.setImagen(dto.getImagen() != null ? dto.getImagen() : "default.jpg");
		return producto;
	}

	public static void copyToEntity(ProductoRequestDTO dto, Producto target) {
		target.setNombre(dto.getNombre());
		target.setDescripcion(dto.getDescripcion());
		target.setPrecio(dto.getPrecio());
		target.setCantidad(dto.getCantidad());
		if (dto.getImagen() != null) {
			target.setImagen(dto.getImagen());
		}
	}

}
