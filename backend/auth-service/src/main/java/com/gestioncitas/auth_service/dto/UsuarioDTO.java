package com.gestioncitas.auth_service.dto;

import com.gestioncitas.auth_service.Entity.Usuario;

public record UsuarioDTO(
        Long id,
        String nombres,
        String apellidos,
        String email,
        String telefono,
        String rol,
        boolean activo
) {
    public static UsuarioDTO from(Usuario u) {
        return new UsuarioDTO(
                u.getId(), u.getNombre(), u.getApellido(), u.getEmail(),
                u.getTelefono(), u.getRol().getNombre(), u.isActivo());
    }
}
