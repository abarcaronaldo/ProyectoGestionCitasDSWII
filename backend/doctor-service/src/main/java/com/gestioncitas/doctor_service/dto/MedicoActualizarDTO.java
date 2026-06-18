package com.gestioncitas.doctor_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MedicoActualizarDTO(
        @NotBlank String nombres,
        @NotBlank String apellidos,
        String telefono,
        @Email String email,
        @NotNull Long especialidadId
) {
}
