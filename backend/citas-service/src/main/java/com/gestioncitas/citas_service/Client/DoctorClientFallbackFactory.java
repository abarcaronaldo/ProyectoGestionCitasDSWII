package com.gestioncitas.citas_service.Client;

import com.gestioncitas.citas_service.dto.MedicoClienteDTO;
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
                throw fallo();
            }

            @Override
            public MedicoClienteDTO obtenerPorUsuarioId(Long usuarioId) {
                throw fallo();
            }

            private RuntimeException fallo() {
                if (cause instanceof FeignException.NotFound notFound) {
                    return notFound;
                }
                return new ServicioExternoNoDisponibleException("doctor-service");
            }
        };
    }
}
