package org.expertojava.jbibrest.modelo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class Ejemplar extends ClaseDominio {
    @NotNull
    @Column(nullable = false)
    private Long libroId;
    @NotNull
    @Column(nullable = false)
    private String codigoEjemplar;
    @Temporal(TemporalType.DATE)
    private Date fechaAdquisicion;
    private String observaciones;
    @OneToOne(mappedBy = "ejemplar")
    private Prestamo prestamo;

    private static Log logger = LogFactory.getLog(Ejemplar.class);

    public Ejemplar() {}

    public Ejemplar(String codigoEjemplar, Long libroId) {
        this.codigoEjemplar = codigoEjemplar;
        this.libroId = libroId;
    }

    public Long getLibroId() {
        return libroId;
    }

    public void setLibroId(Long libroId) {
        this.libroId = libroId;
    }

    public String getCodigoEjemplar() {
        return codigoEjemplar;
    }

    public void setCodigoEjemplar(String codigoEjemplar) {
        this.codigoEjemplar = codigoEjemplar;
    }

    public Date getFechaAdquisicion() {
        return fechaAdquisicion;
    }

    public void setFechaAdquisicion(Date fechaAdquisicion) {
        this.fechaAdquisicion = fechaAdquisicion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Prestamo getPrestamo() {
        return prestamo;
    }

    public void setPrestamo(Prestamo prestamo) {
        if (prestamo == null) {
            throw new IllegalArgumentException("Prestamo no puede ser null");
        }
        this.prestamo = prestamo;
    }

    public void quitaPrestamo() {
        this.prestamo = null;
    }
}
