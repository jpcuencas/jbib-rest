package org.expertojava.jbibrest.modelo;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class TestsAlumno {
    @Test
    public void compruebaEqualsDevuelveTrueConMismoId() {
        Alumno alumno1 = new Alumno("juan.perez");
        Alumno alumno2 = new Alumno("juan.perez");
        alumno1.setId(1L);
        alumno2.setId(1L);
        assertTrue(alumno1.equals(alumno2));
        alumno1.setId(2L);
        assertFalse(alumno1.equals(alumno2));
    }

    @Test
    public void compruebaValoresPorDefectoEnNuevoAlumno() {
        Alumno alumno = new Alumno("juan.perez");
        assertEquals(0, alumno.getPrestamos().size());
        Date fechaActual = new Date();
        assertEquals(EstadoUsuario.ACTIVO, alumno.getEstado(fechaActual));
    }
}
