package org.expertojava.jbibrest.servicio;

import org.expertojava.jbibrest.modelo.*;
import org.expertojava.jbibrest.persistencia.EjemplarDao;
import org.expertojava.jbibrest.persistencia.MultaDao;
import org.expertojava.jbibrest.persistencia.PrestamoDao;
import org.expertojava.jbibrest.persistencia.UsuarioDao;
import org.expertojava.jbibrest.utils.BibliotecaBR;
import org.expertojava.jbibrest.utils.BibliotecaException;
import org.expertojava.jbibrest.utils.GeneradorFechaActualStub;
import org.expertojava.jbibrest.utils.Utils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.CleanupStrategy;
import org.jboss.arquillian.persistence.TestExecutionPhase;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class TestsUsuarioServicio {

    @Inject
    UsuarioServicio usuarioServicio;
    @Inject
    EjemplarServicio ejemplarServicio;

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(Alumno.class.getPackage())
                .addPackage(Profesor.class.getPackage())
                .addPackage(Direccion.class.getPackage())
                .addPackage(Multa.class.getPackage())
                .addPackage(Prestamo.class.getPackage())
                .addPackage(Usuario.class.getPackage())
                .addPackage(EjemplarDao.class.getPackage())
                .addPackage(MultaDao.class.getPackage())
                .addPackage(PrestamoDao.class.getPackage())
                .addPackage(UsuarioDao.class.getPackage())
                .addPackage(BibliotecaException.class.getPackage())
                .addPackage(UsuarioServicio.class.getPackage())
                .addAsResource("persistence-datasource.xml",
                        "META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @UsingDataSet("dbunit/dataset2.xml")
    @Cleanup(phase = TestExecutionPhase.BEFORE, strategy = CleanupStrategy.USED_TABLES_ONLY)
    @Test
    public void usuarioServicioNoEsNullTest() {
        assertNotNull(usuarioServicio);
    }

    @UsingDataSet("dbunit/dataset2.xml")
    @Cleanup(phase = TestExecutionPhase.BEFORE, strategy = CleanupStrategy.USED_TABLES_ONLY)
    @Test
    public void buscaUsuarioPorIdTest() {
        Usuario usuario = usuarioServicio.buscaUsuarioPorId(1L);
        Assert.assertTrue(usuario.getPrestamos().size() == 2);
        assertNotNull(usuarioServicio);
    }

    @UsingDataSet("dbunit/dataset2.xml")
    @Cleanup(phase = TestExecutionPhase.BEFORE, strategy = CleanupStrategy.USED_TABLES_ONLY)
    @Test
    public void recuperaUsuarioTest() {
        Usuario usuario = usuarioServicio.recuperarUsuario("vicente.casamayor");
        Assert.assertTrue(usuario.getPrestamos().size() == 2);
        assertNotNull(usuarioServicio);
    }

    @UsingDataSet("dbunit/dataset2.xml")
    @Cleanup(phase = TestExecutionPhase.BEFORE, strategy = CleanupStrategy.USED_TABLES_ONLY)
    @Test
    public void prestamosActivosTest() {
        List<Prestamo> prestamos = usuarioServicio.prestamosActivos(1L);
        Assert.assertTrue(prestamos.size() == 2);
    }

    @UsingDataSet("dbunit/dataset2.xml")
    @Cleanup(phase = TestExecutionPhase.BEFORE, strategy = CleanupStrategy.USED_TABLES_ONLY)
    @Test
    public void solicitarPrestamoTest() {
        GeneradorFechaActualStub generadorFechaActual = new GeneradorFechaActualStub();
        generadorFechaActual.setFechaActual(Utils.stringToDate("2014-11-02"));

        // Actualizamos el generador de fecha actual en BibliotecaBR para que el
        // método de negocio obtenga esa fecha
        BibliotecaBR.getInstance().setGeneradorFechaActual(generadorFechaActual);

        Prestamo prestamo = usuarioServicio.solicitaPrestamo(1L, 1L);
        Usuario usuario = usuarioServicio.buscaUsuarioPorId(1L);
        assertTrue(prestamo.getEjemplar().getLibroId() == 1L);
        assertTrue(usuario.getPrestamos().contains(prestamo));
    }

    @UsingDataSet("dbunit/dataset2.xml")
    @Cleanup(phase = TestExecutionPhase.BEFORE, strategy = CleanupStrategy.USED_TABLES_ONLY)
    @Test(expected = BibliotecaException.class)
    public void solicitarPrestamoDevuelveExcepcionSiUsuarioMultado() {
        GeneradorFechaActualStub generadorFechaActual = new GeneradorFechaActualStub();
        generadorFechaActual.setFechaActual(Utils.stringToDate("2014-11-02"));

        // Actualizamos el generador de fecha actual en BibliotecaBR para que el
        // método de negocio obtenga esa fecha
        BibliotecaBR.getInstance().setGeneradorFechaActual(generadorFechaActual);

        // Anabel Garcia está multada
        Prestamo prestamo = usuarioServicio.solicitaPrestamo(3L, 1L);
    }

    @UsingDataSet("dbunit/dataset2.xml")
    @Cleanup(phase = TestExecutionPhase.BEFORE, strategy = CleanupStrategy.USED_TABLES_ONLY)
    @Test
    public void solicitarPrestamoIsbnTest() {
        GeneradorFechaActualStub generadorFechaActual = new GeneradorFechaActualStub();
        generadorFechaActual.setFechaActual(Utils.stringToDate("2014-11-02"));

        // Actualizamos el generador de fecha actual en BibliotecaBR para que el
        // método de negocio obtenga esa fecha
        BibliotecaBR.getInstance().setGeneradorFechaActual(generadorFechaActual);

        Prestamo prestamo = usuarioServicio.solicitaPrestamoIsbn(1L, "0321127420");
        Usuario usuario = usuarioServicio.buscaUsuarioPorId(1L);
        assertTrue(prestamo.getEjemplar().getLibroId() == 1);
        assertTrue(usuario.getPrestamos().contains(prestamo));
    }

    @UsingDataSet("dbunit/dataset2.xml")
    @Cleanup(phase = TestExecutionPhase.BEFORE, strategy = CleanupStrategy.USED_TABLES_ONLY)
    @Test
    public void devolverPrestamoATiempoTest() {
        long idEjemplar = 3L;
        long idUsuario = 1L;

        GeneradorFechaActualStub generadorFechaActual = new GeneradorFechaActualStub();
        generadorFechaActual.setFechaActual(Utils.stringToDate("2014-11-02"));
        // Actualizamos el generador de fecha actual en BibliotecaBR para que el
        // método de negocio obtenga esa fecha
        BibliotecaBR.getInstance().setGeneradorFechaActual(generadorFechaActual);
        Usuario usuario = usuarioServicio.buscaUsuarioPorId(idUsuario);
        int numPrestamos = usuario.getPrestamos().size();

        // La devolución debe ser correcta
        ResultadoDevolucion resultado = usuarioServicio.devolverPrestamo(idEjemplar);
        assertTrue(resultado == ResultadoDevolucion.DEVOLUCION_CORRECTA);

        // El número de préstamos del usuario debe ser uno menos
        usuario = usuarioServicio.buscaUsuarioPorId(idUsuario);
        assertTrue(usuario.getPrestamos().size() == numPrestamos - 1);

        // El ejemplar ya no tiene que tener préstamo asociado
        Ejemplar ejemplar = ejemplarServicio.buscaEjemplar(idEjemplar);
        assertNull(ejemplar.getPrestamo());
    }

    @UsingDataSet("dbunit/dataset2.xml")
    @Cleanup(phase = TestExecutionPhase.BEFORE, strategy = CleanupStrategy.USED_TABLES_ONLY)
    @Test
    public void devolverUnicoPrestamoFueraDePlazo() {
        long idEjemplar = 4L;
        long idUsuario = 2L;
        // Fecha de devolucion 2 días tarde
        Date fechaActual = Utils.stringToDate("2014-12-07");

        GeneradorFechaActualStub generadorFechaActual = new GeneradorFechaActualStub();
        generadorFechaActual.setFechaActual(fechaActual);
        // Actualizamos el generador de fecha actual en BibliotecaBR para que el
        // método de negocio obtenga esa fecha
        BibliotecaBR.getInstance().setGeneradorFechaActual(generadorFechaActual);

        Usuario usuario = usuarioServicio.buscaUsuarioPorId(idUsuario);
        int numPrestamos = usuario.getPrestamos().size();

        // Devolvemos fuera de plazo el ejemplar
        // La devolución debe ser FUERA DE PLAZO
        ResultadoDevolucion resultado = usuarioServicio.devolverPrestamo(idEjemplar);
        assertTrue(resultado == ResultadoDevolucion.DEVOLUCION_FUERA_DE_PLAZO);

        // El ejemplar ya no tiene que tener préstamo asociado
        Ejemplar ejemplar = ejemplarServicio.buscaEjemplar(idEjemplar);
        assertNull(ejemplar.getPrestamo());

        // El número de préstamos del usuario debe ser uno menos
        // el usuario debe tener una multa y su estado debe ser MULTADO
        usuario = usuarioServicio.buscaUsuarioPorId(idUsuario);
        assertTrue(usuario.getPrestamos().size() == numPrestamos - 1);
        assertTrue(usuario.getEstado(fechaActual) == EstadoUsuario.MULTADO);

        // La multa tiene que tener la fecha cerrada y
        // debe ser la suma de los días de retraso al día de devolución
        Date fechaFinalizacionMulta = usuario.getMulta().getHasta();
        assertTrue(fechaFinalizacionMulta.equals(Utils.stringToDate("2014-12-09")));
    }

    @UsingDataSet("dbunit/dataset2.xml")
    @Cleanup(phase = TestExecutionPhase.BEFORE, strategy = CleanupStrategy.USED_TABLES_ONLY)
    @Test
    public void devolverVariosPrestamosFueraDePlazo() {
        long idEjemplar1 = 2L;
        long idEjemplar2 = 3L;
        long idUsuario = 1L;
        // Fecha de devolucion ejemplar1 (2 días tarde)
        Date fechaDevolucion1 = Utils.stringToDate("2014-12-02");
        // Fecha de devolucion ejemplar2 (4 dias tarde)
        Date fechaDevolucion2 = Utils.stringToDate("2014-12-04");

        GeneradorFechaActualStub generadorFechaActual = new GeneradorFechaActualStub();
        generadorFechaActual.setFechaActual(fechaDevolucion1);
        // Actualizamos el generador de fecha actual en BibliotecaBR para que el
        // método de negocio obtenga esa fecha
        BibliotecaBR.getInstance().setGeneradorFechaActual(generadorFechaActual);

        Usuario usuario = usuarioServicio.buscaUsuarioPorId(idUsuario);

        // Devolvemos ejemplar1 fuera de plazo
        ResultadoDevolucion resultado = usuarioServicio.devolverPrestamo(idEjemplar1);

        assertTrue(resultado == ResultadoDevolucion.DEVOLUCION_FUERA_DE_PLAZO);
        usuario = usuarioServicio.buscaUsuarioPorId(idUsuario);
        // La multa debe tener abierta la fecha de finalización
        // y debe tener el número de días acumulados igual a 2
        assertTrue(usuario.getMulta().getDiasAcumulados() == 2);

        // Devolvemos ejemplar2 fuera de plazo
        generadorFechaActual.setFechaActual(fechaDevolucion2);
        resultado = usuarioServicio.devolverPrestamo(idEjemplar2);

        assertTrue(resultado == ResultadoDevolucion.DEVOLUCION_FUERA_DE_PLAZO);
        usuario = usuarioServicio.buscaUsuarioPorId(idUsuario);
        // La multa tiene que tener la fecha cerrada y
        // debe ser la suma de los días de retraso al día de devolución
        Date fechaFinalizacionMulta = usuario.getMulta().getHasta();
        assertTrue(fechaFinalizacionMulta.equals(Utils.stringToDate("2014-12-10")));

    }
}
