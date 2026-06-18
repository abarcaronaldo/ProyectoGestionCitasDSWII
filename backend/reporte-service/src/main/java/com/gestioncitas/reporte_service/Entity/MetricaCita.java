package com.gestioncitas.reporte_service.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Una fila por cada evento de cita recibido (reservada/atendida/cancelada).
 * No es 1:1 con Cita: una misma cita genera varios eventos a lo largo de su vida.
 */
@Entity
@Table(name = "metrica_cita")
@Getter
@Setter
@NoArgsConstructor
public class MetricaCita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cita_id", nullable = false)
    private Long citaId;

    @Column(name = "paciente_id", nullable = false)
    private Long pacienteId;

    @Column(name = "medico_id", nullable = false)
    private Long medicoId;

    @Column(name = "especialidad_id", nullable = false)
    private Long especialidadId;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false, length = 20)
    private String estado;

    @Column(name = "ocurrido_en", nullable = false)
    private LocalDateTime ocurridoEn;

    @Column(name = "recibido_en", nullable = false, updatable = false)
    private LocalDateTime recibidoEn;

    @PrePersist
    public void prePersist() {
        if (recibidoEn == null) {
            recibidoEn = LocalDateTime.now();
        }
    }
}
