package com.gestioncitas.reporte_service.dto;

public record ResumenGeneralDTO(
        long totalReservadas,
        long totalAtendidas,
        long totalCanceladas,
        long totalNoAsistio,
        long total
) {
}
