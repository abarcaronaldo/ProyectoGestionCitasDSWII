package com.gestioncitas.doctor_service.Config;

import com.gestioncitas.doctor_service.Entity.Especialidad;
import com.gestioncitas.doctor_service.Entity.Medico;
import com.gestioncitas.doctor_service.Repository.EspecialidadRepository;
import com.gestioncitas.doctor_service.Repository.MedicoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Carga especialidades y medicos de prueba si las tablas estan vacias.
 * usuarioId queda en null: aun no hay un mecanismo (Feign) que confirme
 * el id real del usuario en auth-service, asi que no se inventa un valor.
 */
@Component
@Profile("!test")
public class DataSeeder implements CommandLineRunner {

    private final EspecialidadRepository especialidadRepository;
    private final MedicoRepository medicoRepository;

    public DataSeeder(EspecialidadRepository especialidadRepository, MedicoRepository medicoRepository) {
        this.especialidadRepository = especialidadRepository;
        this.medicoRepository = medicoRepository;
    }

    @Override
    public void run(String... args) {
        if (especialidadRepository.count() == 0) {
            Especialidad cardiologia = crearEspecialidad("Cardiología", "Diagnóstico y tratamiento de enfermedades del corazón");
            Especialidad pediatria = crearEspecialidad("Pediatría", "Atención médica de niños y adolescentes");
            Especialidad dermatologia = crearEspecialidad("Dermatología", "Diagnóstico y tratamiento de enfermedades de la piel");

            crearMedico("Jorge", "Ramírez Castro", "CMP12345", "987111333", "jorge.ramirez@gestion.pe", cardiologia);
            crearMedico("Ana", "Torres Villanueva", "CMP23456", "987222444", "ana.torres@gestion.pe", pediatria);
            crearMedico("Luis", "Fernández Quiroz", "CMP34567", "987333555", "luis.fernandez@gestion.pe", dermatologia);
        }
    }

    private Especialidad crearEspecialidad(String nombre, String descripcion) {
        Especialidad e = new Especialidad();
        e.setNombre(nombre);
        e.setDescripcion(descripcion);
        return especialidadRepository.save(e);
    }

    private void crearMedico(String nombres, String apellidos, String cmp, String telefono, String email,
                              Especialidad especialidad) {
        Medico m = new Medico();
        m.setNombres(nombres);
        m.setApellidos(apellidos);
        m.setCmp(cmp);
        m.setTelefono(telefono);
        m.setEmail(email);
        m.setEspecialidad(especialidad);
        medicoRepository.save(m);
    }
}
