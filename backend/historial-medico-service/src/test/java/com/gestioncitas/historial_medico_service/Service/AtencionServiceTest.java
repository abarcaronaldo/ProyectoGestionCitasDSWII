package com.gestioncitas.historial_medico_service.Service;

import com.gestioncitas.historial_medico_service.Client.CitasClient;
import com.gestioncitas.historial_medico_service.Client.DoctorClient;
import com.gestioncitas.historial_medico_service.Client.PacienteClient;
import com.gestioncitas.historial_medico_service.Entity.Atencion;
import com.gestioncitas.historial_medico_service.Exception.ApiExceptions;
import com.gestioncitas.historial_medico_service.Repository.AtencionRepository;
import com.gestioncitas.historial_medico_service.dto.AtencionCrearDTO;
import com.gestioncitas.historial_medico_service.dto.AtencionDTO;
import com.gestioncitas.historial_medico_service.dto.CitaClienteDTO;
import com.gestioncitas.historial_medico_service.dto.MedicoClienteDTO;
import com.gestioncitas.historial_medico_service.dto.PacienteClienteDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pruebas de la lógica del servicio de atenciones: registro contra una cita ATENDIDA,
 * duplicados y las reglas de "ver solo lo propio" para paciente y médico.
 */
@ExtendWith(MockitoExtension.class)
class AtencionServiceTest {

    @Mock
    private AtencionRepository atencionRepository;
    @Mock
    private CitasClient citasClient;
    @Mock
    private PacienteClient pacienteClient;
    @Mock
    private DoctorClient doctorClient;

    @InjectMocks
    private AtencionService atencionService;

    private AtencionCrearDTO datosCrear() {
        return new AtencionCrearDTO(50L, "Dolor de cabeza", "Migraña", "Reposo", "Sin observaciones");
    }

    private CitaClienteDTO cita(String estado) {
        return new CitaClienteDTO(50L, 1L, 2L, LocalDateTime.now(), estado, "consulta");
    }

    private Atencion atencion(Long pacienteId, Long medicoId) {
        Atencion a = new Atencion();
        a.setId(100L);
        a.setPacienteId(pacienteId);
        a.setMedicoId(medicoId);
        return a;
    }

    @Test
    void crear_conCitaYaAtendidaRegistrada_lanzaReglaNegocio() {
        when(atencionRepository.existsByCitaId(50L)).thenReturn(true);

        assertThatThrownBy(() -> atencionService.crear(datosCrear()))
                .isInstanceOf(ApiExceptions.ReglaNegocio.class)
                .hasMessageContaining("50");

        verify(atencionRepository, never()).save(any());
    }

    @Test
    void crear_conCitaNoAtendida_lanzaReglaNegocio() {
        when(atencionRepository.existsByCitaId(50L)).thenReturn(false);
        when(citasClient.obtenerCita(50L)).thenReturn(cita("RESERVADA"));

        assertThatThrownBy(() -> atencionService.crear(datosCrear()))
                .isInstanceOf(ApiExceptions.ReglaNegocio.class)
                .hasMessageContaining("ATENDIDA");

        verify(atencionRepository, never()).save(any());
    }

    @Test
    void crear_conCitaAtendida_guardaCopiandoDatosDeLaCita() {
        when(atencionRepository.existsByCitaId(50L)).thenReturn(false);
        when(citasClient.obtenerCita(50L)).thenReturn(cita("ATENDIDA"));
        when(atencionRepository.save(any(Atencion.class))).thenAnswer(inv -> {
            Atencion a = inv.getArgument(0);
            a.setId(100L);
            return a;
        });

        AtencionDTO resultado = atencionService.crear(datosCrear());

        assertThat(resultado.citaId()).isEqualTo(50L);
        assertThat(resultado.pacienteId()).isEqualTo(1L);
        assertThat(resultado.medicoId()).isEqualTo(2L);
        assertThat(resultado.diagnostico()).isEqualTo("Migraña");
    }

    @Test
    void obtenerVerificandoPropiedad_pacienteAjeno_lanzaAccesoDenegado() {
        when(atencionRepository.findById(100L)).thenReturn(Optional.of(atencion(1L, 2L)));
        // El usuario que pregunta resuelve al paciente 9, distinto del dueño (1)
        when(pacienteClient.obtenerPorUsuarioId(77L)).thenReturn(new PacienteClienteDTO(9L, true));

        assertThatThrownBy(() -> atencionService.obtenerVerificandoPropiedad(100L, 77L, "PACIENTE"))
                .isInstanceOf(ApiExceptions.AccesoDenegado.class);
    }

    @Test
    void obtenerVerificandoPropiedad_medicoDueño_devuelveLaAtencion() {
        when(atencionRepository.findById(100L)).thenReturn(Optional.of(atencion(1L, 2L)));
        when(doctorClient.obtenerPorUsuarioId(88L)).thenReturn(new MedicoClienteDTO(2L, true));

        AtencionDTO resultado = atencionService.obtenerVerificandoPropiedad(100L, 88L, "MEDICO");

        assertThat(resultado.id()).isEqualTo(100L);
    }

    @Test
    void listarMisAtenciones_rolSinAtenciones_lanzaReglaNegocio() {
        assertThatThrownBy(() -> atencionService.listarMisAtenciones(5L, "RECEPCIONISTA"))
                .isInstanceOf(ApiExceptions.ReglaNegocio.class);
    }
}
