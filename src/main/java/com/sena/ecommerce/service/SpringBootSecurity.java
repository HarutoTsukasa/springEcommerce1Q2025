package com.sena.ecommerce.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// clase es el interceptor con dos anotaciones jpa
@Configuration
public class SpringBootSecurity {

	// El campo "userDetailsService" y el método
	// configure(AuthenticationManagerBuilder)
	// de la versión original nunca se ejecutaban: esta clase no extiende
	// WebSecurityConfigurerAdapter, así que ese hook no existe en Spring Security
	// moderno. Spring detecta solo el bean UserDetailsService
	// (UserDetailServiceImplement) automáticamente. Los eliminé por ser
	// código muerto que sugería una protección que no existía.

	// @SuppressWarnings("deprecation")
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(requests -> requests.requestMatchers("/administrador/**").hasRole("ADMIN")
				.requestMatchers("/productos/**").hasRole("ADMIN")
				// Antes: /apiproductos/** quedaba totalmente fuera de estas reglas,
				// así que cualquiera sin sesión podía crear/editar/borrar productos
				// vía API. Ahora exige sesión para crear y rol ADMIN para
				// editar/borrar; las lecturas siguen siendo públicas.
				.requestMatchers(HttpMethod.POST, "/apiproductos/api").authenticated()
				.requestMatchers(HttpMethod.PUT, "/apiproductos/api/**").hasRole("ADMIN")
				.requestMatchers(HttpMethod.DELETE, "/apiproductos/api/**").hasRole("ADMIN")
				.requestMatchers(HttpMethod.GET, "/apiproductos/**").permitAll().anyRequest().permitAll())
				// Dejo CSRF activo salvo para la API (los clientes REST no mandan
				// el token). Los formularios Thymeleaf existentes usan th:action,
				// que con el dialecto de seguridad de Thymeleaf inyecta el token
				// automáticamente — [Probable, no lo pude probar contra tu
				// entorno real] verifícalo en /usuario/login, /usuario/registro,
				// /productos/create y /productos/edit antes de desplegar.
				.csrf(csrf -> csrf.ignoringRequestMatchers("/apiproductos/**"))
				.formLogin(login -> login.loginPage("/usuario/login").permitAll().defaultSuccessUrl("/usuario/acceder"))
				.logout(logout -> logout.permitAll());
		return http.build();
	}

	@Bean
	BCryptPasswordEncoder getEnecoder() {
		return new BCryptPasswordEncoder();
	}

}
