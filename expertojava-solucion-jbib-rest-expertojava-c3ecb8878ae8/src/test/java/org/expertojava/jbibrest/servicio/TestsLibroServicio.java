package org.expertojava.jbibrest.servicio;

import org.expertojava.jbibrest.modelo.Libro;
import org.expertojava.jbibrest.modelo.Recomendacion;
import org.expertojava.jbibrest.persistencia.LibroDao;
import org.expertojava.jbibrest.persistencia.RecomendacionDao;
import org.expertojava.jbibrest.utils.BibliotecaException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.CleanupStrategy;
import org.jboss.arquillian.persistence.TestExecutionPhase;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class TestsLibroServicio {

    @Inject
    LibroServicio libroServicio;

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(Libro.class.getPackage())
                .addPackage(LibroDao.class.getPackage())
                .addPackage(Recomendacion.class.getPackage())
                .addPackage(RecomendacionDao.class.getPackage())
                .addPackage(LibroServicio.class.getPackage())
                .addPackage(BibliotecaException.class.getPackage())
                .addAsResource("persistence-datasource.xml",
                        "META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void libroServicioNoEsNullTest() {
        assertNotNull(libroServicio);
    }

    @UsingDataSet("dbunit/dataset1.xml")
    @Cleanup(phase = TestExecutionPhase.BEFORE,  strategy = CleanupStrategy.USED_TABLES_ONLY)
    @Test
    public void buscaLibroPorIdTest() {
        Libro libro = libroServicio.buscaLibroPorId(4L);
        assertTrue(libro.getTitulo().equals("Extreme Programming Explained"));
    }

    @UsingDataSet("dbunit/dataset1.xml")
    @Cleanup(phase = TestExecutionPhase.BEFORE,  strategy = CleanupStrategy.USED_TABLES_ONLY)
    @Test
    public void nuevoLibroTest() {
        Libro libro = libroServicio.nuevoLibro("0321826620", "Martin Fowler",  "NoSQL Distilled");
        Libro libroRecuperado = libroServicio.buscaLibroPorId(libro.getId());
        assertTrue(libroRecuperado.getTitulo().equals("NoSQL Distilled"));
    }

    @UsingDataSet("dbunit/dataset1.xml")
    @Cleanup(phase = TestExecutionPhase.BEFORE,  strategy = CleanupStrategy.USED_TABLES_ONLY)
    @Test
    public void listaRecomendacionesTest() {
        List<Recomendacion> recomendaciones = libroServicio.listaRecomendaciones(2L, 3);
        assertTrue(recomendaciones.size() == 2);
    }

    @UsingDataSet("dbunit/dataset1.xml")
    @Cleanup(phase = TestExecutionPhase.BEFORE,  strategy = CleanupStrategy.USED_TABLES_ONLY)
    @Test
    public void buscaLibroPorIsbnTest() {
        Libro libro = libroServicio.buscaLibroIsbn("0132350882");
        assertTrue(libro.getTitulo().equals("Clean Code"));
        assertTrue(libro.getRecomendaciones().size() == 2);
    }

    @UsingDataSet("dbunit/dataset1.xml")
    @Cleanup(phase = TestExecutionPhase.BEFORE,  strategy = CleanupStrategy.USED_TABLES_ONLY)
    @Test
    public void buscaLibrosAutorTest() {
        List<Libro> libros = libroServicio.buscaLibrosAutor("Kent Beck");
        assertTrue(libros.size() == 2);
    }

    @UsingDataSet("dbunit/dataset1.xml")
    @Cleanup(phase = TestExecutionPhase.BEFORE,  strategy = CleanupStrategy.USED_TABLES_ONLY)
    @Test
    public void  buscaLibrosTituloContieneTest() {
        List<Libro> libros = libroServicio.buscaLibrosTituloContiene("Programming");
        assertTrue(libros.size() == 2);
    }

    @UsingDataSet("dbunit/dataset1.xml")
    @Cleanup(phase = TestExecutionPhase.BEFORE,  strategy = CleanupStrategy.USED_TABLES_ONLY)
    @Test
    public void  listaAllLibrosTest() {
        List<Libro> libros = libroServicio.listaLibros();
        assertTrue(libros.size() == 5);
    }
}
