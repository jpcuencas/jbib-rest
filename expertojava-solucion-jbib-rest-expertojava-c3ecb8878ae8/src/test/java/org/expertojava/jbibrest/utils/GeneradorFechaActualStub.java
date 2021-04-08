package org.expertojava.jbibrest.utils;

import javax.persistence.EntityManager;
import java.util.Date;

public class GeneradorFechaActualStub implements GeneradorFechaActual {
    Date fecha = null;

    public void setFechaActual(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFechaActual()  {
       return fecha;
    }
}