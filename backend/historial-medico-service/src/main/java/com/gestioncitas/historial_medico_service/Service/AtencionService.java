package com.gestioncitas.historial_medico_service.Service;

import com.gestioncitas.historial_medico_service.Client.CitasClient;
import com.gestioncitas.historial_medico_service.Client.DoctorClient;
import com.gestioncitas.historial_medico_service.Client.PacienteClient;
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
    private static final String ROL_PACIENTE = "PACIENTE";
    private static final String ROL_MEDICO = "MEDICO";

    private final AtencionRepository atencionRepository;
    private final CitasClient citasClient;
    private final PacienteClient pacienteClient;
    private final DoctorClient doctorClient;

    public AtencionService(AtencionRepository atencionRepository, CitasClient citasClient,
                            PacienteClient pacienteClient, DoctorClient doctorClient) {
        this.atencionRepository = atencionRepository;
        this.citasClient = citasClient;
        this.pacienteClient = pacienteClient;
        this.doctorClient = doctorClient;
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
        return atencionRepository.findByActivoTrue().stream().map(AtencionDTO::from).toList();
    }

    @Transactional(readOnly = true)
    public AtencionDTO obtener(Long id) {
        return atencionRepository.findById(id)
                .map(AtencionDTO::from)
                .orElseThrow(() -> new ApiExceptions.RecursoNoEncontrado("Atención no encontrada: " + id));
    }

    @Transactional(readOnly = true)
    public List<AtencionDTO> listarPorPaciente(Long pacienteId) {
        return atencionRepository.findByPacienteIdAndActivoTrue(pacienteId).stream().map(AtencionDTO::from).toList();
    }

    @Transactional(readOnly = true)
    public List<AtencionDTO> listarPorMedico(Long medicoId) {
        return atencionRepository.findByMedicoIdAndActivoTrue(medicoId).stream().map(AtencionDTO::from).toList();
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

    /** "Ver solo lo propio": un PACIENTE/MEDICO solo puede leer atenciones donde el sea el dueño real. */
    @Transactional(readOnly = true)
    public AtencionDTO obtenerVerificandoPropiedad(Long id, Long usuarioId, String rol) {
        Atencion atencion = atencionRepository.findById(id)
                .orElseThrow(() -> new ApiExceptions.RecursoNoEncontrado("Atención no encontrada: " + id));
        verificarPropiedad(atencion, usuarioId, rol);
        return AtencionDTO.from(atencion);
    }

    /** PACIENTE -> sus atenciones como paciente. MEDICO -> las que el atendio. */
    @Transactional(readOnly = true)
    public List<AtencionDTO> listarMisAtenciones(Long usuarioId, String rol) {
        if (ROL_PACIENTE.equals(rol)) {
            return listarPorPaciente(resolverPacienteId(usuarioId));
        }
        if (ROL_MEDICO.equals(rol)) {
            return listarPorMedico(resolverMedicoId(usuarioId));
        }
        throw new ApiExceptions.ReglaNegocio("El rol '" + rol + "' no tiene atenciones propias que listar.");
    }

    /** Un PACIENTE solo puede listar su propio historial; ADMIN/RECEPCIONISTA pueden listar el de cualquiera. */
    @Transactional(readOnly = true)
    public List<AtencionDTO> listarPorPacienteVerificandoPropiedad(Long pacienteId, Long usuarioId, String rol) {
        if (ROL_PACIENTE.equals(rol) && !resolverPacienteId(usuarioId).equals(pacienteId)) {
            throw new ApiExceptions.AccesoDenegado("Solo puedes ver tu propio historial.");
        }
        return listarPorPaciente(pacienteId);
    }

    /** Un MEDICO solo puede listar su propia agenda clinica; ADMIN/RECEPCIONISTA pueden listar la de cualquiera. */
    @Transactional(readOnly = true)
    public List<AtencionDTO> listarPorMedicoVerificandoPropiedad(Long medicoId, Long usuarioId, String rol) {
        if (ROL_MEDICO.equals(rol) && !resolverMedicoId(usuarioId).equals(medicoId)) {
            throw new ApiExceptions.AccesoDenegado("Solo puedes ver tus propias atenciones como médico.");
        }
        return listarPorMedico(medicoId);
    }

    /** Solo el medico que atendio (o un admin) puede corregir el diagnostico/tratamiento registrado. */
    @Transactional
    public AtencionDTO actualizarVerificandoPropiedad(Long id, AtencionActualizarDTO dto, Long usuarioId, String rol) {
        Atencion atencion = atencionRepository.findById(id)
                .orElseThrow(() -> new ApiExceptions.RecursoNoEncontrado("Atención no encontrada: " + id));
        if (ROL_MEDICO.equals(rol) && !resolverMedicoId(usuarioId).equals(atencion.getMedicoId())) {
            throw new ApiExceptions.AccesoDenegado("Solo el médico que registró la atención puede editarla.");
        }
        return actualizar(id, dto);
    }

    private void verificarPropiedad(Atencion atencion, Long usuarioId, String rol) {
        if (ROL_PACIENTE.equals(rol) && !resolverPacienteId(usuarioId).equals(atencion.getPacienteId())) {
            throw new ApiExceptions.AccesoDenegado("Solo puedes ver tus propias atenciones.");
        }
        if (ROL_MEDICO.equals(rol) && !resolverMedicoId(usuarioId).equals(atencion.getMedicoId())) {
            throw new ApiExceptions.AccesoDenegado("Solo puedes ver las atenciones que tú registraste.");
        }
    }

    private Long resolverPacienteId(Long usuarioId) {
        try {
            return pacienteClient.obtenerPorUsuarioId(usuarioId).id();
        } catch (FeignException.NotFound e) {
            throw new ApiExceptions.RecursoNoEncontrado("No hay un paciente asociado al usuario: " + usuarioId);
        }
    }

    private Long resolverMedicoId(Long usuarioId) {
        try {
            return doctorClient.obtenerPorUsuarioId(usuarioId).id();
        } catch (FeignException.NotFound e) {
            throw new ApiExceptions.RecursoNoEncontrado("No hay un médico asociado al usuario: " + usuarioId);
        }
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
