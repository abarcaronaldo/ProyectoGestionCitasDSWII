package com.gestioncitas.paciente_service.Repository;

import com.gestioncitas.paciente_service.Entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    boolean existsByDni(String dni);

    Optional<Paciente> findByDni(String dni);

    Optional<Paciente> findByUsuarioId(Long usuarioId);
}
