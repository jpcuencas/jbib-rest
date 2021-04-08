package org.expertojava.jbibrest.modelo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Libro extends ClaseDominio {
    @NotNull
    @Column(unique=true, nullable = false)
    private String isbn;
    private String autor;
    private String titulo;
    @Min(0)
    private Integer numPaginas;
    private String portadaURI;
    @OneToMany(mappedBy = "libro", fetch = FetchType.EAGER)
    private Set<Recomendacion> recomendaciones = new HashSet<Recomendacion>();

    private static Log logger = LogFactory.getLog(Libro.class);

    public Libro() {}

    public Libro(String isbn) {
        this.isbn = isbn;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getNumPaginas() {
        return numPaginas;
    }

    public void setNumPaginas(Integer numPaginas) {
        this.numPaginas = numPaginas;
    }

    public String getPortadaURI() {
        return portadaURI;
    }

    public void setPortadaURI(String portadaURI) {
        this.portadaURI = portadaURI;
    }

    public Set<Recomendacion> getRecomendaciones() {
        return recomendaciones;
    }

    // Actualización de la relación a-muchos recomendaciones
    // No hay que actualizar la relación inversa, porque
    // estamos haciendo una inicialización, no un cambio, y la
    // recomendación ya está creada con el libro
    public void añadeRecomendacion(Recomendacion recomendacion) {
        this.getRecomendaciones().add(recomendacion);
    }

    @Override
    public String toString() {
        return "Libro{" +
                "id=" + this.getId() +
                ", isbn='" + this.getIsbn() + '\'' +
                ", autor='" + this.getAutor() + '\'' +
                ", titulo='" + this.getTitulo() + '\'' +
                ", numPaginas=" + this.getNumPaginas() +
                ", portadaURI='" + this.getPortadaURI() + '\'' +
                '}';
    }
}
