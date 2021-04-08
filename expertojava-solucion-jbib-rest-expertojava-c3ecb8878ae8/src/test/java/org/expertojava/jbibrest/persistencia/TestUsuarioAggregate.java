package org.expertojava.jbibrest.persistencia;


import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.expertojava.jbibrest.modelo.Ejemplar;
import org.expertojava.jbibrest.modelo.Multa;
import org.expertojava.jbibrest.modelo.Prestamo;
import org.expertojava.jbibrest.modelo.Usuario;
import org.expertojava.jbibrest.utils.BibliotecaBR;
import org.expertojava.jbibrest.utils.BibliotecaException;
import org.expertojava.jbibrest.utils.Utils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Date;

import static org.junit.Assert.*;

//
// Tests del aggregate formado por las entidades Usuario, Prestamo
// Ejemplar y Multa
//

public class TestUsuarioAggregate {
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
            Connection jdbcConnection = DriverManager
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
                    .getResourceAsStream("dbunit/dataset2.xml"));
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

    @Test
    public void cargaDataSetTest() {
        assertTrue(true);
    }

    @Test
    public void usuarioRecuperadoContienePrestamos() {
        EntityManager em = emf.createEntityManager();
        Usuario usuario = em.find(Usuario.class, 1L);
        em.close();
        assertTrue(usuario.getPrestamos().size() == 2);
    }

    @Test
    public void usuarioRecuperadoContieneMulta() {
        EntityManager em = emf.createEntityManager();
        Usuario usuario = em.find(Usuario.class, 3L);
        em.close();
        assertTrue(usuario.getMulta().getId() == 1L);
    }

    @Test
    public void eliminaPrestamoDeUsuarioyEjemplar() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Usuario usuario = em.find(Usuario.class, 1L);
        Ejemplar ejemplar = em.find(Ejemplar.class, 2L);
        Prestamo prestamo = em.find(Prestamo.class, 3L);
        usuario.quitaPrestamo(prestamo);
        ejemplar.quitaPrestamo();
        em.remove(prestamo);
        em.getTransaction().commit();
        em.close();

        // Comprobamos que la relación se ha actualizado en memoria

        assertFalse(usuario.getPrestamos().contains(prestamo));
        assertNull(ejemplar.getPrestamo());

        // Comprobamos que la relación se ha actualizado en BD

        em = emf.createEntityManager();
        prestamo = em.find(Prestamo.class, 3L);
        usuario = em.find(Usuario.class, 1L);
        ejemplar = em.find(Ejemplar.class, 2L);
        em.close();

        assertFalse(usuario.getPrestamos().contains(prestamo));
        assertNull(ejemplar.getPrestamo());
        assertNull(prestamo);
    }

    @Test(expected = BibliotecaException.class)
    public void eliminaPrestamoDeUsuarioLanzaExcepcionCuandoPrestamoNoEsDeUsuario() {
            EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Usuario usuario = em.find(Usuario.class, 1L);
            Prestamo prestamo = em.find(Prestamo.class, 2L);
            usuario.quitaPrestamo(prestamo);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
            throw ex;
        }
    }

    @Test
    public void eliminaMultaDeUsuario() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Usuario usuario = em.find(Usuario.class, 3L);
        Multa multa = usuario.getMulta();
        assertTrue(multa.getId() == 1L);
        usuario.quitaMulta();
        em.remove(multa);
        em.getTransaction().commit();
        em.close();

        // Comprobamos que la relación se ha actualizado en memoria

        assertNull(usuario.getMulta());

        // Comprobamos que la relación se ha actualizado en BD

        em = emf.createEntityManager();
        multa = em.find(Multa.class, 1L);
        usuario = em.find(Usuario.class, 3L);
        em.close();
        assertNull(usuario.getMulta());
        assertNull(multa);
    }

    @Test
    public void prestamoCreadoConUsuarioyEjemplarActualizaCorrectamenteEntidades() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Ejemplar ejemplar = em.find(Ejemplar.class, 1L);
        Usuario usuario = em.find(Usuario.class, 2L);
        Date fechaInicio = Utils.stringToDate("08-11-2015");
        Date fechaDevolucion = BibliotecaBR.getInstance().fechaDevolucionPrestamo(usuario, fechaInicio);
        Prestamo prestamo = new Prestamo(usuario, ejemplar, fechaInicio, fechaDevolucion);
        em.persist(prestamo);
        em.flush();
        Long prestamoId = prestamo.getId();
        usuario.añadePrestamo(prestamo);
        ejemplar.setPrestamo(prestamo);
        em.getTransaction().commit();
        em.close();

        // Comprobamos que la relación se ha actualizado en memoria

        assertTrue(ejemplar.getPrestamo().equals(prestamo));
        assertTrue(usuario.getPrestamos().contains(prestamo));

        // Comprobamos que la relación se ha actualizado en BD

        em = emf.createEntityManager();
        prestamo = em.find(Prestamo.class, prestamoId);
        ejemplar = em.find(Ejemplar.class, 1L);
        usuario = em.find(Usuario.class, 2L);
        em.close();
        assertTrue(ejemplar.getPrestamo().equals(prestamo));
        assertTrue(usuario.getPrestamos().contains(prestamo));
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
