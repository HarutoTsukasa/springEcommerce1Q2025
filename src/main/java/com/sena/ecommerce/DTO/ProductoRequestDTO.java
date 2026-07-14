package com.sena.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class ProductoRequestDTO {

	@NotBlank(message = "El nombre es obligatorio")
	private String nombre;

	@NotBlank(message = "La descripción es obligatoria")
	private String descripcion;

	private String imagen;

	@NotNull(message = "El precio es obligatorio")
	@Positive(message = "El precio debe ser mayor que 0")
	private Double precio;

	@NotNull(message = "La cantidad es obligatoria")
	@PositiveOrZero(message = "La cantidad no puede ser negativa")
	private Integer cantidad;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

}
