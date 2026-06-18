package com.gestioncitas.citas_service.dto;

public record PacienteClienteDTO(
        Long id,
        String nombres,
        String apellidos,
        boolean activo
) {
}
