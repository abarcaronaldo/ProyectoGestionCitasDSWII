package com.gestioncitas.doctor_service.Repository;

import com.gestioncitas.doctor_service.Entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicoRepository extends JpaRepository<Medico, Long> {

    boolean existsByCmp(String cmp);

    Optional<Medico> findByCmp(String cmp);

    Optional<Medico> findByUsuarioId(Long usuarioId);

    List<Medico> findByEspecialidadId(Long especialidadId);

    List<Medico> findByActivoTrue();

    List<Medico> findByEspecialidadIdAndActivoTrue(Long especialidadId);
}
