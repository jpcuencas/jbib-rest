package org.expertojava.jbibrest.modelo;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestsRecomendacion {
    @Test
    public void compruebaEqualsDevuelveTrueConMismoId() {
        Libro libro1 = new Libro("123456789");
        libro1.setId(1L);
        Libro libro2 = new Libro("123456789");
        libro2.setId(2L);
        Recomendacion recomendacion1 = new Recomendacion(libro1, libro2);
        Recomendacion recomendacion2 = new Recomendacion(libro1, libro2);
        recomendacion1.setId(1L);
        recomendacion2.setId(1L);
        assertTrue(recomendacion1.equals(recomendacion2));
        recomendacion1.setId(2L);
        assertFalse(recomendacion1.equals(recomendacion2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void compruebaExcepcionCuandoSePasaNullAlConstructorDeRecomendacion() {
        Recomendacion recomendacion = new Recomendacion(null, null);
    }
}
