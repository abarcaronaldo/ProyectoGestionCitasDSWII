package com.gestioncitas.historial_medico_service.Service;

import com.gestioncitas.historial_medico_service.Client.CitasClient;
import com.gestioncitas.historial_medico_service.Entity.Atencion;
import com.gestioncitas.historial_medico_service.Exception.ApiExceptions;
import com.gestioncitas.historial_medico_service.Repository.AtencionRepository;
import com.gestioncitas.historial_medico_service.dto.AtencionActualizarDTO;
import com.gestioncitas.historial_medico_service.dto.AtencionCrearDTO;
import com.gestioncitas.historial_medico_service.dto.AtencionDTO;
import com.gestioncitas.historial_medico_service.dto.CitaClienteDTO;
import feign.FeignException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AtencionService {

    private static final String ESTADO_ATENDIDA = "ATENDIDA";

    private final AtencionRepository atencionRepository;
    private final CitasClient citasClient;

    public AtencionService(AtencionRepository atencionRepository, CitasClient citasClient) {
        this.atencionRepository = atencionRepository;
        this.citasClient = citasClient;
    }

    @Transactional
    public AtencionDTO crear(AtencionCrearDTO dto) {
        if (atencionRepository.existsByCitaId(dto.citaId())) {
            throw new ApiExceptions.ReglaNegocio("Ya existe una atención registrada para la cita: " + dto.citaId());
        }

        CitaClienteDTO cita = obtenerCitaAtendida(dto.citaId());

        Atencion atencion = new Atencion();
        atencion.setCitaId(cita.id());
        atencion.setPacienteId(cita.pacienteId());
        atencion.setMedicoId(cita.medicoId());
        atencion.setFechaAtencion(cita.fechaHora());
        atencion.setMotivoConsulta(dto.motivoConsulta());
        atencion.setDiagnostico(dto.diagnostico());
        atencion.setTratamiento(dto.tratamiento());
        atencion.setObservaciones(dto.observaciones());

        return AtencionDTO.from(atencionRepository.save(atencion));
    }

    @Transactional(readOnly = true)
    public List<AtencionDTO> listar() {
        return atencionRepository.findAll().stream().map(AtencionDTO::from).toList();
    }

    @Transactional(readOnly = true)
    public AtencionDTO obtener(Long id) {
        return atencionRepository.findById(id)
                .map(AtencionDTO::from)
                .orElseThrow(() -> new ApiExceptions.RecursoNoEncontrado("Atención no encontrada: " + id));
    }

    @Transactional(readOnly = true)
    public List<AtencionDTO> listarPorPaciente(Long pacienteId) {
        return atencionRepository.findByPacienteId(pacienteId).stream().map(AtencionDTO::from).toList();
    }

    @Transactional(readOnly = true)
    public List<AtencionDTO> listarPorMedico(Long medicoId) {
        return atencionRepository.findByMedicoId(medicoId).stream().map(AtencionDTO::from).toList();
    }

    @Transactional
    public AtencionDTO actualizar(Long id, AtencionActualizarDTO dto) {
        Atencion atencion = atencionRepository.findById(id)
                .orElseThrow(() -> new ApiExceptions.RecursoNoEncontrado("Atención no encontrada: " + id));
        atencion.setMotivoConsulta(dto.motivoConsulta());
        atencion.setDiagnostico(dto.diagnostico());
        atencion.setTratamiento(dto.tratamiento());
        atencion.setObservaciones(dto.observaciones());
        return AtencionDTO.from(atencionRepository.save(atencion));
    }

    @Transactional
    public void eliminar(Long id) {
        Atencion atencion = atencionRepository.findById(id)
                .orElseThrow(() -> new ApiExceptions.RecursoNoEncontrado("Atención no encontrada: " + id));
        atencion.setActivo(false);
        atencionRepository.save(atencion);
    }

    private CitaClienteDTO obtenerCitaAtendida(Long citaId) {
        CitaClienteDTO cita;
        try {
            cita = citasClient.obtenerCita(citaId);
        } catch (FeignException.NotFound e) {
            throw new ApiExceptions.RecursoNoEncontrado("Cita no encontrada: " + citaId);
        }
        if (!ESTADO_ATENDIDA.equals(cita.estado())) {
            throw new ApiExceptions.ReglaNegocio(
                    "La cita debe estar en estado ATENDIDA para registrar una atención (estado actual: " + cita.estado() + ").");
        }
        return cita;
    }
}
