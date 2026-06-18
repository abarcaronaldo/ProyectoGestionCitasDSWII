package com.gestioncitas.reporte_service.dto;

public record ResumenEspecialidadDTO(
        Long especialidadId,
        String especialidadNombre,
        long totalReservadas,
        long totalAtendidas,
        long totalCanceladas,
        long totalNoAsistio,
        long total
) {
}
