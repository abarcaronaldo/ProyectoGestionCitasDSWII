package com.gestioncitas.historial_medico_service.Client;

import feign.FeignException;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class CitasClientFallbackFactory implements FallbackFactory<CitasClient> {

    @Override
    public CitasClient create(Throwable cause) {
        return id -> {
            if (cause instanceof FeignException.NotFound notFound) {
                throw notFound;
            }
            throw new ServicioExternoNoDisponibleException("citas-service");
        };
    }
}
