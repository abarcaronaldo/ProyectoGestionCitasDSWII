package com.gestioncitas.reporte_service.Exception;

/** Excepciones de negocio del servicio de reportes. */
public final class ApiExceptions {

    private ApiExceptions() {
    }

    public static class RecursoNoEncontrado extends RuntimeException {
        public RecursoNoEncontrado(String mensaje) {
            super(mensaje);
        }
    }
}
