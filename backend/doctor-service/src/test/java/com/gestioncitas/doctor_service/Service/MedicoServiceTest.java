package com.gestioncitas.doctor_service.Service;

import com.gestioncitas.doctor_service.Entity.Especialidad;
import com.gestioncitas.doctor_service.Entity.Medico;
import com.gestioncitas.doctor_service.Exception.ApiExceptions;
import com.gestioncitas.doctor_service.Repository.EspecialidadRepository;
import com.gestioncitas.doctor_service.Repository.MedicoRepository;
import com.gestioncitas.doctor_service.dto.MedicoCrearDTO;
import com.gestioncitas.doctor_service.dto.MedicoDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pruebas de la lógica de negocio del servicio de médicos (sin BD ni contexto Spring):
 * CMP duplicado, especialidad inexistente y recurso no encontrado.
 */
@ExtendWith(MockitoExtension.class)
class MedicoServiceTest {

    @Mock
    private MedicoRepository medicoRepository;

    @Mock
    private EspecialidadRepository especialidadRepository;

    @InjectMocks
    private MedicoService medicoService;

    private MedicoCrearDTO datosCrear() {
        return new MedicoCrearDTO(20L, "Jorge", "Ramirez", "CMP12345", "999111222", "jorge@correo.pe", 1L);
    }

    private Especialidad especialidad() {
        Especialidad e = new Especialidad();
        e.setId(1L);
        e.setNombre("Cardiología");
        return e;
    }

    @Test
    void crear_conCmpDuplicado_lanzaReglaNegocio() {
        when(medicoRepository.existsByCmp("CMP12345")).thenReturn(true);

        assertThatThrownBy(() -> medicoService.crear(datosCrear()))
                .isInstanceOf(ApiExceptions.ReglaNegocio.class)
                .hasMessageContaining("CMP12345");

        verify(medicoRepository, never()).save(any());
    }

    @Test
    void crear_conEspecialidadInexistente_lanzaRecursoNoEncontrado() {
        when(medicoRepository.existsByCmp("CMP12345")).thenReturn(false);
        when(especialidadRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> medicoService.crear(datosCrear()))
                .isInstanceOf(ApiExceptions.RecursoNoEncontrado.class)
                .hasMessageContaining("1");

        verify(medicoRepository, never()).save(any());
    }

    @Test
    void crear_guardaYDevuelveDTO() {
        when(medicoRepository.existsByCmp("CMP12345")).thenReturn(false);
        when(especialidadRepository.findById(1L)).thenReturn(Optional.of(especialidad()));
        when(medicoRepository.save(any(Medico.class))).thenAnswer(inv -> {
            Medico m = inv.getArgument(0);
            m.setId(7L);
            return m;
        });

        MedicoDTO resultado = medicoService.crear(datosCrear());

        assertThat(resultado.id()).isEqualTo(7L);
        assertThat(resultado.cmp()).isEqualTo("CMP12345");
        assertThat(resultado.especialidadNombre()).isEqualTo("Cardiología");
    }

    @Test
    void obtener_inexistente_lanzaRecursoNoEncontrado() {
        when(medicoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> medicoService.obtener(99L))
                .isInstanceOf(ApiExceptions.RecursoNoEncontrado.class)
                .hasMessageContaining("99");
    }

    @Test
    void eliminar_marcaActivoFalse() {
        Medico medico = new Medico();
        medico.setId(3L);
        medico.setActivo(true);
        when(medicoRepository.findById(3L)).thenReturn(Optional.of(medico));

        medicoService.eliminar(3L);

        assertThat(medico.isActivo()).isFalse();
        verify(medicoRepository).save(medico);
    }
}
