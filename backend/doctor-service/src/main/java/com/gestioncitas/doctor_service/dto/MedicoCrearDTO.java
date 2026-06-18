package com.gestioncitas.doctor_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MedicoCrearDTO(
        Long usuarioId,
        @NotBlank String nombres,
        @NotBlank String apellidos,
        @NotBlank String cmp,
        String telefono,
        @Email String email,
        @NotNull Long especialidadId
) {
}
