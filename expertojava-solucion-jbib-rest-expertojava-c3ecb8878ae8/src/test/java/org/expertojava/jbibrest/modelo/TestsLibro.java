package org.expertojava.jbibrest.modelo;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestsLibro {

    @Test
    public void compruebaEqualsDevuelveTrueConMismoId() {
        Libro libro1 = new Libro("123456789");
        Libro libro2 = new Libro("123456789");
        libro1.setId(1L);
        libro2.setId(1L);
        assertTrue(libro1.equals(libro2));
        libro2.setId(2L);
        assertFalse(libro1.equals(libro2));
    }

    @Test
    public void compruebaEqualsDevuelveTrueConIdsNull() {
        Libro libro1 = new Libro("123456789");
        Libro libro2 = new Libro("1234");
        assertTrue(libro1.equals(libro2));
    }

    @Test
    public void compruebaValoresPorDefectoEnNuevoLibro() {
        Libro libro = new Libro("123456789");
        assertEquals(0, libro.getRecomendaciones().size());
    }
}
