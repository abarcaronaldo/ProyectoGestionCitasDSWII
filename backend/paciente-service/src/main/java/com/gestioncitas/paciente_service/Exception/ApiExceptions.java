package com.gestioncitas.paciente_service.Exception;

/** Excepciones de negocio del servicio de pacientes. */
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
}
