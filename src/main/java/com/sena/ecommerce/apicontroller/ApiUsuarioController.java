package com.sena.ecommerce.apicontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sena.ecommerce.dto.UsuarioDTO;
import com.sena.ecommerce.dto.UsuarioMapper;
import com.sena.ecommerce.service.IUsuarioService;

// Solo lectura a propósito: no hay caso de uso en esta app para crear/editar
// usuarios por API (el registro ya existe vía /usuario/save). Protegido con
// rol ADMIN en SpringBootSecurity — a diferencia del catálogo de productos,
// la lista de usuarios nunca debería ser pública.
@RestController
@RequestMapping("/apiusuarios")
public class ApiUsuarioController {

	@Autowired
	private IUsuarioService usuarioService;

	@GetMapping("/api")
	public List<UsuarioDTO> getAllUsuarios() {
		return usuarioService.findAll().stream()
				.map(UsuarioMapper::toDTO)
				.toList();
	}

	@GetMapping("/api/{id}")
	public ResponseEntity<UsuarioDTO> getUsuarioById(@PathVariable Integer id) {
		return usuarioService.findById(id)
				.map(UsuarioMapper::toDTO)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
}
