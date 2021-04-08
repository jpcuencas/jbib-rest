package org.expertojava.jbibrest.persistencia;


import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.expertojava.jbibrest.modelo.Libro;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.validation.ConstraintViolationException;
import java.sql.Connection;
import java.sql.DriverManager;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

//
// Tests del aggregate formado por las entidades Libro y Recomendacion
//

public class TestsLibroAggregate {
    private static EntityManagerFactory emf;
    private static IDatabaseConnection connection;
    private static IDataSet dataset;

    @BeforeClass
    public static void initAllTests() {
        try {
            // Inicializamos sólo una vez el emf antes de todos los tests
            emf = Persistence.createEntityManagerFactory("biblioteca-local");

            // Inicializamos la conexión a la BD necesaria para
            // que DBUnit cargue los datos de los tests
            Class.forName("com.mysql.jdbc.Driver");
            Connection jdbcConnection = (Connection) DriverManager
                    .getConnection(
                            "jdbc:mysql://localhost:3306/biblioteca",
                            "root", "expertojava");
            connection = new DatabaseConnection(jdbcConnection);

            // 2 líneas para eliminar el warning
            DatabaseConfig dbConfig = connection.getConfig();
            dbConfig.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MySqlDataTypeFactory());

            // Inicializamos el dataset
            FlatXmlDataSetBuilder flatXmlDataSetBuilder =
                    new FlatXmlDataSetBuilder();
            flatXmlDataSetBuilder.setColumnSensing(true);
            dataset = flatXmlDataSetBuilder.build(Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream("dbunit/dataset1.xml"));
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Excepción al inicializar el emf y DbUnit");
        }
    }

    // Se ejecuta antes de cada test
    @Before
    public void cleanDB() throws Exception {
        // Se hace un "clean insert" de los datos de prueba
        // definidos en el fichero XML. El "clean insert" vacía las
        // tablas de los datos de prueba y después inserta los datos
        DatabaseOperation.CLEAN_INSERT.execute(connection, dataset);
    }

    /*
     * Tests de entidades Libro y Recomendacion
     */

    @Test
    public void createLibroTest() {
        String isbn = "123456789";

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Libro libro = new Libro(isbn);
        em.persist(libro);
        em.getTransaction().commit();

        Long libroId = libro.getId();

        em = emf.createEntityManager();
        Libro libroRecuperado = em.find(Libro.class, libroId);
        assertTrue(libroRecuperado.getIsbn().equals(isbn));
        em.close();
    }

    @Test
    public void findLibroIdTest() {
        EntityManager em = emf.createEntityManager();
        Libro libro = em.find(Libro.class, 2L);
        em.close();

        assertTrue(libro.getIsbn().equals("0132350882"));
        assertTrue(libro.getTitulo().equals("Clean Code"));
        assertTrue(libro.getAutor().equals("Robert C. Martin"));
        assertTrue(libro.getNumPaginas().equals(288));
        assertTrue(libro.getPortadaURI().equals("0132350882.jpg"));
    }

    @Test
    public void numRecomendacionesTest() {

        EntityManager em = emf.createEntityManager();
        Libro libro = em.find(Libro.class, 2L);
        em.close();

        assertTrue(libro.getRecomendaciones().size() == 2);
    }

    @Test(expected = ConstraintViolationException.class)
    public void errorValidacionNumPaginasNegativasTest() {
        Long id = 2L;

        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Libro libro = em.find(Libro.class, id);
            libro.setNumPaginas(-1);
            em.flush();
            em.getTransaction().commit();
        } catch (ConstraintViolationException ex){
            em.getTransaction().rollback();
            throw ex;
        }
        em.close();
    }

    // Se ejecuta una vez después de todos los tests
    @AfterClass
    public static void closeEntityManagerFactory() throws Exception {
        // Borramos todos los datos y cerramos la conexión
        //DatabaseOperation.DELETE_ALL.execute(connection, dataset);
        if (emf != null)
            emf.close();
    }
}
