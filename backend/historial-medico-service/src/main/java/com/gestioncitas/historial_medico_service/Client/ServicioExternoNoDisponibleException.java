package com.gestioncitas.historial_medico_service.Client;

public class ServicioExternoNoDisponibleException extends RuntimeException {

    public ServicioExternoNoDisponibleException(String servicio) {
        super("El servicio '" + servicio + "' no esta disponible en este momento. Intenta nuevamente mas tarde.");
    }
}
