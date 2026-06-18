package com.gestioncitas.historial_medico_service.Client;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Propaga las cabeceras de identidad (X-User-Id/X-User-Rol/X-User-Email) de la peticion
 * entrante hacia las llamadas Feign salientes, para que paciente/doctor/citas-service
 * puedan autenticar la llamada con su propio HeaderAuthFilter.
 */
@Configuration
public class FeignHeaderForwardConfig {

    private static final String[] CABECERAS_A_PROPAGAR = {"X-User-Id", "X-User-Rol", "X-User-Email"};

    @Bean
    public RequestInterceptor headerForwardingInterceptor() {
        return template -> {
            if (!(RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attrs)) {
                return;
            }
            HttpServletRequest request = attrs.getRequest();
            for (String nombre : CABECERAS_A_PROPAGAR) {
                String valor = request.getHeader(nombre);
                if (valor != null) {
                    template.header(nombre, valor);
                }
            }
        };
    }
}
