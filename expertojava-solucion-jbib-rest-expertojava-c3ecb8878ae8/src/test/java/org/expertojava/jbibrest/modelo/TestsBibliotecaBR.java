package org.expertojava.jbibrest.modelo;


import org.expertojava.jbibrest.utils.BibliotecaBR;
import org.expertojava.jbibrest.utils.BibliotecaException;
import org.expertojava.jbibrest.utils.Utils;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class TestsBibliotecaBR {

    //
    // Profesor
    //

    @Test
    public void compruebaNumDiasPrestamoProfesor() {
        int diasProfesor = BibliotecaBR.getInstance()
                .calculaNumDiasPrestamo(
                        new Profesor("alan.turing"));
        assertEquals(30, diasProfesor);
    }

    @Test
    public void compruebaCupoOperacionesProfesor() {
        int cupoProfesor = BibliotecaBR.getInstance().cupoOperaciones(
                new Profesor("alan.turing"));
        assertEquals(cupoProfesor, 8);
    }

    @Test
    public void compruebaNoSaltaExcepcionCupoOperacionesProfesorCorrecto() {
        try {
            Profesor profesor =
                    new Profesor("alan.turing");
            BibliotecaBR.getInstance()
                    .compruebaCupoOperaciones(profesor, 8);
            BibliotecaBR.getInstance()
                    .compruebaCupoOperaciones(profesor, 1);
        } catch (BibliotecaException e) {
            fail("No debería fallar - el cupo de operaciones del" +
                    " PROFESOR es correcto");
        }
    }

    @Test(expected = BibliotecaException.class)
    public void compruebaSaltaExcepcionCupoOperacionesProfesorIncorrecto()
            throws BibliotecaException {
        BibliotecaBR.getInstance()
                .compruebaCupoOperaciones(
                        new Profesor("alan.turing"), 9);
    }

    //
    // Alumno
    //

    @Test
    public void compruebaNumDiasPrestamoAlumno() {
        int diasAlumno = BibliotecaBR.getInstance().calculaNumDiasPrestamo(
                new Alumno("juan.perez"));
        assertEquals(7, diasAlumno);
    }

    @Test
    public void compruebaCupoOperacionesAlumno() {
        int cupoAlumno = BibliotecaBR.getInstance().cupoOperaciones(
                new Alumno("juan.perez"));
        assertEquals(cupoAlumno, 5);
    }

    @Test
    public void compruebaNoSaltaExcpecionCupoOperacionesAlumnoCorrecto() {
        try {
            Alumno alumno = new Alumno("juan.perez");
            BibliotecaBR.getInstance().compruebaCupoOperaciones(alumno, 5);
            BibliotecaBR.getInstance().compruebaCupoOperaciones(alumno, 1);
        } catch (BibliotecaException e) {
            fail("No debería fallar - el cupo de operaciones del ALUMNO es correcto");
        }
    }

    @Test(expected = BibliotecaException.class)
    public void compruebaSaltaExcepcionCupoOperacionesAlumnoIncorrecto()
            throws BibliotecaException {
        BibliotecaBR.getInstance().compruebaCupoOperaciones(new Alumno
                ("juan.perez"), 6);
    }

    // Préstamos y fechas de devolución

    @Test
    public void compruebaFechaDevolucionPrestamoEs7DiasMasTardeParaAlumno() {
        Date fechaInicioPrestamo = Utils.stringToDate("2015-10-01");
        Usuario alumno = new Alumno("juan.perez");
        Date deberiaDevolverseEl = BibliotecaBR.getInstance().fechaDevolucionPrestamo(alumno, fechaInicioPrestamo);
        // La fecha de devolución será 10-01 más 7 días -> 2015-10-08
        Date fechalimite = Utils.stringToDate("2015-10-08");
        assertEquals(0, deberiaDevolverseEl.compareTo(fechalimite));
    }

    @Test
    public void compruebaDiasPenalizacionPrestamoEsDiasDeRetrasoPrestamo() {
        Date fechaInicioPrestamo = Utils.stringToDate("2015-10-01");
        Usuario alumno = new Alumno("juan.perez");
        Prestamo prestamo = creaPrestamo(alumno, "2015-10-01");
        Date fechaDevolucionReal = Utils.stringToDate("2015-10-18");
        // La fecha de devolución será 10-01 más 7 días -> 2015-10-08
        // Si se devuelve el 18-10, la penalización será de 10 días
        assertEquals(10, BibliotecaBR.getInstance().diasPenalizacion(prestamo, fechaDevolucionReal));
    }

    @Test
    public void compruebaPrestamoEsMorosoCuandoFechaFinalizacionPosteriorFechaDevolucion() {
        Usuario alumno = new Alumno("juan.perez");
        Prestamo prestamo = creaPrestamo(alumno, "2015-10-01");
        // La fecha de devolución será 10-01 más 7 días -> 2015-10-08
        // Suponemos una fecha actual posterior a la fecha de devolución -> 2015-10-18
        Date fechaActual = Utils.stringToDate("2015-10-18");
        assertTrue(BibliotecaBR.getInstance().esPrestamoRetrasado(prestamo, fechaActual));
        // Ahora suponemos una fecha actual anterior a la fecha de devolución -> 2015-10-07
        fechaActual = Utils.stringToDate("2015-10-07");
        assertFalse(BibliotecaBR.getInstance().esPrestamoRetrasado(prestamo, fechaActual));
    }

    private Prestamo creaPrestamo(Usuario usuario, String fechaInicioStr) {
        Date fechaInicio = Utils.stringToDate(fechaInicioStr);
        Date fechaDevolucion = BibliotecaBR.getInstance().fechaDevolucionPrestamo(usuario, fechaInicio);
        Ejemplar ejemplar = new Ejemplar("0001", 1L);
        Prestamo prestamo = new Prestamo(usuario, ejemplar, fechaInicio, fechaDevolucion);
        return prestamo;
    }
}