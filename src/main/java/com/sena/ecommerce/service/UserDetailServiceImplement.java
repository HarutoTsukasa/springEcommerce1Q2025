package com.sena.ecommerce.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.sena.ecommerce.model.Usuario;

import jakarta.servlet.http.HttpSession;

@Service
public class UserDetailServiceImplement implements UserDetailsService {

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	HttpSession session;

	private Logger log = LoggerFactory.getLogger(UserDetailServiceImplement.class);

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// El campo del formulario se llama "username" pero contiene el email
		// (el label del login dice "Email"), por eso se busca con findByEmail.
		Optional<Usuario> optionalUser = usuarioService.findByEmail(username);
		if (optionalUser.isPresent()) {
			Usuario usuario = optionalUser.get();
			log.info("Esto es el ID del usuario: {}", usuario.getId());
			session.setAttribute("idUsuario", usuario.getId());

			// Antes: .username(usuario.getNombre()) — usaba el nombre real de la
			// persona como identidad interna de Spring Security. Dos usuarios con
			// el mismo nombre habrían compartido ese identificador. Ahora se usa
			// el email, que es el mismo campo único con el que se hizo la
			// búsqueda arriba.
			return User.builder().username(usuario.getEmail()).password(usuario.getPassword()).roles(usuario.getTipo())
					.build();
		} else {
			throw new UsernameNotFoundException("Usuario no encontrado");
		}
	}

	public String encodePass(String rowPass) {
		BCryptPasswordEncoder pe = new BCryptPasswordEncoder();
		return pe.encode(rowPass);
	}

}
