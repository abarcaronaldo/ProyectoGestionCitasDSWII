package com.gestioncitas.doctor_service.Service;

import com.gestioncitas.doctor_service.Entity.Especialidad;
import com.gestioncitas.doctor_service.Entity.Medico;
import com.gestioncitas.doctor_service.Exception.ApiExceptions;
import com.gestioncitas.doctor_service.Repository.EspecialidadRepository;
import com.gestioncitas.doctor_service.Repository.MedicoRepository;
import com.gestioncitas.doctor_service.dto.MedicoActualizarDTO;
import com.gestioncitas.doctor_service.dto.MedicoCrearDTO;
import com.gestioncitas.doctor_service.dto.MedicoDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MedicoService {

    private final MedicoRepository medicoRepository;
    private final EspecialidadRepository especialidadRepository;

    public MedicoService(MedicoRepository medicoRepository, EspecialidadRepository especialidadRepository) {
        this.medicoRepository = medicoRepository;
        this.especialidadRepository = especialidadRepository;
    }

    @Transactional
    public MedicoDTO crear(MedicoCrearDTO dto) {
        if (medicoRepository.existsByCmp(dto.cmp())) {
            throw new ApiExceptions.ReglaNegocio("Ya existe un médico registrado con el CMP: " + dto.cmp());
        }
        Especialidad especialidad = obtenerEspecialidad(dto.especialidadId());
        Medico medico = new Medico();
        medico.setUsuarioId(dto.usuarioId());
        medico.setNombres(dto.nombres());
        medico.setApellidos(dto.apellidos());
        medico.setCmp(dto.cmp());
        medico.setTelefono(dto.telefono());
        medico.setEmail(dto.email());
        medico.setEspecialidad(especialidad);
        return MedicoDTO.from(medicoRepository.save(medico));
    }

    @Transactional(readOnly = true)
    public List<MedicoDTO> listar() {
        return medicoRepository.findByActivoTrue().stream().map(MedicoDTO::from).toList();
    }

    @Transactional(readOnly = true)
    public MedicoDTO obtener(Long id) {
        return medicoRepository.findById(id)
                .map(MedicoDTO::from)
                .orElseThrow(() -> new ApiExceptions.RecursoNoEncontrado("Médico no encontrado: " + id));
    }

    @Transactional(readOnly = true)
    public List<MedicoDTO> listarPorEspecialidad(Long especialidadId) {
        return medicoRepository.findByEspecialidadIdAndActivoTrue(especialidadId).stream().map(MedicoDTO::from).toList();
    }

    @Transactional(readOnly = true)
    public MedicoDTO obtenerPorUsuarioId(Long usuarioId) {
        return medicoRepository.findByUsuarioId(usuarioId)
                .map(MedicoDTO::from)
                .orElseThrow(() -> new ApiExceptions.RecursoNoEncontrado("No hay un médico asociado al usuario: " + usuarioId));
    }

    @Transactional
    public MedicoDTO actualizar(Long id, MedicoActualizarDTO dto) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new ApiExceptions.RecursoNoEncontrado("Médico no encontrado: " + id));
        Especialidad especialidad = obtenerEspecialidad(dto.especialidadId());
        medico.setNombres(dto.nombres());
        medico.setApellidos(dto.apellidos());
        medico.setTelefono(dto.telefono());
        medico.setEmail(dto.email());
        medico.setEspecialidad(especialidad);
        return MedicoDTO.from(medicoRepository.save(medico));
    }

    @Transactional
    public void eliminar(Long id) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new ApiExceptions.RecursoNoEncontrado("Médico no encontrado: " + id));
        // baja lógica: se desactiva en lugar de borrar para preservar trazabilidad
        medico.setActivo(false);
        medicoRepository.save(medico);
    }

    private Especialidad obtenerEspecialidad(Long especialidadId) {
        return especialidadRepository.findById(especialidadId)
                .orElseThrow(() -> new ApiExceptions.RecursoNoEncontrado("Especialidad no encontrada: " + especialidadId));
    }
}
