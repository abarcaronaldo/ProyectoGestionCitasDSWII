package com.gestioncitas.citas_service.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReprogramarCitaDTO(
        @NotNull @Future LocalDateTime fechaHora
) {
}
