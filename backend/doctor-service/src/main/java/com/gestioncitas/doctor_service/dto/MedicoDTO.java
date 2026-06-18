package com.gestioncitas.doctor_service.dto;

import com.gestioncitas.doctor_service.Entity.Medico;

import java.time.LocalDateTime;

public record MedicoDTO(
        Long id,
        Long usuarioId,
        String nombres,
        String apellidos,
        String cmp,
        String telefono,
        String email,
        Long especialidadId,
        String especialidadNombre,
        boolean activo,
        LocalDateTime creadoEn
) {
    public static MedicoDTO from(Medico m) {
        return new MedicoDTO(
                m.getId(), m.getUsuarioId(), m.getNombres(), m.getApellidos(), m.getCmp(),
                m.getTelefono(), m.getEmail(), m.getEspecialidad().getId(), m.getEspecialidad().getNombre(),
                m.isActivo(), m.getCreadoEn());
    }
}
