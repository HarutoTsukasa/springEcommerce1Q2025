package com.sena.ecommerce.dto;

import com.sena.ecommerce.model.Usuario;

public class UsuarioMapper {

    private UsuarioMapper() {
    }

    public static UsuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        return new UsuarioDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getDireccion(),
                usuario.getTelefono(),
                usuario.getTipo());
    }
}
