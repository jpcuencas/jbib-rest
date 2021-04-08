package org.expertojava.jbibrest.rest;

import org.expertojava.jbibrest.modelo.*;
import org.expertojava.jbibrest.rest.dto.EjemplarItem;
import org.expertojava.jbibrest.rest.dto.IdItem;
import org.expertojava.jbibrest.rest.dto.PrestamoItem;
import org.expertojava.jbibrest.rest.dto.UsuarioDetalle;
import org.expertojava.jbibrest.rest.exception.RestException;
import org.expertojava.jbibrest.servicio.LibroServicio;
import org.expertojava.jbibrest.servicio.UsuarioServicio;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Path("/usuarios")
public class UsuarioResource {
    @Context
    UriInfo uriInfo;
    @Context
    SecurityContext securityContext;
    @Inject
    UsuarioServicio usuarioServicio;
    @Inject
    LibroServicio libroServicio;

    @GET
    @Produces({"application/json"})
    @Path("{usuario}")
    public UsuarioDetalle getUsuario(@PathParam("usuario") String login) {
        if (securityContext.getUserPrincipal().getName().equals(login) &&
                securityContext.isUserInRole("usuario")) {
            System.out.println("Antes de recuperar el usuario: ("+login+") con el servicio");
            Usuario usuario = usuarioServicio.recuperarUsuario(login);
            System.out.println("recuperado el usuario " + login+ " desde el servicio");
            //asegurar que no devuelve una excepción
            if (usuario == null)
                throw new RestException(RestException.USUARIO_NO_EXISTENTE);
            return new UsuarioDetalle(usuario);
        }
        else throw new RestException(RestException.USUARIO_NO_ES_EL_LOGUEADO);
    }

    @GET
    @Produces({"application/json"})
    @Path("{id}/prestamos")
    public List<PrestamoItem> getPrestamos(@PathParam("id") Long id) {
        Usuario usuario = usuarioServicio.buscaUsuarioPorId(id);
        if (usuario == null)
            throw new RestException(RestException.USUARIO_NO_EXISTENTE);
        if (securityContext.getUserPrincipal().getName().equals(usuario.getLogin())  &&
                securityContext.isUserInRole("usuario")) {
            List<PrestamoItem> prestamosItem = new ArrayList<>();
            List<Prestamo> prestamos = usuarioServicio.prestamosActivos(usuario.getId());
            for (Prestamo prestamo : prestamos) {
                Ejemplar ejemplar = prestamo.getEjemplar();
                Libro libro = libroServicio.buscaLibroPorId(ejemplar.getLibroId());
                EjemplarItem ejemplarItem = new  EjemplarItem(ejemplar, libro);
                ejemplarItem.resourceUri = UriBuilder
                        .fromMethod(LibroResource.class,"getLibro").toString();

                PrestamoItem prestamoItem = new PrestamoItem(prestamo,libro);
                prestamoItem.setEjemplarItem(ejemplarItem);

                LibroResource libroResource = new LibroResource();
                prestamoItem.ejemplarItem.resourceUri = libroResource
                        .getLibroUri(uriInfo.getBaseUriBuilder(), prestamoItem.ejemplarItem.ejemplarId);
                prestamosItem.add(prestamoItem);
            }
            return prestamosItem;
        }
        else throw new RestException(RestException.USUARIO_NO_ES_EL_LOGUEADO);
    }


    @POST
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @Path("{id}/prestamos")
    public Response realizarPrestamo(@PathParam("id") Long id, IdItem idLibro) {
        Usuario usuario = usuarioServicio.buscaUsuarioPorId(id);
        if (usuario == null)
            throw new RestException(RestException.USUARIO_NO_EXISTENTE);

        if (securityContext.getUserPrincipal().getName().equals(usuario.getLogin()) &&
                securityContext.isUserInRole("usuario")) {
            Prestamo prestamo = usuarioServicio.solicitaPrestamo(usuario.getId(), idLibro.id);
            //devolvemos la uri del nuevo prestamoItem creado en la cabecera Location
            UriBuilder builder = uriInfo.getAbsolutePathBuilder();
            URI createdUri = builder.path("{id}").build(prestamo.getId());
            //devolvemos una instancia de PrestamoItem en el cuerpo de la respuesta
            Ejemplar ejemplar = prestamo.getEjemplar();

            Libro libroEjemplar = libroServicio.buscaLibroPorId(ejemplar.getLibroId());

            PrestamoItem prestamoItem = new PrestamoItem(prestamo,libroEjemplar);

            EjemplarItem ejemplarItem = new  EjemplarItem(ejemplar, libroEjemplar);
            prestamoItem.setEjemplarItem(ejemplarItem);
            return Response.created(createdUri).entity(prestamoItem).build();
        }
        else throw new RestException(RestException.USUARIO_NO_ES_EL_LOGUEADO);
    }


    @POST
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @Path("{id}/devoluciones")
    public Response realizarDevolucion(@PathParam("id") Long id, IdItem idEjemplar) {
        Usuario usuario = usuarioServicio.buscaUsuarioPorId(id);
        if (usuario == null)
            throw new RestException(RestException.USUARIO_NO_EXISTENTE);

        if (securityContext.getUserPrincipal().getName().equals(usuario.getLogin()) &&
                securityContext.isUserInRole("usuario")) {
            ResultadoDevolucion resultado = usuarioServicio.devolverPrestamo(idEjemplar.id);
            return Response.ok().entity(" { \"resultado\" : \"" + resultado.toString() + "\"} ").build();
        }
        else throw new RestException(RestException.USUARIO_NO_ES_EL_LOGUEADO);
    }

}
