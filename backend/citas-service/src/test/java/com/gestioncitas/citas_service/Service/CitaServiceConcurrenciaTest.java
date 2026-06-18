package com.gestioncitas.citas_service.Service;

import com.gestioncitas.citas_service.Client.DoctorClient;
import com.gestioncitas.citas_service.Client.PacienteClient;
import com.gestioncitas.citas_service.Event.CitaEventPublisher;
import com.gestioncitas.citas_service.Exception.ApiExceptions;
import com.gestioncitas.citas_service.dto.MedicoClienteDTO;
import com.gestioncitas.citas_service.dto.PacienteClienteDTO;
import com.gestioncitas.citas_service.dto.ReservaCitaDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * Evidencia de la issue 3.11: dos reservas simultaneas al mismo medico/hora,
 * solo una debe tener exito (la restriccion UNIQUE de la 3.3 + el manejo de
 * la 3.8 son los que realmente lo garantizan bajo concurrencia real).
 */
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CitaServiceConcurrenciaTest {

    @Autowired
    private CitaService citaService;

    @MockitoBean
    private DoctorClient doctorClient;

    @MockitoBean
    private PacienteClient pacienteClient;

    @MockitoBean
    private CitaEventPublisher eventPublisher;

    @BeforeEach
    void setUp() {
        when(doctorClient.obtenerMedico(anyLong()))
                .thenReturn(new MedicoClienteDTO(1L, "Jorge", "Ramirez", 1L, true));
        when(pacienteClient.obtenerPaciente(anyLong()))
                .thenReturn(new PacienteClienteDTO(1L, "Lucia", "Quispe", true));
    }

    @Test
    void dosReservasSimultaneasAlMismoMedicoYHora_soloUnaTieneExito() throws InterruptedException {
        LocalDateTime fechaHora = LocalDateTime.now().plusDays(5).withHour(9).withMinute(0).withSecond(0).withNano(0);
        int hilos = 2;

        ExecutorService executor = Executors.newFixedThreadPool(hilos);
        CountDownLatch listos = new CountDownLatch(hilos);
        AtomicInteger exitos = new AtomicInteger();
        AtomicInteger rechazos = new AtomicInteger();

        for (int i = 0; i < hilos; i++) {
            long pacienteId = i + 1L;
            executor.submit(() -> {
                try {
                    citaService.reservar(new ReservaCitaDTO(pacienteId, 1L, 1L, fechaHora, "concurrencia"));
                    exitos.incrementAndGet();
                } catch (ApiExceptions.ReglaNegocio e) {
                    rechazos.incrementAndGet();
                } finally {
                    listos.countDown();
                }
            });
        }

        boolean terminaron = listos.await(10, TimeUnit.SECONDS);
        executor.shutdown();

        assertThat(terminaron).isTrue();
        assertThat(exitos.get()).isEqualTo(1);
        assertThat(rechazos.get()).isEqualTo(1);
    }
}
