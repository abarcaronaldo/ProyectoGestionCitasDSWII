package com.gestioncitas.doctor_service.Service;

import com.gestioncitas.doctor_service.Entity.Especialidad;
import com.gestioncitas.doctor_service.Exception.ApiExceptions;
import com.gestioncitas.doctor_service.Repository.EspecialidadRepository;
import com.gestioncitas.doctor_service.dto.EspecialidadActualizarDTO;
import com.gestioncitas.doctor_service.dto.EspecialidadCrearDTO;
import com.gestioncitas.doctor_service.dto.EspecialidadDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EspecialidadService {

    private final EspecialidadRepository especialidadRepository;

    public EspecialidadService(EspecialidadRepository especialidadRepository) {
        this.especialidadRepository = especialidadRepository;
    }

    @Transactional
    public EspecialidadDTO crear(EspecialidadCrearDTO dto) {
        if (especialidadRepository.existsByNombre(dto.nombre())) {
            throw new ApiExceptions.ReglaNegocio("Ya existe una especialidad con el nombre: " + dto.nombre());
        }
        Especialidad especialidad = new Especialidad();
        especialidad.setNombre(dto.nombre());
        especialidad.setDescripcion(dto.descripcion());
        return EspecialidadDTO.from(especialidadRepository.save(especialidad));
    }

    @Transactional(readOnly = true)
    public List<EspecialidadDTO> listar() {
        return especialidadRepository.findByActivoTrue().stream().map(EspecialidadDTO::from).toList();
    }

    @Transactional(readOnly = true)
    public EspecialidadDTO obtener(Long id) {
        return especialidadRepository.findById(id)
                .map(EspecialidadDTO::from)
                .orElseThrow(() -> new ApiExceptions.RecursoNoEncontrado("Especialidad no encontrada: " + id));
    }

    @Transactional
    public EspecialidadDTO actualizar(Long id, EspecialidadActualizarDTO dto) {
        Especialidad especialidad = especialidadRepository.findById(id)
                .orElseThrow(() -> new ApiExceptions.RecursoNoEncontrado("Especialidad no encontrada: " + id));
        especialidad.setDescripcion(dto.descripcion());
        return EspecialidadDTO.from(especialidadRepository.save(especialidad));
    }

    @Transactional
    public void eliminar(Long id) {
        Especialidad especialidad = especialidadRepository.findById(id)
                .orElseThrow(() -> new ApiExceptions.RecursoNoEncontrado("Especialidad no encontrada: " + id));
        // baja lógica: se desactiva en lugar de borrar para preservar trazabilidad
        especialidad.setActivo(false);
        especialidadRepository.save(especialidad);
    }
}
