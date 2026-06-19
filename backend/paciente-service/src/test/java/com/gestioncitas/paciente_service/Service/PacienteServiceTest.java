package com.gestioncitas.paciente_service.Service;

import com.gestioncitas.paciente_service.Entity.Paciente;
import com.gestioncitas.paciente_service.Exception.ApiExceptions;
import com.gestioncitas.paciente_service.Repository.PacienteRepository;
import com.gestioncitas.paciente_service.dto.PacienteActualizarDTO;
import com.gestioncitas.paciente_service.dto.PacienteCrearDTO;
import com.gestioncitas.paciente_service.dto.PacienteDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pruebas de la lógica de negocio del servicio de pacientes (sin BD ni contexto Spring):
 * validación de DNI duplicado, recurso no encontrado y baja lógica.
 */
@ExtendWith(MockitoExtension.class)
class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private PacienteService pacienteService;

    private PacienteCrearDTO datosCrear() {
        return new PacienteCrearDTO(
                10L, "70123456", "Lucia", "Quispe",
                LocalDate.of(1995, 5, 20), "F", "999000111",
                "lucia@correo.pe", "Av. Siempre Viva 123", "O+", "ninguna");
    }

    @Test
    void crear_conDniDuplicado_lanzaReglaNegocio() {
        when(pacienteRepository.existsByDni("70123456")).thenReturn(true);

        assertThatThrownBy(() -> pacienteService.crear(datosCrear()))
                .isInstanceOf(ApiExceptions.ReglaNegocio.class)
                .hasMessageContaining("70123456");

        verify(pacienteRepository, never()).save(any());
    }

    @Test
    void crear_guardaYDevuelveDTO() {
        when(pacienteRepository.existsByDni("70123456")).thenReturn(false);
        when(pacienteRepository.save(any(Paciente.class))).thenAnswer(inv -> {
            Paciente p = inv.getArgument(0);
            p.setId(1L);
            return p;
        });

        PacienteDTO resultado = pacienteService.crear(datosCrear());

        assertThat(resultado.id()).isEqualTo(1L);
        assertThat(resultado.dni()).isEqualTo("70123456");
        assertThat(resultado.nombres()).isEqualTo("Lucia");
        verify(pacienteRepository).save(any(Paciente.class));
    }

    @Test
    void obtener_inexistente_lanzaRecursoNoEncontrado() {
        when(pacienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pacienteService.obtener(99L))
                .isInstanceOf(ApiExceptions.RecursoNoEncontrado.class)
                .hasMessageContaining("99");
    }

    @Test
    void actualizar_inexistente_lanzaRecursoNoEncontrado() {
        PacienteActualizarDTO dto = new PacienteActualizarDTO(
                "Nuevo", "Apellido", LocalDate.of(1990, 1, 1),
                "M", "988877766", "nuevo@correo.pe", "Otra dir", "A+", "polen");
        when(pacienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pacienteService.actualizar(99L, dto))
                .isInstanceOf(ApiExceptions.RecursoNoEncontrado.class);

        verify(pacienteRepository, never()).save(any());
    }

    @Test
    void eliminar_marcaActivoFalseYNoBorra() {
        Paciente paciente = new Paciente();
        paciente.setId(5L);
        paciente.setActivo(true);
        when(pacienteRepository.findById(5L)).thenReturn(Optional.of(paciente));

        pacienteService.eliminar(5L);

        ArgumentCaptor<Paciente> captor = ArgumentCaptor.forClass(Paciente.class);
        verify(pacienteRepository).save(captor.capture());
        assertThat(captor.getValue().isActivo()).isFalse();
        verify(pacienteRepository, never()).delete(any());
    }
}
