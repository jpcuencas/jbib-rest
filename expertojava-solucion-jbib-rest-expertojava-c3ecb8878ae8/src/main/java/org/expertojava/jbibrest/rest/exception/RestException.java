package org.expertojava.jbibrest.rest.exception;


public class RestException extends RuntimeException {
    public static final java.lang.String LIBRO_NO_EXISTENTE =
            "Libro no existente";

    public static final String BUSQUEDA_POR_TITULO_Y_AUTOR_NO_IMPLEMENTADA =
            "Búsqueda por título y autor no implementada";

    public static final String USUARIO_NO_EXISTENTE =
            "Usuario no existente";

    public static final String USUARIO_NO_ES_EL_LOGUEADO =
            "El usuario no es el logueado";
            /*
    public static final String AUTORIZADO_SOLO_A_BIBLIOTECARIOS =
            "Autorizado sólo a bibliotecarios";

     */

    public RestException() {
        super();
    }

    public RestException(String message) {
        super(message);
    }

    public RestException(String message, Throwable cause) {
        super(message, cause);
    }

}
