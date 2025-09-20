package com.sena.ecommerce.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sena.ecommerce.model.Orden;
import com.sena.ecommerce.model.Producto;
import com.sena.ecommerce.service.IOrdenService;
import com.sena.ecommerce.service.IProductoService;
import com.sena.ecommerce.service.IUsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/administrador")
public class administradorController {

	private final Logger LOGGER = (Logger) LoggerFactory.getLogger(administradorController.class);

	@Autowired
	private IProductoService productoService;

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private IOrdenService ordenService;

	@GetMapping("")
	public String home(Model model) {
		List<Producto> productos = productoService.findAll();
		model.addAttribute("productos", productos);
		// addAllAtributes --- no es
		return "administrador/home";
	}

	// metodo de usuarios
	@GetMapping("/usuarios")
	public String usuarios(Model model) {
		model.addAttribute("usuarios", usuarioService.findAll());
		return "administrador/usuarios";
	}

	// metodo de las ordenes
	@GetMapping("/ordenes")
	public String ordenes(Model model) {
		model.addAttribute("ordenes", ordenService.findAll());
		return "administrador/ordenes";
	}

	// metodo detalles de una orden
	@GetMapping("/detalle/{id}")
	public String detalleOrden(@PathVariable Integer id, Model model) {
		LOGGER.info("Id de la orden: {}", id);
		Optional<Orden> orden = ordenService.findById(id);
		model.addAttribute("detalles", orden.get().getDetalle());
		return "administrador/detalleorden";
	}

	@PostMapping("/search")
	public String searchProducto(@RequestParam String nombre, Model model, HttpSession session) {
		LOGGER.info("nombre del producto: {}", nombre);
		List<Producto> productos = productoService.findAll().stream()
				.filter(p -> p.getNombre().toUpperCase().contains(nombre.toUpperCase())
						|| p.getDescripcion().toUpperCase().contains(nombre.toUpperCase()))
				.collect(Collectors.toList());
		model.addAttribute("productos", productos);
		model.addAttribute("sesion", session.getAttribute("idUsuario"));
		return "administrador/home";
	}

}
