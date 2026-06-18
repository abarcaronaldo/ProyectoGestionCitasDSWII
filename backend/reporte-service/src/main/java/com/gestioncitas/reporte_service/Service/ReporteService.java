package com.gestioncitas.reporte_service.Service;

import com.gestioncitas.reporte_service.Client.DoctorClient;
import com.gestioncitas.reporte_service.Exception.ApiExceptions;
import com.gestioncitas.reporte_service.Repository.MetricaCitaRepository;
import com.gestioncitas.reporte_service.dto.EspecialidadClienteDTO;
import com.gestioncitas.reporte_service.dto.MedicoClienteDTO;
import com.gestioncitas.reporte_service.dto.ResumenEspecialidadDTO;
import com.gestioncitas.reporte_service.dto.ResumenGeneralDTO;
import com.gestioncitas.reporte_service.dto.ResumenMedicoDTO;
import feign.FeignException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReporteService {

    private static final String RESERVADA = "RESERVADA";
    private static final String ATENDIDA = "ATENDIDA";
    private static final String CANCELADA = "CANCELADA";
    private static final String NO_ASISTIO = "NO_ASISTIO";

    private final MetricaCitaRepository metricaCitaRepository;
    private final DoctorClient doctorClient;

    public ReporteService(MetricaCitaRepository metricaCitaRepository, DoctorClient doctorClient) {
        this.metricaCitaRepository = metricaCitaRepository;
        this.doctorClient = doctorClient;
    }

    @Transactional(readOnly = true)
    public ResumenGeneralDTO resumenGeneral() {
        long reservadas = metricaCitaRepository.countByEstado(RESERVADA);
        long atendidas = metricaCitaRepository.countByEstado(ATENDIDA);
        long canceladas = metricaCitaRepository.countByEstado(CANCELADA);
        long noAsistio = metricaCitaRepository.countByEstado(NO_ASISTIO);
        return new ResumenGeneralDTO(reservadas, atendidas, canceladas, noAsistio,
                reservadas + atendidas + canceladas + noAsistio);
    }

    @Transactional(readOnly = true)
    public ResumenMedicoDTO resumenPorMedico(Long medicoId) {
        MedicoClienteDTO medico = obtenerMedico(medicoId);
        long reservadas = metricaCitaRepository.countByMedicoIdAndEstado(medicoId, RESERVADA);
        long atendidas = metricaCitaRepository.countByMedicoIdAndEstado(medicoId, ATENDIDA);
        long canceladas = metricaCitaRepository.countByMedicoIdAndEstado(medicoId, CANCELADA);
        long noAsistio = metricaCitaRepository.countByMedicoIdAndEstado(medicoId, NO_ASISTIO);
        return new ResumenMedicoDTO(medicoId, medico.nombres() + " " + medico.apellidos(),
                reservadas, atendidas, canceladas, noAsistio, reservadas + atendidas + canceladas + noAsistio);
    }

    @Transactional(readOnly = true)
    public ResumenEspecialidadDTO resumenPorEspecialidad(Long especialidadId) {
        EspecialidadClienteDTO especialidad = obtenerEspecialidad(especialidadId);
        long reservadas = metricaCitaRepository.countByEspecialidadIdAndEstado(especialidadId, RESERVADA);
        long atendidas = metricaCitaRepository.countByEspecialidadIdAndEstado(especialidadId, ATENDIDA);
        long canceladas = metricaCitaRepository.countByEspecialidadIdAndEstado(especialidadId, CANCELADA);
        long noAsistio = metricaCitaRepository.countByEspecialidadIdAndEstado(especialidadId, NO_ASISTIO);
        return new ResumenEspecialidadDTO(especialidadId, especialidad.nombre(),
                reservadas, atendidas, canceladas, noAsistio, reservadas + atendidas + canceladas + noAsistio);
    }

    private MedicoClienteDTO obtenerMedico(Long medicoId) {
        try {
            return doctorClient.obtenerMedico(medicoId);
        } catch (FeignException.NotFound e) {
            throw new ApiExceptions.RecursoNoEncontrado("Médico no encontrado: " + medicoId);
        }
    }

    private EspecialidadClienteDTO obtenerEspecialidad(Long especialidadId) {
        try {
            return doctorClient.obtenerEspecialidad(especialidadId);
        } catch (FeignException.NotFound e) {
            throw new ApiExceptions.RecursoNoEncontrado("Especialidad no encontrada: " + especialidadId);
        }
    }
}
