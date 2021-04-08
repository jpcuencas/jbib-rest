package org.expertojava.jbibrest.modelo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.expertojava.jbibrest.utils.BibliotecaBR;
import org.expertojava.jbibrest.utils.Utils;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class TestsUsuario {

    private static Log logger = LogFactory.getLog(TestsUsuario.class);

    @Test
    public void compruebaEqualsDevuelveTrueConMismoId() {
        Usuario usuario1 = new Alumno("juan.perez");
        Usuario usuario2 = new Alumno("ana.garcia");
        usuario1.setId(1L);
        usuario2.setId(1L);
        assertTrue(usuario1.equals(usuario2));
        usuario2.setId(2L);
        assertFalse(usuario1.equals(usuario2));
    }

    @Test
    public void compruebaEqualsDevuelveFalseConDistintoTipo() {
        Usuario usuario1 = new Alumno("juan.perez");
        Usuario usuario2 = new Profesor("alan.turing");
        usuario1.setId(1L);
        usuario2.setId(1L);
        assertFalse(usuario1.equals(usuario2));
    }

    @Test
    public void compruebaGetPrestamosDevolveColeccionVacia() {
        Usuario usuario = new Profesor("alan.turing");
        assertEquals(usuario.getPrestamos().size(), 0);
    }

    @Test
    public void compruebaUsuarioRecienCreadoEsActivo() {
        Usuario usuario = new Alumno("juan.perez");
        Date fechaActual = new Date();
        assertTrue(usuario.getEstado(fechaActual).equals(EstadoUsuario.ACTIVO));
    }

    @Test
    public void compruebaUsuarioConMultaTieneEstadoMultado() {
        Usuario usuario = new Alumno("juan.perez");
        Date fechaActual = new Date();
        Date fechaFutura = BibliotecaBR.getInstance().fechaDevolucionPrestamo(usuario, fechaActual);
        Multa multa = new Multa(usuario, fechaActual, fechaFutura);
        usuario.setMulta(multa);
        assertTrue(usuario.getEstado(fechaActual).equals(EstadoUsuario.MULTADO));
    }

    @Test
    public void compruebaUsuarioConPrestamoVencidoTieneEstadoMoroso() {
        Usuario usuario = new Alumno("juan.perez");
        Prestamo prestamo = creaPrestamo(usuario, "01-10-2015");
        // La fecha de devolución será 01-10 más 7 días -> 08-10-2015
        usuario.getPrestamos().add(prestamo);
        // Fecha actual posterior a la de devolución
        Date fechaActual = Utils.stringToDate("10-10-2015");
        assertTrue(usuario.getEstado(fechaActual).equals(EstadoUsuario.MOROSO));
    }

    private Prestamo creaPrestamo(Usuario usuario, String fechaInicioStr) {
        Date fechaInicio = Utils.stringToDate(fechaInicioStr);
        Date fechaDevolucion = BibliotecaBR.getInstance().fechaDevolucionPrestamo(usuario, fechaInicio);
        Ejemplar ejemplar = new Ejemplar("0001", 1L);
        Prestamo prestamo = new Prestamo(usuario, ejemplar, fechaInicio, fechaDevolucion);
        return prestamo;
    }
}