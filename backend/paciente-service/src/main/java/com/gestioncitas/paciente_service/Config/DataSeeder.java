package com.gestioncitas.paciente_service.Config;

import com.gestioncitas.paciente_service.Entity.Paciente;
import com.gestioncitas.paciente_service.Repository.PacienteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Carga pacientes de prueba si la tabla esta vacia.
 * Algunos pacientes se enlazan a su cuenta demo del auth-service via usuarioId
 * (ids fijos del seeder de auth) para que el paciente pueda reservar y ver sus citas.
 */
@Component
@Profile("!test")
public class DataSeeder implements CommandLineRunner {

    private final PacienteRepository pacienteRepository;

    public DataSeeder(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @Override
    public void run(String... args) {
        if (pacienteRepository.count() == 0) {
            // usuarioId enlaza el paciente con su cuenta del auth-service (ids demo del seeder de auth:
            // 1 = paciente.Quispe, 2 = paciente.Soto). Asi GET /api/pacientes/usuario/{id} encuentra
            // el paciente al iniciar sesion (necesario para reservar y ver sus citas). Mendoza no tiene cuenta.
            crear(1L, "70123456", "Lucía", "Quispe Ramos", LocalDate.of(1995, 3, 12), "F",
                    "987654321", "paciente.Quispe@gestion.pe", "Av. Los Olivos 123, Lima", "O+", "Ninguna conocida");
            crear(2L, "71234567", "Carlos", "Huamán Soto", LocalDate.of(1988, 7, 25), "M",
                    "987111222", "paciente.Soto@gestion.pe", "Jr. Las Flores 456, Lima", "A+", "Penicilina");
            crear(null, "72345678", "Rosa", "Mendoza Vila", LocalDate.of(2001, 11, 5), "F",
                    "987999000", "paciente.Mendoza@gestion.pe", "Calle Real 789, Lima", "B-", "Ninguna conocida");
        }
    }

    private void crear(Long usuarioId, String dni, String nombres, String apellidos, LocalDate fechaNacimiento, String sexo,
                       String telefono, String email, String direccion, String grupoSanguineo, String alergias) {
        Paciente p = new Paciente();
        p.setUsuarioId(usuarioId);
        p.setDni(dni);
        p.setNombres(nombres);
        p.setApellidos(apellidos);
        p.setFechaNacimiento(fechaNacimiento);
        p.setSexo(sexo);
        p.setTelefono(telefono);
        p.setEmail(email);
        p.setDireccion(direccion);
        p.setGrupoSanguineo(grupoSanguineo);
        p.setAlergias(alergias);
        pacienteRepository.save(p);
    }
}
