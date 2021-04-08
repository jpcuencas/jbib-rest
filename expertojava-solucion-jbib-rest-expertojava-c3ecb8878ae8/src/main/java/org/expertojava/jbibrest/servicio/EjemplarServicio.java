package org.expertojava.jbibrest.servicio;

import org.expertojava.jbibrest.modelo.Ejemplar;
import org.expertojava.jbibrest.persistencia.EjemplarDao;

import javax.inject.Inject;
import javax.transaction.Transactional;

@Transactional
public class EjemplarServicio {

    @Inject
    EjemplarDao ejemplarDao;

    public Ejemplar buscaEjemplar(long ejemplarId) {
        return ejemplarDao.find(ejemplarId);
    }

    public long numEjemplaresTotales(long libroId) {
        return ejemplarDao.numEjemplaresLibroId(libroId);
    }

    public long numEjemplaresPrestados(long libroId) {
        return ejemplarDao.numEjemplaresPrestados(libroId);
    }

    public long numEjemplaresDisponibles(long libroId) {
        return ejemplarDao.numEjemplaresDisponibles(libroId);
    }
}
