package com.gestioncitas.historial_medico_service.dto;

import java.time.LocalDateTime;

public record CitaClienteDTO(
        Long id,
        Long pacienteId,
        Long medicoId,
        LocalDateTime fechaHora,
        String estado,
        String motivo
) {
}
