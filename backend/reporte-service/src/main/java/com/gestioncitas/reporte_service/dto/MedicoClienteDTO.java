package com.gestioncitas.reporte_service.dto;

public record MedicoClienteDTO(
        Long id,
        String nombres,
        String apellidos,
        boolean activo
) {
}
