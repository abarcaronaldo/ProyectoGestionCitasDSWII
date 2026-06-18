package com.gestioncitas.citas_service.Client;

import feign.FeignException;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class PacienteClientFallbackFactory implements FallbackFactory<PacienteClient> {

    @Override
    public PacienteClient create(Throwable cause) {
        return id -> {
            if (cause instanceof FeignException.NotFound notFound) {
                throw notFound;
            }
            throw new ServicioExternoNoDisponibleException("paciente-service");
        };
    }
}
