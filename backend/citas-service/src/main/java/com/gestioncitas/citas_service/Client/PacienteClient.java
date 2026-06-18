package com.gestioncitas.citas_service.Client;

import com.gestioncitas.citas_service.dto.PacienteClienteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "paciente-service", configuration = FeignRetryConfig.class, fallbackFactory = PacienteClientFallbackFactory.class)
public interface PacienteClient {

    @GetMapping("/api/pacientes/{id}")
    PacienteClienteDTO obtenerPaciente(@PathVariable Long id);
}
