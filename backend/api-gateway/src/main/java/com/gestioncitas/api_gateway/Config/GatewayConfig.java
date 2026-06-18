package com.gestioncitas.api_gateway.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions.lb;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.web.servlet.function.RequestPredicates.path;

@Configuration
public class GatewayConfig {

    @Bean
    public RouterFunction<ServerResponse> authServerResponseRoutes(){
        return route("auth-service")
                .route(path("/api/auth/**"), http())
                .route(path("/api/usuarios/**"), http())
                .filter(lb("auth-service"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> pacienteServerResponseRoutes(){
        return route("paciente-service")
                .route(path("/api/pacientes/**"), http())
                .filter(lb("paciente-service"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> doctorServerResponseRoutes(){
        return route("doctor-service")
                .route(path("/api/medicos/**"), http())
                .route(path("/api/especialidades/**"), http())
                .filter(lb("doctor-service"))
                .build();
    }

}
