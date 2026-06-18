package com.gestioncitas.historial_medico_service.Client;

import com.gestioncitas.historial_medico_service.dto.PacienteClienteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "paciente-service", configuration = FeignRetryConfig.class, fallbackFactory = PacienteClientFallbackFactory.class)
public interface PacienteClient {

    @GetMapping("/api/pacientes/usuario/{usuarioId}")
    PacienteClienteDTO obtenerPorUsuarioId(@PathVariable Long usuarioId);
}
