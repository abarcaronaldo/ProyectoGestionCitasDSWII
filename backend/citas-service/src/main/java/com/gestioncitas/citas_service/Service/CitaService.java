package com.gestioncitas.citas_service.Service;

import com.gestioncitas.citas_service.Client.DoctorClient;
import com.gestioncitas.citas_service.Client.PacienteClient;
import com.gestioncitas.citas_service.Entity.Cita;
import com.gestioncitas.citas_service.Entity.EstadoCita;
import com.gestioncitas.citas_service.Event.CitaEventPublisher;
import com.gestioncitas.citas_service.Exception.ApiExceptions;
import com.gestioncitas.citas_service.Repository.CitaRepository;
import com.gestioncitas.citas_service.dto.CitaDTO;
import com.gestioncitas.citas_service.dto.CitaEventoDTO;
import com.gestioncitas.citas_service.dto.MedicoClienteDTO;
import com.gestioncitas.citas_service.dto.PacienteClienteDTO;
import com.gestioncitas.citas_service.dto.ReprogramarCitaDTO;
import com.gestioncitas.citas_service.dto.ReservaCitaDTO;
import feign.FeignException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CitaService {

    private static final String HORARIO_OCUPADO = "El médico ya tiene una cita reservada en esa fecha y hora.";
    private static final String CONFLICTO_CONCURRENCIA = "La cita fue modificada por otro proceso. Vuelve a intentarlo.";
    private static final String ROL_PACIENTE = "PACIENTE";
    private static final String ROL_MEDICO = "MEDICO";

    private final CitaRepository citaRepository;
    private final DoctorClient doctorClient;
    private final PacienteClient pacienteClient;
    private final CitaEventPublisher eventPublisher;

    public CitaService(CitaRepository citaRepository, DoctorClient doctorClient, PacienteClient pacienteClient,
                        CitaEventPublisher eventPublisher) {
        this.citaRepository = citaRepository;
        this.doctorClient = doctorClient;
        this.pacienteClient = pacienteClient;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public CitaDTO reservar(ReservaCitaDTO dto) {
        validarMedicoActivo(dto.medicoId());
        validarPacienteActivo(dto.pacienteId());

        if (citaRepository.existsByMedicoIdAndFechaHora(dto.medicoId(), dto.fechaHora())) {
            throw new ApiExceptions.ReglaNegocio(HORARIO_OCUPADO);
        }

        Cita cita = new Cita();
        cita.setPacienteId(dto.pacienteId());
        cita.setMedicoId(dto.medicoId());
        cita.setEspecialidadId(dto.especialidadId());
        cita.setFechaHora(dto.fechaHora());
        cita.setMotivo(dto.motivo());

        CitaDTO citaDTO;
        try {
            citaDTO = CitaDTO.from(citaRepository.save(cita));
        } catch (DataIntegrityViolationException e) {
            // El existsBy de arriba no es atomico: dos peticiones concurrentes pueden pasarlo a la vez.
            // La restriccion UNIQUE de la BD (3.3) es la que realmente garantiza que solo una se guarde;
            // si la perdimos en esa carrera, la traducimos al mismo error de negocio claro.
            throw new ApiExceptions.ReglaNegocio(HORARIO_OCUPADO);
        }
        eventPublisher.publicar(CitaEventPublisher.ROUTING_RESERVADA, CitaEventoDTO.from(citaDTO));
        return citaDTO;
    }

    @Transactional
    public CitaDTO cancelar(Long id) {
        Cita cita = obtenerCitaReservada(id);
        cita.setEstado(EstadoCita.CANCELADA);
        CitaDTO citaDTO = guardarConControlDeConcurrencia(cita);
        eventPublisher.publicar(CitaEventPublisher.ROUTING_CANCELADA, CitaEventoDTO.from(citaDTO));
        return citaDTO;
    }

    @Transactional
    public CitaDTO reprogramar(Long id, ReprogramarCitaDTO dto) {
        Cita cita = obtenerCitaReservada(id);
        if (citaRepository.existsByMedicoIdAndFechaHora(cita.getMedicoId(), dto.fechaHora())) {
            throw new ApiExceptions.ReglaNegocio(HORARIO_OCUPADO);
        }
        cita.setFechaHora(dto.fechaHora());
        return guardarConControlDeConcurrencia(cita);
    }

    @Transactional
    public CitaDTO marcarAsistencia(Long id, boolean asistio) {
        Cita cita = obtenerCitaReservada(id);
        cita.setEstado(asistio ? EstadoCita.ATENDIDA : EstadoCita.NO_ASISTIO);
        CitaDTO citaDTO = guardarConControlDeConcurrencia(cita);
        if (asistio) {
            eventPublisher.publicar(CitaEventPublisher.ROUTING_ATENDIDA, CitaEventoDTO.from(citaDTO));
        }
        return citaDTO;
    }

    @Transactional(readOnly = true)
    public CitaDTO obtener(Long id) {
        return citaRepository.findById(id)
                .map(CitaDTO::from)
                .orElseThrow(() -> new ApiExceptions.RecursoNoEncontrado("Cita no encontrada: " + id));
    }

    @Transactional(readOnly = true)
    public List<CitaDTO> listarPorPaciente(Long pacienteId) {
        return citaRepository.findByPacienteId(pacienteId).stream().map(CitaDTO::from).toList();
    }

    @Transactional(readOnly = true)
    public List<CitaDTO> listarPorMedico(Long medicoId) {
        return citaRepository.findByMedicoId(medicoId).stream().map(CitaDTO::from).toList();
    }

    /** Un PACIENTE solo puede ver sus propias citas; ADMIN/RECEPCIONISTA pueden ver las de cualquiera. */
    @Transactional(readOnly = true)
    public List<CitaDTO> listarPorPacienteVerificandoPropiedad(Long pacienteId, Long usuarioId, String rol) {
        if (ROL_PACIENTE.equals(rol) && !resolverPacienteId(usuarioId).equals(pacienteId)) {
            throw new ApiExceptions.AccesoDenegado("Solo puedes ver tus propias citas.");
        }
        return listarPorPaciente(pacienteId);
    }

    /** Un MEDICO solo puede ver su propia agenda; ADMIN/RECEPCIONISTA pueden ver la de cualquiera. */
    @Transactional(readOnly = true)
    public List<CitaDTO> listarPorMedicoVerificandoPropiedad(Long medicoId, Long usuarioId, String rol) {
        if (ROL_MEDICO.equals(rol) && !resolverMedicoId(usuarioId).equals(medicoId)) {
            throw new ApiExceptions.AccesoDenegado("Solo puedes ver tu propia agenda.");
        }
        return listarPorMedico(medicoId);
    }

    /** Un PACIENTE solo puede cancelar su propia cita; RECEPCIONISTA/ADMIN pueden cancelar cualquiera. */
    @Transactional
    public CitaDTO cancelarVerificandoPropiedad(Long id, Long usuarioId, String rol) {
        if (ROL_PACIENTE.equals(rol)) {
            Cita cita = citaRepository.findById(id)
                    .orElseThrow(() -> new ApiExceptions.RecursoNoEncontrado("Cita no encontrada: " + id));
            if (!cita.getPacienteId().equals(resolverPacienteId(usuarioId))) {
                throw new ApiExceptions.AccesoDenegado("Solo puedes cancelar tus propias citas.");
            }
        }
        return cancelar(id);
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

    private Cita obtenerCitaReservada(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ApiExceptions.RecursoNoEncontrado("Cita no encontrada: " + id));
        if (cita.getEstado() != EstadoCita.RESERVADA) {
            throw new ApiExceptions.ReglaNegocio(
                    "La cita no esta en estado RESERVADA (estado actual: " + cita.getEstado() + ").");
        }
        return cita;
    }

    private CitaDTO guardarConControlDeConcurrencia(Cita cita) {
        try {
            return CitaDTO.from(citaRepository.save(cita));
        } catch (ObjectOptimisticLockingFailureException | DataIntegrityViolationException e) {
            throw new ApiExceptions.ReglaNegocio(CONFLICTO_CONCURRENCIA);
        }
    }

    private void validarMedicoActivo(Long medicoId) {
        MedicoClienteDTO medico;
        try {
            medico = doctorClient.obtenerMedico(medicoId);
        } catch (FeignException.NotFound e) {
            throw new ApiExceptions.RecursoNoEncontrado("Médico no encontrado: " + medicoId);
        }
        if (!medico.activo()) {
            throw new ApiExceptions.ReglaNegocio("El médico no esta activo: " + medicoId);
        }
    }

    private void validarPacienteActivo(Long pacienteId) {
        PacienteClienteDTO paciente;
        try {
            paciente = pacienteClient.obtenerPaciente(pacienteId);
        } catch (FeignException.NotFound e) {
            throw new ApiExceptions.RecursoNoEncontrado("Paciente no encontrado: " + pacienteId);
        }
        if (!paciente.activo()) {
            throw new ApiExceptions.ReglaNegocio("El paciente no esta activo: " + pacienteId);
        }
    }
}
