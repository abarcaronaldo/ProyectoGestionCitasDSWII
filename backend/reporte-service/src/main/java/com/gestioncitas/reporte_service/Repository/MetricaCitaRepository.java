package com.gestioncitas.reporte_service.Repository;

import com.gestioncitas.reporte_service.Entity.MetricaCita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MetricaCitaRepository extends JpaRepository<MetricaCita, Long> {

    long countByEstado(String estado);

    long countByMedicoId(Long medicoId);

    long countByMedicoIdAndEstado(Long medicoId, String estado);

    long countByEspecialidadId(Long especialidadId);

    long countByEspecialidadIdAndEstado(Long especialidadId, String estado);

    List<MetricaCita> findByMedicoId(Long medicoId);

    List<MetricaCita> findByEspecialidadId(Long especialidadId);
}
