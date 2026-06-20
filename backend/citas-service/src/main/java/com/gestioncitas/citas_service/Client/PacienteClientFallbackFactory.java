package com.gestioncitas.citas_service.Client;

import com.gestioncitas.citas_service.dto.PacienteClienteDTO;
import feign.FeignException;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class PacienteClientFallbackFactory implements FallbackFactory<PacienteClient> {

    @Override
    public PacienteClient create(Throwable cause) {
        return new PacienteClient() {
            @Override
            public PacienteClienteDTO obtenerPaciente(Long id) {
                throw fallo();
            }

            @Override
            public PacienteClienteDTO obtenerPorUsuarioId(Long usuarioId) {
                throw fallo();
            }

            private RuntimeException fallo() {
                if (cause instanceof FeignException.NotFound notFound) {
                    return notFound;
                }
                return new ServicioExternoNoDisponibleException("paciente-service");
            }
        };
    }
}
