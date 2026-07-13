package com.sena.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sena.ecommerce.model.Producto;
import com.sena.ecommerce.model.Usuario;
import com.sena.ecommerce.service.IProductoService;
import com.sena.ecommerce.service.IUsuarioService;
import com.sena.ecommerce.service.UploadFileService;

import java.util.List;
import java.util.Optional;

@RestController // Cambio de @Controller a @RestController
@RequestMapping("/apiproductos")
public class ApiProductoController {

	@Autowired
	private IProductoService productoservice;

	@Autowired
	private UploadFileService upload;

	@Autowired
	private IUsuarioService usuarioservice;

	// Endpoint GET para obtener todos los productos (API)
	@GetMapping("/api")
	public List<Producto> getAllProducts() {
		return productoservice.findAll();
	}

	// Endpoint GET para obtener un producto por ID (API)
	@GetMapping("/api/{id}")
	public ResponseEntity<Producto> getProductById(@PathVariable Integer id) {
		Optional<Producto> producto = productoservice.get(id);
		return producto.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	// Endpoint POST para crear nuevo producto (API)
	@PostMapping("/api")
	public ResponseEntity<Producto> createProduct(@RequestBody Producto producto) {
		// Obtener usuario de la sesión (ajustar según tu lógica de autenticación)
		Usuario u = usuarioservice.findById(4).get();
		producto.setUsuario(u);

		// Setear imagen por defecto si no se proporciona
		if (producto.getImagen() == null)
			producto.setImagen("default.jpg");

		Producto savedProduct = productoservice.save(producto);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
	}

	// Endpoint PUT para actualizar producto (API)
	@PutMapping("/api/{id}")
	public ResponseEntity<Producto> updateProduct(@PathVariable Integer id, @RequestBody Producto productDetails) {
		Optional<Producto> producto = productoservice.get(id);
		if (producto.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Producto existingProduct = producto.get();
		existingProduct.setNombre(productDetails.getNombre());
		existingProduct.setDescripcion(productDetails.getDescripcion());
		existingProduct.setPrecio(productDetails.getPrecio());
		existingProduct.setCantidad(productDetails.getCantidad());

		// Mantener la imagen existente a menos que se envíe una nueva
		if (productDetails.getImagen() != null) {
			existingProduct.setImagen(productDetails.getImagen());
		}

		productoservice.save(existingProduct);
		return ResponseEntity.ok(existingProduct);
	}

	// Endpoint DELETE para eliminar producto (API)
	@DeleteMapping("/api/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {
		Optional<Producto> producto = productoservice.get(id);
		if (producto.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Producto p = producto.get();
		if (!p.getImagen().equals("default.jpg")) {
			upload.deleteImage(p.getImagen());
		}

		productoservice.delete(id);
		return ResponseEntity.ok().build();
	}
}