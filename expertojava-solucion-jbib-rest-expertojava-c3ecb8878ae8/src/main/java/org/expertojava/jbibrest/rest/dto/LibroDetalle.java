package org.expertojava.jbibrest.rest.dto;


import org.expertojava.jbibrest.modelo.Libro;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LibroDetalle {
    @XmlElement(name="resource_uri")
    public String uri;
    public Long id;
    public String isbn;
    public String titulo;
    public String autor;
    public String imagen;
    public long ejemplares;
    public long disponibles;

    public LibroDetalle() {}

    public LibroDetalle(Libro libro) {
        this.id = libro.getId();
        this.isbn = libro.getIsbn();
        this.autor = libro.getAutor();
        this.titulo = libro.getTitulo();
        this.imagen = libro.getPortadaURI();
    }
}
