package com.gestioncitas.citas_service.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReservaCitaDTO(
        @NotNull Long pacienteId,
        @NotNull Long medicoId,
        @NotNull Long especialidadId,
        @NotNull @Future LocalDateTime fechaHora,
        String motivo
) {
}
