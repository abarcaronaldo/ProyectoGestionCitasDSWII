package com.gestioncitas.api_gateway.Security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Envuelve la peticion para forzar el valor de ciertas cabeceras (o esconderlas si el
 * valor es null), sin importar lo que haya mandado el cliente original. Es la pieza que
 * hace que un microservicio interno solo vea la identidad que el gateway decidio, nunca
 * la que el cliente intento inyectar el mismo (anti-spoofing).
 */
public class HeaderOverrideRequestWrapper extends HttpServletRequestWrapper {

    private final Map<String, String> overridesPorNombreEnMinuscula = new HashMap<>();

    public HeaderOverrideRequestWrapper(HttpServletRequest request, Map<String, String> overrides) {
        super(request);
        overrides.forEach((nombre, valor) -> overridesPorNombreEnMinuscula.put(nombre.toLowerCase(), valor));
    }

    @Override
    public String getHeader(String name) {
        String clave = name.toLowerCase();
        if (overridesPorNombreEnMinuscula.containsKey(clave)) {
            return overridesPorNombreEnMinuscula.get(clave);
        }
        return super.getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        String clave = name.toLowerCase();
        if (overridesPorNombreEnMinuscula.containsKey(clave)) {
            String valor = overridesPorNombreEnMinuscula.get(clave);
            return valor == null ? Collections.emptyEnumeration() : Collections.enumeration(List.of(valor));
        }
        return super.getHeaders(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        Set<String> nombres = new LinkedHashSet<>();
        Enumeration<String> originales = super.getHeaderNames();
        while (originales.hasMoreElements()) {
            String nombre = originales.nextElement();
            if (!overridesPorNombreEnMinuscula.containsKey(nombre.toLowerCase())) {
                nombres.add(nombre);
            }
        }
        overridesPorNombreEnMinuscula.forEach((nombre, valor) -> {
            if (valor != null) {
                nombres.add(nombre);
            }
        });
        return Collections.enumeration(nombres);
    }
}
