package org.expertojava.jbibrest.servicio;

import org.expertojava.jbibrest.modelo.Libro;
import org.expertojava.jbibrest.modelo.Recomendacion;
import org.expertojava.jbibrest.persistencia.LibroDao;
import org.expertojava.jbibrest.persistencia.RecomendacionDao;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
public class LibroServicio {
    @Inject
    LibroDao libroDao;
    @Inject
    RecomendacionDao recomendacionDao;

    public Libro nuevoLibro(String isbn, String autor, String titulo) {
        if (isbn == null || isbn.equals("")) {
            throw new IllegalArgumentException("ISBN no puede ser vacío");
        }
        if (autor == null || autor.equals("")) {
            throw new IllegalArgumentException("Autor no puede ser vacío");
        }
        if (titulo == null || titulo.equals("")) {
            throw new IllegalArgumentException("Título no puede ser vacío");
        }
        Libro libro = new Libro(isbn);
        libro.setAutor(autor);
        libro.setTitulo(titulo);
        libro = libroDao.create(libro);
        return libro;
    }

    public Libro buscaLibroPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id no puede ser null");
        }
        return libroDao.find(id);
    }

    public List<Recomendacion> listaRecomendaciones(Long libroId, int n) {
        List<Recomendacion> listaDevuelta = new ArrayList<>();
        Libro libro = libroDao.find(libroId);
        int anyadidos = 0;
        for (Recomendacion rec : libro.getRecomendaciones()) {
            if (anyadidos == n) break;
            listaDevuelta.add(rec);
            anyadidos++;
        }
        return listaDevuelta;
    }

    public Libro buscaLibroIsbn(String isbn) {
        if (isbn == null || isbn.equals("")) {
            throw new IllegalArgumentException("El isbn no puede ser vacío");
        }
        return libroDao.findLibroPorIsbn(isbn);
    }

    public List<Libro> listaLibros() {
        return libroDao.listAllLibros();
    }

    public List<Libro> buscaLibrosAutor(String autorStr) {
        if (autorStr == null || autorStr.equals("")) {
            throw new IllegalArgumentException("El nombre del autor no puede ser vacío");
        }
        return libroDao.findLibrosPorAutor(autorStr);
    }

    public List<Libro> buscaLibrosTituloContiene(String tituloStr) {
        if (tituloStr == null || tituloStr.equals("")) {
            throw new IllegalArgumentException("La cadena del título no puede ser vacía");
        }
        return libroDao.findLibrosTituloContiene(tituloStr);
    }
}
