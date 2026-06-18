package com.gestioncitas.historial_medico_service.dto;

import com.gestioncitas.historial_medico_service.Entity.Atencion;

import java.time.LocalDateTime;

public record AtencionDTO(
        Long id,
        Long citaId,
        Long pacienteId,
        Long medicoId,
        LocalDateTime fechaAtencion,
        String motivoConsulta,
        String diagnostico,
        String tratamiento,
        String observaciones,
        boolean activo,
        LocalDateTime creadoEn
) {
    public static AtencionDTO from(Atencion a) {
        return new AtencionDTO(
                a.getId(), a.getCitaId(), a.getPacienteId(), a.getMedicoId(), a.getFechaAtencion(),
                a.getMotivoConsulta(), a.getDiagnostico(), a.getTratamiento(), a.getObservaciones(),
                a.isActivo(), a.getCreadoEn());
    }
}
