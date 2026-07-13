package com.sena.ecommerce.controller;

import java.util.ArrayList;
import java.util.Date;
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

import com.sena.ecommerce.model.DetalleOrden;
import com.sena.ecommerce.model.Orden;
import com.sena.ecommerce.model.Producto;
import com.sena.ecommerce.model.Usuario;
import com.sena.ecommerce.service.IDetalleOrdenService;
import com.sena.ecommerce.service.IOrdenService;
import com.sena.ecommerce.service.IProductoService;
import com.sena.ecommerce.service.IUsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/") // la raiz del proyecto
public class HomeUserController {

	// BUG CRÍTICO ORIGINAL: "List<DetalleOrden> detalles" y "Orden orden" eran
	// campos de instancia de este @Controller. Spring crea los controllers
	// como singletons, así que ese carrito era UNO SOLO compartido por todos
	// los usuarios concurrentes de la aplicación: el carrito de un usuario
	// podía mezclarse o pisarse con el de otro. Ahora viven en la sesión HTTP
	// de cada usuario.
	private static final String CART_SESSION_KEY = "carrito";
	private static final String ORDEN_SESSION_KEY = "ordenActual";

	private final Logger LOGGER = (Logger) LoggerFactory.getLogger(HomeUserController.class);

	@Autowired
	private IProductoService productoService;

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private IOrdenService ordenService;

	@Autowired
	private IDetalleOrdenService detalleOrdenService;

	@SuppressWarnings("unchecked")
	private List<DetalleOrden> getCarrito(HttpSession session) {
		List<DetalleOrden> carrito = (List<DetalleOrden>) session.getAttribute(CART_SESSION_KEY);
		if (carrito == null) {
			carrito = new ArrayList<>();
			session.setAttribute(CART_SESSION_KEY, carrito);
		}
		return carrito;
	}

	private Orden getOrdenActual(HttpSession session) {
		Orden orden = (Orden) session.getAttribute(ORDEN_SESSION_KEY);
		if (orden == null) {
			orden = new Orden();
			session.setAttribute(ORDEN_SESSION_KEY, orden);
		}
		return orden;
	}

	@GetMapping("")
	public String home(Model model, HttpSession session) {
		LOGGER.info("sesion usuario: {}", session.getAttribute("idUsuario"));
		model.addAttribute("productos", productoService.findAll());
		model.addAttribute("sesion", session.getAttribute("idUsuario"));
		return "usuario/home";
	}

	@GetMapping("productohome/{id}")
	public String productoHome(@PathVariable Integer id, Model model, HttpSession session) {
		Optional<Producto> op = productoService.get(id);
		if (op.isEmpty()) {
			return "redirect:/";
		}
		model.addAttribute("producto", op.get());
		model.addAttribute("sesion", session.getAttribute("idUsuario"));
		return "usuario/productoHome";
	}

	@PostMapping("/cart")
	public String addCart(@RequestParam Integer id, @RequestParam Double cantidad, Model model, HttpSession session) {
		List<DetalleOrden> carrito = getCarrito(session);
		Orden orden = getOrdenActual(session);

		Optional<Producto> op = productoService.get(id);
		if (op.isEmpty()) {
			return "redirect:/";
		}
		Producto p = op.get();

		boolean yaInsertado = carrito.stream().anyMatch(prod -> prod.getProducto().getId().equals(p.getId()));
		if (!yaInsertado) {
			DetalleOrden detaorden = new DetalleOrden();
			detaorden.setCantidad(cantidad);
			detaorden.setPrecio(p.getPrecio());
			detaorden.setNombre(p.getNombre());
			detaorden.setTotal(p.getPrecio() * cantidad);
			detaorden.setProducto(p);
			carrito.add(detaorden);
		}

		double sumaTotal = carrito.stream().mapToDouble(DetalleOrden::getTotal).sum();
		orden.setTotal(sumaTotal);

		model.addAttribute("cart", carrito);
		model.addAttribute("orden", orden);
		model.addAttribute("sesion", session.getAttribute("idUsuario"));
		return "usuario/carrito";
	}

