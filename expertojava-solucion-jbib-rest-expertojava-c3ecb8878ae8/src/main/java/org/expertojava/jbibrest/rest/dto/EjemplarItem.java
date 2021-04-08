package org.expertojava.jbibrest.rest.dto;

import org.expertojava.jbibrest.modelo.Ejemplar;
import org.expertojava.jbibrest.modelo.Libro;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class EjemplarItem {
    @XmlElement(name="resource_uri")
    public String resourceUri;
    public String isbn;
    public String titulo;
    @XmlElement(name="ejemplar_id")
    public Long ejemplarId;
    @XmlElement(name="id")
    public Long libroId;

    public EjemplarItem() {}

    public EjemplarItem(Ejemplar ejemplar, Libro libro) {
        this.ejemplarId = ejemplar.getId();
        this.libroId = ejemplar.getLibroId();
        this.isbn = libro.getIsbn();
        this.titulo = libro.getTitulo();
    }
}
