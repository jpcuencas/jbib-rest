package org.expertojava.jbibrest.rest.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.expertojava.jbibrest.utils.BibliotecaException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class MapeadorExcepciones implements ExceptionMapper<RuntimeException> {
    private static Log logger = LogFactory.getLog(MapeadorExcepciones.class);

    public Response toResponse(RuntimeException excep) {
        logger.error(excep.getMessage(), excep);

        // Excepción de la capa REST - RestException
        if (excep instanceof RestException) {
            String mensaje = excep.getMessage();
            switch (mensaje) {
                // Respuesta 404 NOT FOUND
                case RestException.LIBRO_NO_EXISTENTE:
                    return myResponse(Response.Status.NOT_FOUND, mensaje,
                            "Error del servicio Rest: Debe solicitar al bibliotecario que añada el libro");
                case RestException.USUARIO_NO_EXISTENTE:
                    return myResponse(Response.Status.NOT_FOUND, mensaje,
                            "Error del servicio Rest: Debe darse de alta en el sistema");
                // Respuesta 501 NOT IMPLEMENTED
                case RestException.BUSQUEDA_POR_TITULO_Y_AUTOR_NO_IMPLEMENTADA:
                    return myResponse(Response.Status.NOT_IMPLEMENTED, mensaje,
                            "Error del servicio Rest: Este servicio estará operativo en el futuro");
                // Respuesta 401 UNAUTHORIZED
                case RestException.USUARIO_NO_ES_EL_LOGUEADO:
                    return myResponse(Response.Status.UNAUTHORIZED, mensaje,
                            "Error del servicio Rest: Sólo puede realizar operaciones si usted es el mismo usuario con el que se ha logueado");
                default:
                    // Respuesta 500 INTERNAL SERVER ERROR
                    return myResponse(Response.Status.INTERNAL_SERVER_ERROR, mensaje,"error del servicio rest");
            }
        }

        // Excepciones de la capa Servicio
        else if (excep instanceof BibliotecaException) {
            String mensaje = excep.getMessage();
            switch (mensaje) {
                // Respuesta 403 FORBIDDEN
                case BibliotecaException.NO_HAY_EJEMPLARES_DISPONIBLES:
                    return myResponse(Response.Status.FORBIDDEN, mensaje,
                            "Error de Biblioteca: No puede solicitar el préstamo, tiene que esperar a que haya ejemplares disponibles");
                case BibliotecaException.USUARIO_MOROSO:
                    return myResponse(Response.Status.FORBIDDEN, mensaje,
                            "Error de Biblioteca: No puede realizar ningún préstamo, tiene que devolver primero los libros que tiene prestados");
                case BibliotecaException.USUARIO_MULTADO:
                    return myResponse(Response.Status.FORBIDDEN, mensaje,
                            "Error de Biblioteca: No puede realizar ningún préstamo hasta que no finalice la multa");
                case BibliotecaException.EJEMPLAR_NO_DISPONIBLE:
                    return myResponse(Response.Status.FORBIDDEN, mensaje,
                            "Error de Biblioteca: No se puede realizar el préstamo, tiene que esperar a que haya ejemplares disponibles");
                    // Respuesta 404 NOT FOUND
                case BibliotecaException.EJEMPLAR_NO_EXISTENTE:
                    return myResponse(Response.Status.NOT_FOUND, mensaje,
                            "Error de Biblioteca: No se puede realizar el préstamo, tiene que esperar a que se adquieran más ejemplares");
                case BibliotecaException.USUARIO_NO_EXISTENTE:
                    return myResponse(Response.Status.NOT_FOUND, mensaje,
                            "Error de Biblioteca: No se puede realizar el préstamo, no consta como registrado en el sistema");
                default:
                    // Respuesta 500 INTERNAL SERVER ERROR
                    return myResponse(Response.Status.INTERNAL_SERVER_ERROR, mensaje,"error de la Biblioteca");

            }
        }

        // Otras excepciones
        else return myResponse(Response.Status.INTERNAL_SERVER_ERROR,
                    excep.toString(),"error del servidor");
    }


    //Devolvemos en la respuesta un objeto ErrorMensajeBean, con información
    //sobre el error, que será serializado por Jaxb a xml y/o Json
    private Response myResponse(Response.Status status, String mensaje1, String mensaje2) {
        ErrorMessageBean errorMensaje =
                new ErrorMessageBean(status, mensaje1,mensaje2);
        return Response.status(status)
                .entity(errorMensaje)
                .build();
    }

}
