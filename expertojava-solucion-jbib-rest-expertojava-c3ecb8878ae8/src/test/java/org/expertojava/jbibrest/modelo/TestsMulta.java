package org.expertojava.jbibrest.modelo;

import org.expertojava.jbibrest.utils.Utils;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertTrue;

public class TestsMulta {
    @Test
    public void compruebaMultaSeCreaCorrectamenteConFechaInicioyFechaFin() {
        Usuario profesor = new Profesor("alan.turing");
        Date fechaInicioMulta = Utils.stringToDate("01-10-2015");
        Date fechaFinalMulta = Utils.stringToDate("10-10-2015");
        Multa multa = new Multa(profesor, fechaInicioMulta, fechaFinalMulta);
        assertTrue(multa.getUsuario().equals(profesor));
        assertTrue(multa.getDesde().equals(fechaInicioMulta));
        assertTrue(multa.getHasta().equals(fechaFinalMulta));
    }

    @Test
    public void compruebaMultaSeCreaCorrectamenteConFechaInicioyDias() {
        Usuario profesor = new Profesor("alan.turing");
        Date fechaInicioMulta = Utils.stringToDate("01-10-2015");
        Multa multa = new Multa(profesor, fechaInicioMulta, 10);
        assertTrue(multa.getUsuario().equals(profesor));
        assertTrue(multa.getDesde().equals(fechaInicioMulta));
        assertTrue(multa.getDiasAcumulados() == 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void compruebaExcepcionCuandoSePasaNullAlConstructorDeMulta() {
        Usuario profesor = new Profesor("alan.turing");
        Date fechaInicioMulta =  Utils.stringToDate("01-10-2015");
        Date fechaFinalMulta = null;
        Multa multa = new Multa(profesor, fechaInicioMulta, fechaFinalMulta);
    }
}