package com.gestioncitas.citas_service.Client;

import com.gestioncitas.citas_service.dto.MedicoClienteDTO;
import org.springframework.stereotype.Component;

@Component
public class DoctorClientFallback implements DoctorClient {

    @Override
    public MedicoClienteDTO obtenerMedico(Long id) {
        throw new ServicioExternoNoDisponibleException("doctor-service");
    }
}
