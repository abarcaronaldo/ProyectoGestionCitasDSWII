package com.gestioncitas.citas_service.dto;

import com.gestioncitas.citas_service.Entity.Cita;
import com.gestioncitas.citas_service.Entity.EstadoCita;

import java.time.LocalDateTime;

public record CitaDTO(
        Long id,
        Long pacienteId,
        Long medicoId,
        Long especialidadId,
        LocalDateTime fechaHora,
        EstadoCita estado,
        String motivo,
        LocalDateTime creadaEn
) {
    public static CitaDTO from(Cita c) {
        return new CitaDTO(
                c.getId(), c.getPacienteId(), c.getMedicoId(), c.getEspecialidadId(),
                c.getFechaHora(), c.getEstado(), c.getMotivo(), c.getCreadaEn());
    }
}
