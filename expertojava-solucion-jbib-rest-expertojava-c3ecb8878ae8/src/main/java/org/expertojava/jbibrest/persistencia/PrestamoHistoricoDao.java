package org.expertojava.jbibrest.persistencia;

import org.expertojava.jbibrest.modelo.PrestamoHistorico;

import javax.persistence.TypedQuery;
import java.util.List;

public class PrestamoHistoricoDao extends Dao<PrestamoHistorico, Long> {
    String FIND_ALL_PRESTAMOSHISTORICOS = "SELECT p FROM PrestamoHistorico p";

    @Override
    public PrestamoHistorico find(Long id) {
        return em.find(PrestamoHistorico.class, id);
    }

    public List<PrestamoHistorico> listAllPrestamoHistorico() {
        TypedQuery<PrestamoHistorico> query = em.createQuery(FIND_ALL_PRESTAMOSHISTORICOS, PrestamoHistorico.class);
        return query.getResultList();
    }
}
