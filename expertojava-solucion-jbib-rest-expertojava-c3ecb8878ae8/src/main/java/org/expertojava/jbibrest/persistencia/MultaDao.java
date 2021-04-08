package org.expertojava.jbibrest.persistencia;

import org.expertojava.jbibrest.modelo.Multa;

import javax.persistence.TypedQuery;
import java.util.List;

public class MultaDao extends Dao<Multa, Long> {
    String FIND_ALL_MULTAS = "SELECT m FROM Multa m ";

    @Override
    public Multa find(Long id) {
        return em.find(Multa.class, id);
    }

    public List<Multa> listAllMultas() {
        TypedQuery<Multa> query = em.createQuery(FIND_ALL_MULTAS, Multa.class);
        return query.getResultList();
    }
}
