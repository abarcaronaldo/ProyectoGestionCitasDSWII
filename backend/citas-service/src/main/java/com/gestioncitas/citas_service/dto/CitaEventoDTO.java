package com.gestioncitas.citas_service.dto;

import com.gestioncitas.citas_service.Entity.EstadoCita;

import java.time.LocalDateTime;

public record CitaEventoDTO(
        Long citaId,
        Long pacienteId,
        Long medicoId,
        Long especialidadId,
        LocalDateTime fechaHora,
        EstadoCita estado,
        LocalDateTime ocurridoEn
) {
    public static CitaEventoDTO from(CitaDTO c) {
        return new CitaEventoDTO(
                c.id(), c.pacienteId(), c.medicoId(), c.especialidadId(),
                c.fechaHora(), c.estado(), LocalDateTime.now());
    }
}
