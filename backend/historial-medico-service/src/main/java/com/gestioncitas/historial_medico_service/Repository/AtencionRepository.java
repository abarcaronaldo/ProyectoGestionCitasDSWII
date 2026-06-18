package com.gestioncitas.historial_medico_service.Repository;

import com.gestioncitas.historial_medico_service.Entity.Atencion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AtencionRepository extends JpaRepository<Atencion, Long> {

    boolean existsByCitaId(Long citaId);

    Optional<Atencion> findByCitaId(Long citaId);

    List<Atencion> findByPacienteId(Long pacienteId);

    List<Atencion> findByMedicoId(Long medicoId);
}
