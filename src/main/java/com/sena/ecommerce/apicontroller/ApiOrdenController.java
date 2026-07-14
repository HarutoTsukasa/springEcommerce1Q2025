package com.sena.ecommerce.apicontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sena.ecommerce.dto.OrdenDTO;
import com.sena.ecommerce.dto.OrdenMapper;
import com.sena.ecommerce.service.IOrdenService;

// Solo lectura, protegido con rol ADMIN en SpringBootSecurity. Igual que con
// Usuario: una orden trae datos de compra y del comprador, no es catálogo
// público como Producto.
@RestController
@RequestMapping("/apiordenes")
public class ApiOrdenController {

	@Autowired
	private IOrdenService ordenService;

	@GetMapping("/api")
	public List<OrdenDTO> getAllOrdenes() {
		return ordenService.findAll().stream()
				.map(OrdenMapper::toDTO)
				.toList();
	}

	@GetMapping("/api/{id}")
	public ResponseEntity<OrdenDTO> getOrdenById(@PathVariable Integer id) {
		return ordenService.findById(id)
				.map(OrdenMapper::toDTO)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
}
