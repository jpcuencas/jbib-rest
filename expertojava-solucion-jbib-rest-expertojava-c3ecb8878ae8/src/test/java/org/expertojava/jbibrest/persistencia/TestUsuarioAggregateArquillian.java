package org.expertojava.jbibrest.persistencia;

import org.expertojava.jbibrest.modelo.*;
import org.expertojava.jbibrest.utils.BibliotecaException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
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

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class TestUsuarioAggregateArquillian {

    @Inject
    UsuarioDao usuarioDao;
    @Inject
    PrestamoDao prestamoDao;
    @Inject
    EjemplarDao ejemplarDao;
    @Inject
    MultaDao multaDao;

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(Alumno.class.getPackage())
                .addPackage(Profesor.class.getPackage())
                .addPackage(Direccion.class.getPackage())
                .addPackage(Multa.class.getPackage())
                .addPackage(Prestamo.class.getPackage())
                .addPackage(Usuario.class.getPackage())
                .addPackage(Dao.class.getPackage())
                .addPackage(EjemplarDao.class.getPackage())
                .addPackage(MultaDao.class.getPackage())
                .addPackage(PrestamoDao.class.getPackage())
                .addPackage(UsuarioDao.class.getPackage())
                .addPackage(BibliotecaException.class.getPackage())
                .addAsResource("persistence-datasource.xml",
                        "META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @UsingDataSet("dbunit/dataset2.xml")
    @Cleanup(phase = TestExecutionPhase.BEFORE, strategy = CleanupStrategy.USED_TABLES_ONLY)
    @Test
    public void daosNoSonNullTest() {
        assertNotNull(usuarioDao);
        assertNotNull(prestamoDao);
        assertNotNull(ejemplarDao);
        assertNotNull(multaDao);
    }

    // Usuario

    @UsingDataSet("dbunit/dataset2.xml")
    @Cleanup(phase = TestExecutionPhase.BEFORE, strategy = CleanupStrategy.USED_TABLES_ONLY)
    @Test
    public void usuarioRecuperadoContienePrestamos() {
        Usuario usuario = usuarioDao.find(1L);
        assertTrue(usuario.getPrestamos().size() == 2);
    }

    @UsingDataSet("dbunit/dataset2.xml")
    @Cleanup(phase = TestExecutionPhase.BEFORE, strategy = CleanupStrategy.USED_TABLES_ONLY)
    @Test
    public void usuarioFindDevuelveNullSiNoExiste() {
        Usuario usuario = usuarioDao.find(1000L);
        assertNull(usuario);
    }

    @UsingDataSet("dbunit/dataset2.xml")
    @Cleanup(phase = TestExecutionPhase.BEFORE, strategy = CleanupStrategy.USED_TABLES_ONLY)
    @Test
    public void usuarioByLoginDevuelveNullSiNoExiste() {
        Usuario usuario = usuarioDao.findUsuarioPorLogin("KKKKK");
        assertNull(usuario);
    }

    @UsingDataSet("dbunit/dataset2.xml")
    @Cleanup(phase = TestExecutionPhase.BEFORE, strategy = CleanupStrategy.USED_TABLES_ONLY)
    @Test
    public void usuarioRecuperadoContieneMulta() {
        Usuario usuario = usuarioDao.find(3L);
        assertTrue(usuario.getMulta().getId() == 1L);
    }


    @Cleanup(phase = TestExecutionPhase.BEFORE,  strategy = CleanupStrategy.USED_TABLES_ONLY)
    @UsingDataSet("dbunit/dataset2.xml")
    @Test
    @InSequence(1)
    public void eliminaPrestamoDeUsuarioyEjemplar() {
        Usuario usuario = usuarioDao.find(1L);
        Ejemplar ejemplar = ejemplarDao.find(2L);
        Prestamo prestamo = prestamoDao.find(3L);
        usuario.quitaPrestamo(prestamo);
        usuarioDao.update(usuario);
        ejemplar.quitaPrestamo();
        ejemplarDao.update(ejemplar);
        prestamoDao.delete(prestamo);

        // Comprobamos que la relación se ha actualizado en memoria

        assertFalse(usuario.getPrestamos().contains(prestamo));
        assertNull(ejemplar.getPrestamo());
    }

    @Cleanup(phase = TestExecutionPhase.NONE)
    @Test
    @InSequence(2)
    public void compruebaPrestamoEliminado() {
        Prestamo prestamo = prestamoDao.find(3L);
        Usuario usuario = usuarioDao.find(1L);
        Ejemplar ejemplar = ejemplarDao.find(2L);

        assertFalse(usuario.getPrestamos().contains(prestamo));
        assertNull(ejemplar.getPrestamo());
        assertNull(prestamo);
    }

    @Cleanup(phase = TestExecutionPhase.BEFORE,  strategy = CleanupStrategy.USED_TABLES_ONLY)
    @UsingDataSet("dbunit/dataset2.xml")
    @Test
    @InSequence(3)
    public void eliminaMultaDeUsuario() {
        Usuario usuario = usuarioDao.find(3L);
        Multa multa = usuario.getMulta();
        assertTrue(multa.getId() == 1L);
        usuario.quitaMulta();
        usuarioDao.update(usuario);
        multaDao.delete(multa);
        assertNull(usuario.getMulta());
    }

    @Cleanup(phase = TestExecutionPhase.NONE)
    @Test
    @InSequence(4)
    public void compuebaMultaEliminada() {
        Multa multa = multaDao.find(1L);
        Usuario usuario = usuarioDao.find(3L);
        assertNull(usuario.getMulta());
        assertNull(multa);
    }

    // Ejemplar

    @Cleanup(phase = TestExecutionPhase.BEFORE,  strategy = CleanupStrategy.USED_TABLES_ONLY)
    @UsingDataSet("dbunit/dataset2.xml")
    @Test
    public void compuebaEjemplaresTotalesPrestadosYDisponibles() {
        List<Ejemplar> ejemplaresLibroId = ejemplarDao.listEjemplaresLibroId(1L);
        List<Ejemplar> ejemplaresPrestados = ejemplarDao.listEjemplaresPrestados(1L);
        List<Ejemplar> ejemplaresDisponbiles = ejemplarDao.listEjemplaresDisponibles(1L);
        assertTrue(ejemplaresLibroId.size() == 3);
        assertTrue(ejemplaresPrestados.size() == 1);
        assertTrue(ejemplaresDisponbiles.size() == 2);
    }


    @Cleanup(phase = TestExecutionPhase.BEFORE,  strategy = CleanupStrategy.USED_TABLES_ONLY)
    @UsingDataSet("dbunit/dataset2.xml")
    @Test
    public void compuebaNumEjemplaresTotalesPrestadosYDisponibles() {
        long numEjemplaresLibroId = ejemplarDao.numEjemplaresLibroId(1L);
        long numEjemplaresPrestados = ejemplarDao.numEjemplaresPrestados(1L);
        long numEjemplaresDisponbiles = ejemplarDao.numEjemplaresDisponibles(1L);
        assertTrue(numEjemplaresLibroId == 3);
        assertTrue(numEjemplaresPrestados == 1);
        assertTrue(numEjemplaresDisponbiles == 2);
    }
}