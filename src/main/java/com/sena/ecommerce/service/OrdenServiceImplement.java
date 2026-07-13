package com.sena.ecommerce.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sena.ecommerce.model.Orden;
import com.sena.ecommerce.model.Usuario;
import com.sena.ecommerce.repository.IOrdenRepository;

@Service
public class OrdenServiceImplement implements IOrdenService {

	@Autowired
	private IOrdenRepository ordenRepository;

	@Override
	public Orden save(Orden orden) {
		return ordenRepository.save(orden);
	}

	@Override
	public List<Orden> findAll() {
		return ordenRepository.findAll();
	}

	// BUG ORIGINAL: las ramas if/else if solo cubrían numero < 1000. A partir
	// de la orden 1000, "numeroConcatenado" quedaba en "" (sin rama else),
	// y la siguiente llamada hacía Integer.parseInt("") sobre esa cadena
	// vacía al recorrer las órdenes existentes, reventando con
	// NumberFormatException. String.format con ancho fijo cubre cualquier
	// magnitud sin ramas manuales.
	@Override
	public String generarNumeroOrden() {
		List<Orden> ordenes = findAll();

		int numero = ordenes.stream().map(Orden::getNumero).mapToInt(Integer::parseInt).max().orElse(0) + 1;

		return String.format("%013d", numero);
	}

	@Override
	public List<Orden> findByUsuario(Usuario usuario) {
		return ordenRepository.findByUsuario(usuario);
	}

	@Override
	public Optional<Orden> findById(Integer id) {
		return ordenRepository.findById(id);
	}

}
