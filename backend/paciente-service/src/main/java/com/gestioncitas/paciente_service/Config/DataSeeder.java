package com.gestioncitas.paciente_service.Config;

import com.gestioncitas.paciente_service.Entity.Paciente;
import com.gestioncitas.paciente_service.Repository.PacienteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Carga pacientes de prueba si la tabla esta vacia.
 * usuarioId queda en null: aun no hay un mecanismo (Feign) que confirme
 * el id real del usuario en auth-service, asi que no se inventa un valor.
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
            crear("70123456", "Lucía", "Quispe Ramos", LocalDate.of(1995, 3, 12), "F",
                    "987654321", "paciente.Quispe@gestion.pe", "Av. Los Olivos 123, Lima", "O+", "Ninguna conocida");
            crear("71234567", "Carlos", "Huamán Soto", LocalDate.of(1988, 7, 25), "M",
                    "987111222", "paciente.Soto@gestion.pe", "Jr. Las Flores 456, Lima", "A+", "Penicilina");
            crear("72345678", "Rosa", "Mendoza Vila", LocalDate.of(2001, 11, 5), "F",
                    "987999000", "paciente.Mendoza@gestion.pe", "Calle Real 789, Lima", "B-", "Ninguna conocida");
        }
    }

    private void crear(String dni, String nombres, String apellidos, LocalDate fechaNacimiento, String sexo,
                       String telefono, String email, String direccion, String grupoSanguineo, String alergias) {
        Paciente p = new Paciente();
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
