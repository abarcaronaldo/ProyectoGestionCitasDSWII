package com.gestioncitas.citas_service.dto;

import jakarta.validation.constraints.NotNull;

public record AsistenciaDTO(
        @NotNull Boolean asistio
) {
}
