package org.expertojava.jbibrest.rest.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.expertojava.jbibrest.modelo.Libro;
import org.expertojava.jbibrest.modelo.Prestamo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PrestamoItem {
    //campo nuevo para identificar cada préstamo
    @XmlElement(name="id_prestamo")
    public Long id;
    @XmlElement(name="fecha_prestamo")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd,HH:00", timezone="CET")
    public Date fechaPrestamo;
    @XmlElement(name="fecha_devolucion")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd,HH:00", timezone="CET")
    public Date fechaDevolucion;
    @XmlElement(name="libro")
    public EjemplarItem ejemplarItem;

    //el parámetro libro es nuevo
    public PrestamoItem(Prestamo prestamo, Libro libro) {
        this.id = prestamo.getId();
        this.fechaPrestamo = prestamo.getFecha();
        this.fechaDevolucion = prestamo.getDeberiaDevolverseEl();
        this.ejemplarItem = new EjemplarItem(prestamo.getEjemplar(), libro);
    }

    public void setEjemplarItem(EjemplarItem ejemplarItem) {
        this.ejemplarItem = ejemplarItem;
    }
}
