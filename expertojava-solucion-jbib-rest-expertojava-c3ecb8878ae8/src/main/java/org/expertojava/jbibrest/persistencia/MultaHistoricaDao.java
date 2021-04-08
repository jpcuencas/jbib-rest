package org.expertojava.jbibrest.persistencia;

import org.expertojava.jbibrest.modelo.MultaHistorica;

import javax.persistence.TypedQuery;
import java.util.List;

public class MultaHistoricaDao extends Dao<MultaHistorica, Long> {
    String FIND_ALL_MULTASHISTORICAS = "SELECT m FROM MultaHistorica m";

    @Override
    public MultaHistorica find(Long id) {
        return em.find(MultaHistorica.class, id);
    }

    public List<MultaHistorica> listAllMultasHistoricas() {
        TypedQuery<MultaHistorica> query = em.createQuery(FIND_ALL_MULTASHISTORICAS, MultaHistorica.class);
        return query.getResultList();
    }
}
