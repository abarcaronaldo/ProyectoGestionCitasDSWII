package com.gestioncitas.historial_medico_service.Client;

import com.gestioncitas.historial_medico_service.dto.CitaClienteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "citas-service", configuration = FeignRetryConfig.class, fallbackFactory = CitasClientFallbackFactory.class)
public interface CitasClient {

    @GetMapping("/api/citas/{id}")
    CitaClienteDTO obtenerCita(@PathVariable Long id);
}
