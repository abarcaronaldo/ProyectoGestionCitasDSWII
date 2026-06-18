package com.gestioncitas.citas_service.Client;

import com.gestioncitas.citas_service.dto.MedicoClienteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "doctor-service", configuration = FeignRetryConfig.class, fallbackFactory = DoctorClientFallbackFactory.class)
public interface DoctorClient {

    @GetMapping("/api/medicos/{id}")
    MedicoClienteDTO obtenerMedico(@PathVariable Long id);
}
