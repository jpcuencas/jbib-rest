package org.expertojava.jbibrest.modelo;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestsEjemplar {
    @Test
    public void compruebaEqualsDevuelveTrueConMismoId() {
        Ejemplar ejemplar1 = new Ejemplar("0001", 1L);
        Ejemplar ejemplar2 = new Ejemplar("0001", 1L);
        ejemplar1.setId(1L);
        ejemplar2.setId(1L);
        assertTrue(ejemplar1.equals(ejemplar2));
        ejemplar2.setId(2L);
        assertFalse(ejemplar1.equals(ejemplar2));
    }
}
