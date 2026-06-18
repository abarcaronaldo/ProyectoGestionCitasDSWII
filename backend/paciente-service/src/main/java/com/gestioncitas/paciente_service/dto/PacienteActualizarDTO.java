package com.gestioncitas.paciente_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record PacienteActualizarDTO(
        @NotBlank String nombres,
        @NotBlank String apellidos,
        LocalDate fechaNacimiento,
        String sexo,
        String telefono,
        @Email String email,
        String direccion,
        String grupoSanguineo,
        String alergias
) {
}
