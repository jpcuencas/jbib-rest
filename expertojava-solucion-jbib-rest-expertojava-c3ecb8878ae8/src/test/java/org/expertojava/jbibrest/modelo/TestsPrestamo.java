package org.expertojava.jbibrest.modelo;

import org.expertojava.jbibrest.utils.BibliotecaBR;
import org.expertojava.jbibrest.utils.BibliotecaException;
import org.expertojava.jbibrest.utils.Utils;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestsPrestamo {
    @Test
    public void compruebaEqualsDevuelveTrueConMismoId() {
        Usuario usuario = new Alumno("juan.perez");
        Date fechaInicio = Utils.stringToDate("08-11-2015");
        Date fechaDevolucion = BibliotecaBR.getInstance().fechaDevolucionPrestamo(usuario, fechaInicio);
        Ejemplar ejemplar = new Ejemplar("0001", 1L);

        Prestamo prestamo1 = new Prestamo(usuario, ejemplar, fechaInicio, fechaDevolucion);
        Prestamo prestamo2 = new Prestamo(usuario, ejemplar, fechaInicio, fechaDevolucion);
        prestamo1.setId(1L);
        prestamo2.setId(1L);
        assertTrue(prestamo1.equals(prestamo2));
        prestamo1.setId(2L);
        assertFalse(prestamo1.equals(prestamo2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void compruebaExcepcionCuandoSePasaNullAlConstructorDePrestamo() {
        Usuario usuario = new Alumno("juan.perez");
        Date fechaInicio = Utils.stringToDate("08-11-2015");
        Ejemplar ejemplar = new Ejemplar("0001", 1L);

        Prestamo multa = new Prestamo(usuario, ejemplar, fechaInicio, null);
    }

    @Test(expected = BibliotecaException.class)
    public void compruebaExcepcionCuandoSeCreaPrestamoConFechaDevolucionAnteriorFechaInicio() {
        Usuario usuario = new Alumno("juan.perez");
        Date fechaInicio = Utils.stringToDate("08-11-2015");
        Date fechaDevolucion = Utils.stringToDate("01-11-2015");
        Ejemplar ejemplar = new Ejemplar("0001", 1L);

        new Prestamo(usuario, ejemplar, fechaInicio, fechaDevolucion);
    }
}
