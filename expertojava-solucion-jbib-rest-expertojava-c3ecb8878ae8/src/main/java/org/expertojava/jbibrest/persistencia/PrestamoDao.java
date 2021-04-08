package org.expertojava.jbibrest.persistencia;

import org.expertojava.jbibrest.modelo.Prestamo;

import javax.persistence.TypedQuery;
import java.util.List;

public class PrestamoDao extends Dao<Prestamo, Long> {
    String FIND_ALL_PRESTAMOS = "SELECT p FROM Prestamo p";

    @Override
    public Prestamo find(Long id) {
        return em.find(Prestamo.class, id);
    }

    public List<Prestamo> listAllPrestamos() {
        TypedQuery<Prestamo> query = em.createQuery(FIND_ALL_PRESTAMOS, Prestamo.class);
        return query.getResultList();
    }
}
