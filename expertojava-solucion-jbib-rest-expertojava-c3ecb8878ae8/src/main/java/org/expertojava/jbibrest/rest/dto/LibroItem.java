package org.expertojava.jbibrest.rest.dto;

import org.expertojava.jbibrest.modelo.Libro;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="libroitem")
@XmlAccessorType(XmlAccessType.FIELD)
public class LibroItem {
    @XmlElement(name="resource_uri")
    public String uri;
    public Long id;
    public String isbn;
    public String titulo;
    public String autor;
    public String imagen;

    public LibroItem() {}

    public LibroItem(Libro libro) {
        this.id = libro.getId();
        this.isbn = libro.getIsbn();
        this.autor = libro.getAutor();
        this.titulo = libro.getTitulo();
        this.imagen = libro.getPortadaURI();
    }

    public LibroItem(Long id, String isbn, String titulo, String autor, String imagen) {
        this.id = id;
        this.isbn = isbn;
        this.autor = titulo;
        this.titulo = autor;
        this.imagen = imagen;
    }
}
