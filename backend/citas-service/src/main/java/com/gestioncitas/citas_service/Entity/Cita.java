package com.gestioncitas.citas_service.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "cita")
@Getter
@Setter
@NoArgsConstructor
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Referencia LÓGICA al paciente (vive en paciente_db, otra base de datos)
    @Column(name = "paciente_id", nullable = false)
    private Long pacienteId;

    // Referencia LÓGICA al médico (vive en doctor_db, otra base de datos)
    @Column(name = "medico_id", nullable = false)
    private Long medicoId;

    // Referencia LÓGICA a la especialidad (vive en doctor_db, otra base de datos)
    @Column(name = "especialidad_id", nullable = false)
    private Long especialidadId;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoCita estado = EstadoCita.RESERVADA;

    @Column(length = 300)
    private String motivo;

    @Column(name = "creada_en", nullable = false, updatable = false)
    private LocalDateTime creadaEn;

    // Bloqueo optimista: Hibernate incrementa esta columna en cada UPDATE
    // y rechaza la escritura (OptimisticLockException) si la versión leída ya cambió.
    @Version
    private Long version;

    @PrePersist
    public void prePersist() {
        if (creadaEn == null) {
            creadaEn = LocalDateTime.now();
        }
    }
}
