package com.gestioncitas.citas_service.Client;

import com.gestioncitas.citas_service.dto.PacienteClienteDTO;
import org.springframework.stereotype.Component;

@Component
public class PacienteClientFallback implements PacienteClient {

    @Override
    public PacienteClienteDTO obtenerPaciente(Long id) {
        throw new ServicioExternoNoDisponibleException("paciente-service");
    }
}
