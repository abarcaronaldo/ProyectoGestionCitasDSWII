package com.gestioncitas.doctor_service.dto;

import jakarta.validation.constraints.NotBlank;

public record EspecialidadCrearDTO(
        @NotBlank String nombre,
        String descripcion
) {
}
