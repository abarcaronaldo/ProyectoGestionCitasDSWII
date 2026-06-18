package com.gestioncitas.citas_service.Repository;

import com.gestioncitas.citas_service.Entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByPacienteId(Long pacienteId);

    List<Cita> findByMedicoId(Long medicoId);

    boolean existsByMedicoIdAndFechaHora(Long medicoId, LocalDateTime fechaHora);

    List<Cita> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);
}
