package org.expertojava.jbibrest.modelo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Recomendacion extends ClaseDominio {
    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Libro libro;
    @NotNull
    @OneToOne
    @JoinColumn(nullable = false)
    private Libro libroRelacionado;
    private String comentario;

    private static Log logger = LogFactory.getLog(Recomendacion.class);

    public Recomendacion() {}

    public Recomendacion(Libro libro, Libro libroRelacionado) {
        String msg;
        if (libro == null) {
            msg = "Error al crear recomendación: libro null";
            logger.error(msg);
            throw new IllegalArgumentException(msg);
        }
        if (libroRelacionado == null) {
            msg = "Error al crear recomendación: libroRelacionado null";
            logger.error(msg);
            throw new IllegalArgumentException(msg);
        }
        this.libro = libro;
        this.libroRelacionado = libroRelacionado;
    }

    public Libro getLibro() {
        return libro;
    }

    public Libro getLibroRelacionado() {
        return libroRelacionado;
    }

    public void setLibroRelacionado(Libro libroRelacionado) {
        this.libroRelacionado = libroRelacionado;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
