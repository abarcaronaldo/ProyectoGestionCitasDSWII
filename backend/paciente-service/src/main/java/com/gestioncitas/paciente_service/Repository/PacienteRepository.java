package com.gestioncitas.paciente_service.Repository;

import com.gestioncitas.paciente_service.Entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    boolean existsByDni(String dni);

    Optional<Paciente> findByDni(String dni);

    // Solo el paciente ACTIVO: el borrado es lógico (activo=false), y un paciente dado de
    // baja no debe resolver como "el paciente del usuario X". Filtrar por activo además evita
    // el NonUniqueResultException si quedan filas inactivas con el mismo usuarioId.
    Optional<Paciente> findByUsuarioIdAndActivoTrue(Long usuarioId);
}
