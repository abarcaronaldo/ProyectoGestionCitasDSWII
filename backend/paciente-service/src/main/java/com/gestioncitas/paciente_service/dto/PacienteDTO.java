package com.gestioncitas.paciente_service.dto;

import com.gestioncitas.paciente_service.Entity.Paciente;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PacienteDTO(
        Long id,
        Long usuarioId,
        String dni,
        String nombres,
        String apellidos,
        LocalDate fechaNacimiento,
        String sexo,
        String telefono,
        String email,
        String direccion,
        String grupoSanguineo,
        String alergias,
        boolean activo,
        LocalDateTime creadoEn
) {
    public static PacienteDTO from(Paciente p) {
        return new PacienteDTO(
                p.getId(), p.getUsuarioId(), p.getDni(), p.getNombres(), p.getApellidos(),
                p.getFechaNacimiento(), p.getSexo(), p.getTelefono(), p.getEmail(),
                p.getDireccion(), p.getGrupoSanguineo(), p.getAlergias(), p.isActivo(), p.getCreadoEn());
    }
}
