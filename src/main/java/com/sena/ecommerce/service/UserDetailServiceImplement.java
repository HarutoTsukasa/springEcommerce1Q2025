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

@Service
public class UserDetailServiceImplement implements UserDetailsService {

	@Autowired
	private IUsuarioService usuarioService;

	private Logger log = LoggerFactory.getLogger(UserDetailServiceImplement.class);

	// BUG CRÍTICO: la versión anterior hacía
	// "session.setAttribute("idUsuario", usuario.getId())" AQUÍ, dentro de
	// loadUserByUsername(). Este método solo busca al usuario por email —
	// es DaoAuthenticationProvider quien, DESPUÉS de que este método
	// retorna, compara la contraseña recibida contra la almacenada. Poner
	// el efecto secundario aquí significaba que la sesión quedaba marcada
	// como "logueada" ANTES de que la contraseña se verificara, sin
	// importar si era correcta o no. Cualquiera que supiera el email de un
	// usuario podía tomar su sesión con cualquier contraseña. Ahora este
	// método solo construye y devuelve el UserDetails; la sesión se marca
	// en UsuarioController.acceder(), que solo se ejecuta tras
	// autenticación exitosa real.
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// El campo del formulario se llama "username" pero contiene el email
		// (el label del login dice "Email"), por eso se busca con findByEmail.
		Optional<Usuario> optionalUser = usuarioService.findByEmail(username);
		if (optionalUser.isPresent()) {
			Usuario usuario = optionalUser.get();
			log.info("Usuario encontrado para intento de login, id: {}", usuario.getId());

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
