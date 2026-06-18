package com.gestioncitas.historial_medico_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AtencionCrearDTO(
        @NotNull Long citaId,
        @NotBlank String motivoConsulta,
        @NotBlank String diagnostico,
        String tratamiento,
        String observaciones
) {
}
