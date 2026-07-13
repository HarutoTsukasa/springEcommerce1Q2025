package com.sena.ecommerce;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceWebConfiguration implements WebMvcConfigurer {

	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// Ruta absoluta para Windows
		// String externalPath = "file:D:/images/";
		// registry.addResourceHandler("/images/**").addResourceLocations(externalPath);
		registry.addResourceHandler("/images/**").addResourceLocations("file:images/");
	}
}
