package com.gestioncitas.reporte_service.dto;

import java.time.LocalDateTime;

/**
 * Forma del evento que publica citas-service (CitaEventoDTO) tal como llega
 * a este consumidor. Es un contrato propio: no se comparte la clase del productor.
 */
public record EventoCitaDTO(
        Long citaId,
        Long pacienteId,
        Long medicoId,
        Long especialidadId,
        LocalDateTime fechaHora,
        String estado,
        LocalDateTime ocurridoEn
) {
}
