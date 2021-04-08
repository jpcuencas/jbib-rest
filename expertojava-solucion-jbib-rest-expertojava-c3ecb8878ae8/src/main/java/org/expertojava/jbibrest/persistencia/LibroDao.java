package org.expertojava.jbibrest.persistencia;

import org.expertojava.jbibrest.modelo.Libro;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

public class LibroDao extends Dao<Libro, Long> {
    String FIND_ALL_AUTORES = "SELECT l FROM Libro l ";
    String FIND_LIBRO_POR_ISBN = "SELECT l FROM Libro l WHERE l.isbn = :isbn";
    String FIND_LIBRO_POR_AUTOR = "SELECT l FROM Libro l WHERE l.autor = :autor";
    String FIND_LIBRO_TITULO_CONTIENE = "SELECT l FROM Libro l WHERE l.titulo LIKE :patron";

    @Override
    public Libro find(Long id) {
        return em.find(Libro.class, id);
    }

    public Libro findLibroPorIsbn(String isbn) {
        Libro libro;
        TypedQuery<Libro> query = em.createQuery(FIND_LIBRO_POR_ISBN, Libro.class)
                .setParameter("isbn", isbn);
        try {
            libro = query.getSingleResult();
        } catch (NoResultException ex) {
            libro = null;
        }
        return libro;
    }

    public List<Libro> listAllLibros() {
        TypedQuery<Libro> query = em.createQuery(FIND_ALL_AUTORES, Libro.class);
        return query.getResultList();
    }

    public List<Libro> findLibrosPorAutor(String autorStr) {
        TypedQuery<Libro> query = em.createQuery(FIND_LIBRO_POR_AUTOR, Libro.class)
                .setParameter("autor", autorStr);
        return query.getResultList();
    }

    public List<Libro> findLibrosTituloContiene(String tituloStr) {
        TypedQuery<Libro> query = em.createQuery(FIND_LIBRO_TITULO_CONTIENE, Libro.class)
                .setParameter("patron", '%'+tituloStr+'%');
        return query.getResultList();
    }
}