	@GetMapping("/delete/cart/{id}")
	public String deleteProductoCart(@PathVariable Integer id, Model model, HttpSession session) {
		List<DetalleOrden> carrito = getCarrito(session);
		Orden orden = getOrdenActual(session);

		carrito.removeIf(detalleOrden -> detalleOrden.getProducto().getId().equals(id));

		double sumaTotal = carrito.stream().mapToDouble(DetalleOrden::getTotal).sum();
		orden.setTotal(sumaTotal);

		model.addAttribute("cart", carrito);
		model.addAttribute("orden", orden);
		model.addAttribute("sesion", session.getAttribute("idUsuario"));
		return "usuario/carrito";
	}

	@GetMapping("/getCart")
	public String getCart(Model model, HttpSession session) {
		model.addAttribute("cart", getCarrito(session));
		model.addAttribute("orden", getOrdenActual(session));
		model.addAttribute("sesion", session.getAttribute("idUsuario"));
		return "/usuario/carrito";
	}

	@GetMapping("/orden")
	public String orden(Model model, HttpSession session) {
		// Antes: Integer.parseInt(session.getAttribute("idUsuario").toString())
		// sin comprobar null primero — NPE si se llegaba aquí sin sesión.
		Object idUsuario = session.getAttribute("idUsuario");
		if (idUsuario == null) {
			return "redirect:/usuario/login";
		}
		Optional<Usuario> u = usuarioService.findById(Integer.parseInt(idUsuario.toString()));
		if (u.isEmpty()) {
			return "redirect:/usuario/login";
		}

		model.addAttribute("cart", getCarrito(session));
		model.addAttribute("orden", getOrdenActual(session));
		model.addAttribute("usuario", u.get());
		model.addAttribute("sesion", idUsuario);
		return "usuario/resumenorden";
	}

	@GetMapping("/saveOrder")
	public String saveOrder(HttpSession session) {
		Object idUsuario = session.getAttribute("idUsuario");
		if (idUsuario == null) {
			return "redirect:/usuario/login";
		}
		Optional<Usuario> optUsuario = usuarioService.findById(Integer.parseInt(idUsuario.toString()));
		if (optUsuario.isEmpty()) {
			return "redirect:/usuario/login";
		}

		List<DetalleOrden> carrito = getCarrito(session);
		if (carrito.isEmpty()) {
			return "redirect:/getCart";
		}
		Orden orden = getOrdenActual(session);

		orden.setFechacreacion(new Date());
		orden.setNumero(ordenService.generarNumeroOrden());
		orden.setUsuario(optUsuario.get());
		ordenService.save(orden);

		for (DetalleOrden dt : carrito) {
			dt.setOrden(orden);
			detalleOrdenService.save(dt);

			Producto p = dt.getProducto();
			int cantComprada = dt.getCantidad().intValue();
			if (p.getCantidad() >= cantComprada) {
				p.setCantidad(p.getCantidad() - cantComprada);
				productoService.update(p);
			} else {
				throw new IllegalStateException("Stock insuficiente para el producto: " + p.getNombre());
			}
		}

		session.removeAttribute(CART_SESSION_KEY);
		session.removeAttribute(ORDEN_SESSION_KEY);
		return "redirect:/";
	}

	@PostMapping("/search")
	public String searchProducto(@RequestParam String nombre, Model model, HttpSession session) {
		List<Producto> productos = productoService.findAll().stream()
				.filter(p -> p.getNombre().toUpperCase().contains(nombre.toUpperCase())
						|| p.getDescripcion().toUpperCase().contains(nombre.toUpperCase()))
				.collect(Collectors.toList());
		model.addAttribute("productos", productos);
		model.addAttribute("sesion", session.getAttribute("idUsuario"));
		return "usuario/home";
	}

}
