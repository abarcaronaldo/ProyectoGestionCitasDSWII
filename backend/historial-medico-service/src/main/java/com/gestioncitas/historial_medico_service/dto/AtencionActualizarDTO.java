package com.gestioncitas.historial_medico_service.dto;

import jakarta.validation.constraints.NotBlank;

public record AtencionActualizarDTO(
        @NotBlank String motivoConsulta,
        @NotBlank String diagnostico,
        String tratamiento,
        String observaciones
) {
}
