package com.gestioncitas.doctor_service.Security;

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
                        .requestMatchers(HttpMethod.POST, "/api/medicos/**", "/api/especialidades/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/medicos/**", "/api/especialidades/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/medicos/**", "/api/especialidades/**").hasRole("ADMIN")
                        // listar TODOS los medicos/especialidades: personal de la clinica, y tambien
                        // PACIENTE (necesita elegir medico/especialidad al reservar una cita, 7.3).
                        .requestMatchers(HttpMethod.GET, "/api/medicos", "/api/especialidades").hasAnyRole("ADMIN", "RECEPCIONISTA", "MEDICO", "PACIENTE")
                        .anyRequest().authenticated())
                .addFilterBefore(headerAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
