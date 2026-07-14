package com.sena.ecommerce.controller;

import java.io.IOException;
import java.util.Optional;

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
import org.springframework.web.multipart.MultipartFile;

import com.sena.ecommerce.model.Producto;
import com.sena.ecommerce.model.Usuario;
import com.sena.ecommerce.service.IProductoService;
import com.sena.ecommerce.service.IUsuarioService;
import com.sena.ecommerce.service.UploadFileService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/productos")
public class ProductoController {

	private final Logger LOGGER = (Logger) LoggerFactory.getLogger(ProductoController.class);

	@Autowired
	private IProductoService productoService;

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private UploadFileService upload;

	@GetMapping("")
	public String show(Model model) {
		model.addAttribute("productos", productoService.findAll());
		return "productos/show";
	}

	@GetMapping("/create")
	public String create() {
		return "productos/create";
	}

	@PostMapping("/save")
	public String save(Producto producto, @RequestParam("img") MultipartFile file, HttpSession session)
			throws IOException {
		LOGGER.info("Este es el objeto del producto a guardar en la DB {}", producto);
		Object idUsuario = session.getAttribute("idUsuario");
		if (idUsuario == null) {
			return "redirect:/usuario/login";
		}
		Usuario u = usuarioService.findById(Integer.parseInt(idUsuario.toString())).orElse(null);
		if (u == null) {
			return "redirect:/usuario/login";
		}
		producto.setUsuario(u);
		if (producto.getId() == null) {
			String nombreImagen = upload.saveImages(file, producto.getNombre());
			producto.setImagen(nombreImagen);
		}
		productoService.save(producto);
		return "redirect:/productos";
	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Integer id, Model model) {
		Optional<Producto> op = productoService.get(id);
		if (op.isEmpty()) {
			return "redirect:/productos";
		}
		LOGGER.info("Busqueda de producto por id {}", op.get());
		model.addAttribute("producto", op.get());
		return "productos/edit";
	}

	@PostMapping("/update")
	public String update(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {
		LOGGER.info("Este es el objeto del producto a actualizar el DB {}", producto);
		Optional<Producto> op = productoService.get(producto.getId());
		if (op.isEmpty()) {
			return "redirect:/productos";
		}
		Producto p = op.get();

		if (file.isEmpty()) {
			producto.setImagen(p.getImagen());
		} else {
			// Antes: p.getImagen().equals("default.jpg") sin comprobar null primero.
			if (p.getImagen() != null && !p.getImagen().equals("default.jpg")) {
				upload.deleteImage(p.getImagen());
			}
			String nombreImagen = upload.saveImages(file, p.getNombre());
			producto.setImagen(nombreImagen);
		}
		producto.setUsuario(p.getUsuario());
		productoService.update(producto);
		return "redirect:/productos";
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id) {
		Optional<Producto> op = productoService.get(id);
		if (op.isEmpty()) {
			return "redirect:/productos";
		}
		Producto p = op.get();
		// Antes: mismo NPE potencial que en update().
		if (p.getImagen() != null && !p.getImagen().equals("default.jpg")) {
			upload.deleteImage(p.getImagen());
		}
		productoService.delete(id);
		return "redirect:/productos";
	}

}
