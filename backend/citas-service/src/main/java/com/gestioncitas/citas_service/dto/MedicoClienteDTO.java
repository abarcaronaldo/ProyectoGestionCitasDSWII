package com.gestioncitas.citas_service.dto;

public record MedicoClienteDTO(
        Long id,
        String nombres,
        String apellidos,
        Long especialidadId,
        boolean activo
) {
}
