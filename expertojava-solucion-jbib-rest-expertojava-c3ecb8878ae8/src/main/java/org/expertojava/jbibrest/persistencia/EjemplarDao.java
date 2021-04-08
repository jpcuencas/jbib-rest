package org.expertojava.jbibrest.persistencia;

import org.expertojava.jbibrest.modelo.Ejemplar;

import javax.persistence.TypedQuery;
import java.util.List;

public class EjemplarDao extends Dao<Ejemplar, Long> {
    String FIND_ALL_EJEMPLARES = "SELECT e FROM Ejemplar e ";
    String FIND_EJEMPLARES_LIBROID = "SELECT e FROM Ejemplar e WHERE e.libroId = :libroId";
    String FIND_EJEMPLARES_PRESTADOS =
            "SELECT e FROM Ejemplar e, Prestamo p " +
                    "WHERE e.libroId = :libroId " +
                    "      AND p.ejemplar = e";
    String FIND_EJEMPLARES_DISPONBILES =
            "SELECT e FROM Ejemplar e " +
                    "WHERE e.libroId = :libroId AND e NOT IN " +
                    "(SELECT e FROM Ejemplar e, Prestamo p " +
                    "           WHERE e.libroId = :libroId " +
                    "                  AND p.ejemplar = e)";
    String NUM_EJEMPLARES_LIBROID = "SELECT COUNT(e) FROM Ejemplar e WHERE e.libroId = :libroId";
    String NUM_EJEMPLARES_PRESTADOS =
            "SELECT COUNT(e) FROM Ejemplar e, Prestamo p " +
                    "WHERE e.libroId = :libroId " +
                    "      AND p.ejemplar = e";
    String NUM_EJEMPLARES_DISPONBILES =
            "SELECT COUNT(e) FROM Ejemplar e " +
                    "WHERE e.libroId = :libroId AND e NOT IN " +
                    "(SELECT e FROM Ejemplar e, Prestamo p " +
                    "           WHERE e.libroId = :libroId " +
                    "                  AND p.ejemplar = e)";
    @Override
    public Ejemplar find(Long id) {
        return em.find(Ejemplar.class, id);
    }

    public List<Ejemplar> listAllLibros() {
        TypedQuery<Ejemplar> query = em.createQuery(FIND_ALL_EJEMPLARES, Ejemplar.class);
        return query.getResultList();
    }

    public List<Ejemplar> listEjemplaresLibroId(Long libroId) {
        TypedQuery<Ejemplar> query = em.createQuery(FIND_EJEMPLARES_LIBROID, Ejemplar.class);
        return query.setParameter("libroId", libroId).getResultList();
    }

    public List<Ejemplar> listEjemplaresPrestados(Long libroId) {
        TypedQuery<Ejemplar> query = em.createQuery(FIND_EJEMPLARES_PRESTADOS, Ejemplar.class);
        return query.setParameter("libroId", libroId).getResultList();
    }

    public List<Ejemplar> listEjemplaresDisponibles(Long libroId) {
        TypedQuery<Ejemplar> query = em.createQuery(FIND_EJEMPLARES_DISPONBILES, Ejemplar.class);
        return query.setParameter("libroId", libroId).getResultList();
    }

    public long numEjemplaresLibroId(Long libroId) {
        TypedQuery<Long> query = em.createQuery(NUM_EJEMPLARES_LIBROID, Long.class);
        return query.setParameter("libroId", libroId).getSingleResult();
    }

    public long numEjemplaresPrestados(Long libroId) {
        TypedQuery<Long> query = em.createQuery(NUM_EJEMPLARES_PRESTADOS, Long.class);
        return query.setParameter("libroId", libroId).getSingleResult();
    }

    public long numEjemplaresDisponibles(Long libroId) {
        TypedQuery<Long> query = em.createQuery(NUM_EJEMPLARES_DISPONBILES, Long.class);
        return query.setParameter("libroId", libroId).getSingleResult();
    }
}
