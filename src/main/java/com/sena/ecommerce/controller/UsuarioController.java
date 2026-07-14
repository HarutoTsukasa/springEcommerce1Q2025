package com.sena.ecommerce.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sena.ecommerce.model.Orden;
import com.sena.ecommerce.model.Usuario;
import com.sena.ecommerce.service.IOrdenService;
import com.sena.ecommerce.service.IUsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	private final Logger LOGGER = (Logger) LoggerFactory.getLogger(UsuarioController.class);

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private IOrdenService ordenService;

	BCryptPasswordEncoder passEncode = new BCryptPasswordEncoder();

	@GetMapping("/registro")
	public String createUser() {
		return "usuario/registro";
	}

	@PostMapping("/save")
	public String save(Usuario usuario, Model model) {
		LOGGER.info("Usuario a registrar: {}", usuario);
		usuario.setTipo("USER");
		usuario.setPassword(passEncode.encode(usuario.getPassword()));
		usuarioService.save(usuario);
		return "redirect:/";
	}

	@GetMapping("/login")
	public String login() {
		return "usuario/login";
	}

	@GetMapping("/acceder")
	public String acceder(HttpSession session) {
		// Antes: Integer.parseInt(session.getAttribute("idUsuario").toString())
		// sin comprobar null primero. Si alguien llegaba a esta URL sin haberse
		// autenticado (idUsuario nunca seteado), esto lanzaba NullPointerException
		// en vez de redirigir a login.
		Object idUsuarioAttr = session.getAttribute("idUsuario");
		if (idUsuarioAttr == null) {
			LOGGER.warn("Acceso a /usuario/acceder sin sesión activa");
			return "redirect:/usuario/login";
		}

		Optional<Usuario> user = usuarioService.findById(Integer.parseInt(idUsuarioAttr.toString()));
		if (user.isEmpty()) {
			LOGGER.warn("Usuario no encontrado en DB para id de sesión {}", idUsuarioAttr);
			return "redirect:/usuario/login";
		}

		if (user.get().getTipo().equals("ADMIN")) {
			return "redirect:/administrador";
		}
		return "redirect:/";
	}

	@GetMapping("/cerrar")
	public String cerrarSesion(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

	@GetMapping("/compras")
	public String compras(HttpSession session, Model model) {
		Object idUsuarioAttr = session.getAttribute("idUsuario");
		if (idUsuarioAttr == null) {
			return "redirect:/usuario/login";
		}
		model.addAttribute("sesion", idUsuarioAttr);
		Optional<Usuario> usuario = usuarioService.findById(Integer.parseInt(idUsuarioAttr.toString()));
		if (usuario.isEmpty()) {
			return "redirect:/usuario/login";
		}
		List<Orden> ordenes = ordenService.findByUsuario(usuario.get());
		model.addAttribute("ordenes", ordenes);
		return "usuario/compras";
	}

	@GetMapping("/detalle/{id}")
	public String detalleCompra(HttpSession session, Model model, @PathVariable Integer id) {
		Object idUsuarioAttr = session.getAttribute("idUsuario");
		model.addAttribute("sesion", idUsuarioAttr);
		LOGGER.info("Id de la orden: {}", id);
		Optional<Orden> orden = ordenService.findById(id);
		if (orden.isEmpty()) {
			return "redirect:/usuario/compras";
		}
		model.addAttribute("detalles", orden.get().getDetalle());
		return "usuario/detallecompra";
	}

}
