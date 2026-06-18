package com.gestioncitas.historial_medico_service.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "atencion")
@Getter
@Setter
@NoArgsConstructor
public class Atencion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Referencia LÓGICA a la cita que originó esta atención (vive en citas_db, otra base de datos).
    // Una cita atendida genera exactamente una atención -> unicidad.
    @Column(name = "cita_id", nullable = false, unique = true)
    private Long citaId;

    // Referencia LÓGICA al paciente (vive en paciente_db, otra base de datos)
    @Column(name = "paciente_id", nullable = false)
    private Long pacienteId;

    // Referencia LÓGICA al médico (vive en doctor_db, otra base de datos)
    @Column(name = "medico_id", nullable = false)
    private Long medicoId;

    @Column(name = "fecha_atencion", nullable = false)
    private LocalDateTime fechaAtencion;

    @Column(name = "motivo_consulta", nullable = false, length = 300)
    private String motivoConsulta;

    @Column(nullable = false, length = 1000)
    private String diagnostico;

    @Column(length = 1000)
    private String tratamiento;

    @Column(length = 1000)
    private String observaciones;

    @Column(nullable = false)
    private boolean activo = true;

    @Column(name = "creado_en", nullable = false, updatable = false)
    private LocalDateTime creadoEn;

    @PrePersist
    public void prePersist() {
        if (creadoEn == null) {
            creadoEn = LocalDateTime.now();
        }
    }
}
