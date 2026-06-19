package com.gestioncitas.doctor_service.Repository;

import com.gestioncitas.doctor_service.Entity.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EspecialidadRepository extends JpaRepository<Especialidad, Long> {

    boolean existsByNombre(String nombre);

    Optional<Especialidad> findByNombre(String nombre);

    List<Especialidad> findByActivoTrue();
}
