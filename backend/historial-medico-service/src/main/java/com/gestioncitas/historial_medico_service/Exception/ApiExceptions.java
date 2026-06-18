package com.gestioncitas.historial_medico_service.Exception;

/** Excepciones de negocio del servicio de historial medico. */
public final class ApiExceptions {

    private ApiExceptions() {
    }

    public static class RecursoNoEncontrado extends RuntimeException {
        public RecursoNoEncontrado(String mensaje) {
            super(mensaje);
        }
    }

    public static class ReglaNegocio extends RuntimeException {
        public ReglaNegocio(String mensaje) {
            super(mensaje);
        }
    }

    public static class AccesoDenegado extends RuntimeException {
        public AccesoDenegado(String mensaje) {
            super(mensaje);
        }
    }
}
