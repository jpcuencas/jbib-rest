package org.expertojava.jbibrest.rest;

import org.expertojava.jbibrest.modelo.Libro;
import org.expertojava.jbibrest.modelo.Recomendacion;
import org.expertojava.jbibrest.rest.dto.LibroDetalle;
import org.expertojava.jbibrest.rest.dto.LibroItem;
import org.expertojava.jbibrest.rest.exception.RestException;
import org.expertojava.jbibrest.servicio.EjemplarServicio;
import org.expertojava.jbibrest.servicio.LibroServicio;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

@Path("/libros")
public class LibroResource {
    @Context
    UriInfo uriInfo;
    @Inject
    LibroServicio libroServicio;
    @Inject
    EjemplarServicio ejemplarServicio;
    String uriIBaseImagen = "http://expertojava.ua.es/media/";

    @GET
    @Produces({"application/json"})
    @Path("{id}")
    public LibroDetalle getLibro(@PathParam("id") Long id) {
        Libro libro = libroServicio.buscaLibroPorId(id);
        if (libro == null)
            throw new RestException(RestException.LIBRO_NO_EXISTENTE);
        LibroDetalle libroDetalle = new LibroDetalle(libro);
        libroDetalle.disponibles = ejemplarServicio.numEjemplaresDisponibles(libro.getId());
        libroDetalle.ejemplares = ejemplarServicio.numEjemplaresTotales(libro.getId());
        libroDetalle.imagen = uriIBaseImagen + libroDetalle.imagen;
        libroDetalle.uri = getLibroUri(uriInfo.getBaseUriBuilder(), libro.getId());
        return libroDetalle;
    }

    @GET
    @Produces({"application/json"})
    public Collection<LibroItem> getTodosLibros(@QueryParam("autor") String autor,
                                                @QueryParam("titulo") String titulo) {
        Collection<Libro> libros;
        Collection<LibroItem> librosItems = new ArrayList<LibroItem>();
        if (autor != null && titulo != null)
            throw new RestException(RestException.BUSQUEDA_POR_TITULO_Y_AUTOR_NO_IMPLEMENTADA);
        else if (autor != null)
            libros = libroServicio.buscaLibrosAutor(autor);
        else if (titulo != null)
            libros = libroServicio.buscaLibrosTituloContiene(titulo);
        else
            libros = libroServicio.listaLibros();
        for (Libro libro : libros) {
            LibroItem libroItem = new LibroItem(libro);
            libroItem.uri = getLibroUri(uriInfo.getBaseUriBuilder(), libro.getId());
            libroItem.imagen = uriIBaseImagen + nombreFicheroMini(libroItem.imagen);
            librosItems.add(libroItem);
        }
        if (librosItems.size()==0) System.out.println("lista de libros vacia");
        return librosItems;
    }

    @GET
    @Produces({"application/json"})
    @Path("{id}/recomendaciones")
    public Collection<LibroItem> getRecomendaciones(@PathParam("id") Long id,
                                                   @DefaultValue("1") @QueryParam("num") int num) {
        Collection<LibroItem> librosItems = new ArrayList<>();
        Libro libro = libroServicio.buscaLibroPorId(id);
        if (libro == null)
            throw new RestException(RestException.LIBRO_NO_EXISTENTE);
        Collection<Recomendacion> listaRecomendaciones = libroServicio.listaRecomendaciones(libro.getId(), num);
        for (Recomendacion recomendacion : listaRecomendaciones) {
            libro = recomendacion.getLibroRelacionado();
            LibroItem libroItem = new LibroItem(libro);
            libroItem.uri = getLibroUri(uriInfo.getBaseUriBuilder(), libro.getId());
            libroItem.imagen = uriIBaseImagen + nombreFicheroMini(libroItem.imagen);
            librosItems.add(libroItem);
        }
        return librosItems;
    }

    // Obtiene la URI de una petición de un libro.
    // Ejemplo: "03042424" -> "http://localhost:8080/jbib-rest/api/libros/0321127420"
    public String getLibroUri(UriBuilder baseUriBuilder, Long id) {
        baseUriBuilder.path(LibroResource.class)
                .path(LibroResource.class, "getLibro");
        URI uri = baseUriBuilder.build(id);
        return uri.toString();
    }

    // Devuelve el nombre de fichero de la versión pequeña
    // de la portada de un libro, añadiendo el sufijo "small".
    // Ejemplo: 03042424.jpg ->  03042424-small.jpg
    private String nombreFicheroMini(String nombreFichero) {
            String[] splitString = nombreFichero.split("\\.");
            return splitString[0] + "-small." + splitString[1];
    }

}
