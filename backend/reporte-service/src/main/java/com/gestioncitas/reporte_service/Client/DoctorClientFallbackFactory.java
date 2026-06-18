package com.gestioncitas.reporte_service.Client;

import com.gestioncitas.reporte_service.dto.EspecialidadClienteDTO;
import com.gestioncitas.reporte_service.dto.MedicoClienteDTO;
import feign.FeignException;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class DoctorClientFallbackFactory implements FallbackFactory<DoctorClient> {

    @Override
    public DoctorClient create(Throwable cause) {
        return new DoctorClient() {
            @Override
            public MedicoClienteDTO obtenerMedico(Long id) {
                throw traducir(cause);
            }

            @Override
            public EspecialidadClienteDTO obtenerEspecialidad(Long id) {
                throw traducir(cause);
            }
        };
    }

    private RuntimeException traducir(Throwable cause) {
        if (cause instanceof FeignException.NotFound notFound) {
            return notFound;
        }
        return new ServicioExternoNoDisponibleException("doctor-service");
    }
}
