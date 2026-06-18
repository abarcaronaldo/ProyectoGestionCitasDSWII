package com.gestioncitas.historial_medico_service.Client;

import com.gestioncitas.historial_medico_service.dto.MedicoClienteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "doctor-service", configuration = FeignRetryConfig.class, fallbackFactory = DoctorClientFallbackFactory.class)
public interface DoctorClient {

    @GetMapping("/api/medicos/usuario/{usuarioId}")
    MedicoClienteDTO obtenerPorUsuarioId(@PathVariable Long usuarioId);
}
