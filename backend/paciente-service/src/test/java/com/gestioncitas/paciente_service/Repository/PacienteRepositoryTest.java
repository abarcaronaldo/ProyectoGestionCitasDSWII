package com.gestioncitas.paciente_service.Repository;

import com.gestioncitas.paciente_service.Entity.Paciente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * El borrado es baja lógica (activo=false). El listado debe mostrar solo los activos:
 * un paciente "borrado" no debe seguir apareciendo.
 */
@DataJpaTest
class PacienteRepositoryTest {

    @Autowired
    private PacienteRepository pacienteRepository;

    private Paciente nuevoPaciente(String dni, boolean activo) {
        Paciente p = new Paciente();
        p.setDni(dni);
        p.setNombres("Test");
        p.setApellidos("Paciente");
        p.setActivo(activo);
        return p;
    }

    @Test
    void findByActivoTrue_excluyeLosDadosDeBaja() {
        pacienteRepository.save(nuevoPaciente("70000001", true));
        pacienteRepository.save(nuevoPaciente("70000002", true));
        pacienteRepository.save(nuevoPaciente("70000003", false));

        List<Paciente> activos = pacienteRepository.findByActivoTrue();

        assertThat(activos).hasSize(2);
        assertThat(activos).extracting(Paciente::getDni)
                .containsExactlyInAnyOrder("70000001", "70000002")
                .doesNotContain("70000003");
    }
}
