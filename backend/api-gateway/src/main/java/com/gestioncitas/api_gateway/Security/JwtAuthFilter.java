package com.gestioncitas.api_gateway.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.core.annotation.Order;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Valida el JWT y, si es valido, propaga la identidad real (X-User-Id/X-User-Rol/X-User-Email)
 * a los microservicios internos. En cualquier caso (ruta publica, OPTIONS o ruta protegida)
 * las mismas cabeceras que pudiera traer el cliente original se ignoran/ocultan primero:
 * nadie puede "fingir" ser otro usuario o rol mandando esas cabeceras el mismo (anti-spoofing).
 */
@Component
@Order(2)
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final List<String> RUTAS_PUBLICAS = List.of(
            "/api/auth/", "/actuator/");
    private static final List<String> CABECERAS_DE_IDENTIDAD = List.of("X-User-Id", "X-User-Rol", "X-User-Email");

    private final SecretKey key;

    public JwtAuthFilter(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Permitir preflight CORS y rutas públicas, pero igual sin dejar pasar X-User-* del cliente
        if ("OPTIONS".equalsIgnoreCase(request.getMethod()) || esRutaPublica(request.getRequestURI())) {
            filterChain.doFilter(ocultarCabecerasDeIdentidad(request), response);
            return;
        }

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token ausente");
            return;
        }

        Claims claims;
        try {
            claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(header.substring(7)).getPayload();
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido o expirado");
            return;
        }

        filterChain.doFilter(propagarIdentidad(request, claims), response);
    }

    private boolean esRutaPublica(String uri) {
        return RUTAS_PUBLICAS.stream().anyMatch(uri::startsWith);
    }

    private HttpServletRequest ocultarCabecerasDeIdentidad(HttpServletRequest request) {
        Map<String, String> ocultar = new HashMap<>();
        CABECERAS_DE_IDENTIDAD.forEach(nombre -> ocultar.put(nombre, null));
        return new HeaderOverrideRequestWrapper(request, ocultar);
    }

    private HttpServletRequest propagarIdentidad(HttpServletRequest request, Claims claims) {
        Map<String, String> identidad = new HashMap<>();
        identidad.put("X-User-Id", String.valueOf(claims.get("uid")));
        identidad.put("X-User-Rol", String.valueOf(claims.get("rol")));
        identidad.put("X-User-Email", claims.getSubject());
        return new HeaderOverrideRequestWrapper(request, identidad);
    }
}
