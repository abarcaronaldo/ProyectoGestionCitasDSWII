package com.gestioncitas.reporte_service.dto;

public record ResumenMedicoDTO(
        Long medicoId,
        String medicoNombre,
        long totalReservadas,
        long totalAtendidas,
        long totalCanceladas,
        long totalNoAsistio,
        long total
) {
}
