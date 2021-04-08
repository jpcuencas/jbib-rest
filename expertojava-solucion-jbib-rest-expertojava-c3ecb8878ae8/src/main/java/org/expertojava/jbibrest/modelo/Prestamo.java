package org.expertojava.jbibrest.modelo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.expertojava.jbibrest.utils.BibliotecaException;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class Prestamo extends ClaseDominio {
    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Usuario usuario;
    @OneToOne
    private Ejemplar ejemplar;
    private Date fecha;
    private Date deberiaDevolverseEl;

    private static Log logger = LogFactory.getLog(Prestamo.class);

    public Prestamo() {}

    public Prestamo(Usuario usuario, Ejemplar ejemplar, Date fecha,
                    Date deberiaDevolverseEl) {
        String msg;

        if (usuario == null) {
            msg = "Error al crear prestamo: usuario null";
            logger.error(msg);
            throw new IllegalArgumentException(msg);
        }
        if (ejemplar == null) {
            msg = "Error al crear prestamo: ejemplar null";
            logger.error(msg);
            throw new IllegalArgumentException(msg);
        }
        if (fecha == null) {
            msg = "Error al crear prestamo: fecha null";
            logger.error(msg);
            throw new IllegalArgumentException(msg);
        }
        if (deberiaDevolverseEl == null) {
            msg = "Error al crear prestamo: deberiaDevolverseEl es null";
            logger.error(msg);
            throw new IllegalArgumentException(msg);
        }
        if (fecha.after(deberiaDevolverseEl)) {
            msg = "La fecha de inicio del préstamo no puede ser " +
                    "mayor que la de devolución";
            logger.error(msg);
            throw new BibliotecaException(msg);
        }
        if (fecha.after(deberiaDevolverseEl)) {
            msg = "La fecha de inicio del préstamo no puede ser " +
                    "mayor que la de devolución";
            logger.error(msg);
            throw new BibliotecaException(msg);
        }
        this.deberiaDevolverseEl = deberiaDevolverseEl;
        this.usuario = usuario;
        this.ejemplar = ejemplar;
        this.fecha = fecha;
        this.deberiaDevolverseEl = deberiaDevolverseEl;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Ejemplar getEjemplar() {
        return ejemplar;
    }

    public Date getFecha() {
        return fecha;
    }

    public Date getDeberiaDevolverseEl() {
        return deberiaDevolverseEl;
    }

    // No definimos setters de los atributos porque
    // no se van a poder cambiar una vez creado
}
