package com.gestioncitas.historial_medico_service.Client;

import feign.FeignException;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class DoctorClientFallbackFactory implements FallbackFactory<DoctorClient> {

    @Override
    public DoctorClient create(Throwable cause) {
        return usuarioId -> {
            if (cause instanceof FeignException.NotFound notFound) {
                throw notFound;
            }
            throw new ServicioExternoNoDisponibleException("doctor-service");
        };
    }
}
