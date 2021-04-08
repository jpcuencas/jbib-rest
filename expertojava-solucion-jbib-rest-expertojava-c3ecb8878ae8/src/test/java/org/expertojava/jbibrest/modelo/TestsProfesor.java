package org.expertojava.jbibrest.modelo;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class TestsProfesor {
    @Test
    public void compruebaEqualsDevuelveTrueConMismoId() {
        Profesor profesor1 = new Profesor("alan.turing");
        Profesor profesor2 = new Profesor("kent.beck");
        profesor1.setId(1L);
        profesor2.setId(1L);
        assertTrue(profesor1.equals(profesor2));
        profesor1.setId(2L);
        assertFalse(profesor1.equals(profesor2));
    }

    @Test
    public void compruebaValoresPorDefectoEnNuevoProfesor() {
        Profesor profesor = new Profesor("alan.turing");
        assertEquals(0, profesor.getPrestamos().size());
        Date fechaActual = new Date();
        assertEquals(EstadoUsuario.ACTIVO, profesor.getEstado(fechaActual));
    }
}
