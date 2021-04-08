package org.expertojava.jbibrest.utils;

import java.util.Date;

public class GeneradorFechaActualReal implements GeneradorFechaActual {
    public Date getFechaActual() {
        return new Date();
    }
}
