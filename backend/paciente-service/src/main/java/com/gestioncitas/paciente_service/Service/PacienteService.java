package com.gestioncitas.paciente_service.Service;

import com.gestioncitas.paciente_service.Entity.Paciente;
import com.gestioncitas.paciente_service.Exception.ApiExceptions;
import com.gestioncitas.paciente_service.Repository.PacienteRepository;
import com.gestioncitas.paciente_service.dto.PacienteActualizarDTO;
import com.gestioncitas.paciente_service.dto.PacienteCrearDTO;
import com.gestioncitas.paciente_service.dto.PacienteDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @Transactional
    public PacienteDTO crear(PacienteCrearDTO dto) {
        if (pacienteRepository.existsByDni(dto.dni())) {
            throw new ApiExceptions.ReglaNegocio("Ya existe un paciente registrado con el DNI: " + dto.dni());
        }
        Paciente paciente = new Paciente();
        paciente.setUsuarioId(dto.usuarioId());
        paciente.setDni(dto.dni());
        paciente.setNombres(dto.nombres());
        paciente.setApellidos(dto.apellidos());
        paciente.setFechaNacimiento(dto.fechaNacimiento());
        paciente.setSexo(dto.sexo());
        paciente.setTelefono(dto.telefono());
        paciente.setEmail(dto.email());
        paciente.setDireccion(dto.direccion());
        paciente.setGrupoSanguineo(dto.grupoSanguineo());
        paciente.setAlergias(dto.alergias());
        return PacienteDTO.from(pacienteRepository.save(paciente));
    }

    @Transactional(readOnly = true)
    public List<PacienteDTO> listar() {
        return pacienteRepository.findByActivoTrue().stream().map(PacienteDTO::from).toList();
    }

    @Transactional(readOnly = true)
    public PacienteDTO obtener(Long id) {
        return pacienteRepository.findById(id)
                .map(PacienteDTO::from)
                .orElseThrow(() -> new ApiExceptions.RecursoNoEncontrado("Paciente no encontrado: " + id));
    }

    @Transactional(readOnly = true)
    public PacienteDTO obtenerPorUsuarioId(Long usuarioId) {
        return pacienteRepository.findByUsuarioIdAndActivoTrue(usuarioId)
                .map(PacienteDTO::from)
                .orElseThrow(() -> new ApiExceptions.RecursoNoEncontrado("No hay un paciente asociado al usuario: " + usuarioId));
    }

    @Transactional
    public PacienteDTO actualizar(Long id, PacienteActualizarDTO dto) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ApiExceptions.RecursoNoEncontrado("Paciente no encontrado: " + id));
        paciente.setNombres(dto.nombres());
        paciente.setApellidos(dto.apellidos());
        paciente.setFechaNacimiento(dto.fechaNacimiento());
        paciente.setSexo(dto.sexo());
        paciente.setTelefono(dto.telefono());
        paciente.setEmail(dto.email());
        paciente.setDireccion(dto.direccion());
        paciente.setGrupoSanguineo(dto.grupoSanguineo());
        paciente.setAlergias(dto.alergias());
        return PacienteDTO.from(pacienteRepository.save(paciente));
    }

    @Transactional
    public void eliminar(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ApiExceptions.RecursoNoEncontrado("Paciente no encontrado: " + id));
        // baja lógica: se desactiva en lugar de borrar para preservar trazabilidad
        paciente.setActivo(false);
        pacienteRepository.save(paciente);
    }
}
