package com.gestioncitas.doctor_service.dto;

import com.gestioncitas.doctor_service.Entity.Especialidad;

import java.time.LocalDateTime;

public record EspecialidadDTO(
        Long id,
        String nombre,
        String descripcion,
        boolean activo,
        LocalDateTime creadoEn
) {
    public static EspecialidadDTO from(Especialidad e) {
        return new EspecialidadDTO(
                e.getId(), e.getNombre(), e.getDescripcion(), e.isActivo(), e.getCreadoEn());
    }
}
