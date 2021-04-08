package org.expertojava.jbibrest.persistencia;


import org.expertojava.jbibrest.modelo.Recomendacion;

import javax.persistence.TypedQuery;
import java.util.List;

public class RecomendacionDao extends Dao<Recomendacion, Long> {
    String FIND_ALL_RECOMENDACIONES = "SELECT r FROM Recomendacion r ";

    @Override
    public Recomendacion find(Long id) {
        return em.find(Recomendacion.class, id);
    }

    public List<Recomendacion> listAllRecomendaciones() {
        TypedQuery<Recomendacion> query = em.createQuery(FIND_ALL_RECOMENDACIONES, Recomendacion.class);
        return query.getResultList();
    }
}
