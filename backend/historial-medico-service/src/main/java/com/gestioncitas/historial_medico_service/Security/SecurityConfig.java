package com.gestioncitas.historial_medico_service.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final HeaderAuthFilter headerAuthFilter;

    public SecurityConfig(HeaderAuthFilter headerAuthFilter) {
        this.headerAuthFilter = headerAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**").permitAll()
                        // crear: la registra el medico que atendio la cita
                        .requestMatchers(HttpMethod.POST, "/api/atenciones").hasRole("MEDICO")
                        // editar: el propio medico (verificado en el Service) o un admin
                        .requestMatchers(HttpMethod.PUT, "/api/atenciones/**").hasAnyRole("MEDICO", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/atenciones/**").hasRole("ADMIN")
                        // listar TODAS las atenciones: solo personal administrativo
                        .requestMatchers(HttpMethod.GET, "/api/atenciones").hasAnyRole("ADMIN", "RECEPCIONISTA")
                        // el resto de GET (por id, por paciente, por medico, mis-atenciones) exige autenticacion;
                        // el filtro fino de "ver solo lo propio" lo aplica AtencionService segun el rol.
                        .requestMatchers(HttpMethod.GET, "/api/atenciones/**").authenticated()
                        .anyRequest().authenticated())
                .addFilterBefore(headerAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
