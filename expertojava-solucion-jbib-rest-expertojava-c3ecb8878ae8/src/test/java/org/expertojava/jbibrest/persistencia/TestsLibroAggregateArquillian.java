package org.expertojava.jbibrest.persistencia;

import org.expertojava.jbibrest.modelo.Libro;
import org.expertojava.jbibrest.modelo.Recomendacion;
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

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class TestsLibroAggregateArquillian {

    @Inject
    LibroDao libroDao;
    @Inject
    RecomendacionDao recomendacionDao;

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(Libro.class.getPackage())
                .addPackage(Recomendacion.class.getPackage())
                .addPackage(LibroDao.class.getPackage())
                .addPackage(RecomendacionDao.class.getPackage())
                .addPackage(BibliotecaException.class.getPackage())
                .addAsResource("persistence-datasource.xml",
                        "META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }


    @Test
    public void libroDaoNoEsNullTest() {
        assertNotNull(libroDao);
    }

    @UsingDataSet("dbunit/dataset1.xml")
    @Cleanup(phase = TestExecutionPhase.BEFORE,  strategy = CleanupStrategy.USED_TABLES_ONLY)
    @Test
    public void findLibroIdTest() {
        Libro libro = libroDao.find(2L);
        assertTrue(libro.getIsbn().equals("0132350882"));
        assertTrue(libro.getTitulo().equals("Clean Code"));
        assertTrue(libro.getAutor().equals("Robert C. Martin"));
        assertTrue(libro.getNumPaginas().equals(288));
        assertTrue(libro.getPortadaURI().equals("0132350882.jpg"));
    }

    @UsingDataSet("dbunit/dataset1.xml")
    @Cleanup(phase = TestExecutionPhase.BEFORE,  strategy = CleanupStrategy.USED_TABLES_ONLY)
    @Test
    public void numRecomendacionesTest()  {
        Libro libro = libroDao.find(2L);
        assertTrue(libro.getRecomendaciones().size() == 2);
    }

    @UsingDataSet("dbunit/dataset1.xml")
    @Cleanup(phase = TestExecutionPhase.BEFORE,  strategy = CleanupStrategy.USED_TABLES_ONLY)
    @Test
    public void createLibroTest() {
        String isbn = "123456789";

        Libro libro = new Libro(isbn);
        Libro libroCreado = libroDao.create(libro);
        Long libroId = libroCreado.getId();
        Libro libroRecuperado = libroDao.find(libroId);
        assertTrue(libroRecuperado.getIsbn().equals(isbn));
    }

    @UsingDataSet("dbunit/dataset1.xml")
    @Cleanup(phase = TestExecutionPhase.BEFORE,  strategy = CleanupStrategy.USED_TABLES_ONLY)
    @Test
    public void findLibroDevuelveNullSiNoExiste() {
        Libro libro = libroDao.find(1000L);
        assertNull(libro);
    }

    @UsingDataSet("dbunit/dataset1.xml")
    @Cleanup(phase = TestExecutionPhase.BEFORE,  strategy = CleanupStrategy.USED_TABLES_ONLY)
    @Test
    public void findLibroByIsbnDevuelveNullSiNoExiste() {
        Libro libro = libroDao.findLibroPorIsbn("000000");
        assertNull(libro);
    }

    @UsingDataSet("dbunit/dataset1.xml")
    @Cleanup(phase = TestExecutionPhase.BEFORE,  strategy = CleanupStrategy.USED_TABLES_ONLY)
    @Test
    public void findRecomendacionTest() {
        Recomendacion recomendacion = recomendacionDao.find(1L);
        assertTrue(recomendacion.getLibro().getTitulo().equals("Clean Code"));
        assertTrue(recomendacion.getLibroRelacionado().getTitulo().equals("Test Driven Development"));
    }

    @UsingDataSet("dbunit/dataset1.xml")
    @Cleanup(phase = TestExecutionPhase.BEFORE,  strategy = CleanupStrategy.USED_TABLES_ONLY)
    @Test
    public void añadeRecomendacionTest() {
        Libro libro3 = libroDao.find(3L);
        Libro libro1 = libroDao.find(1L);
        Recomendacion recomendacion = new Recomendacion(libro3, libro1);
        recomendacion = recomendacionDao.create(recomendacion);

        // actualizamos la relación en memoria
        libro3.añadeRecomendacion(recomendacion);

        assertTrue(libro3.getRecomendaciones().size() == 2);
        assertTrue(libro3.getRecomendaciones().contains(recomendacion));
    }
}