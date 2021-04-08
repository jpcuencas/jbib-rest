package org.expertojava.jbibrest.servicio;

import org.expertojava.jbibrest.modelo.*;
import org.expertojava.jbibrest.persistencia.EjemplarDao;
import org.expertojava.jbibrest.persistencia.MultaDao;
import org.expertojava.jbibrest.persistencia.PrestamoDao;
import org.expertojava.jbibrest.persistencia.UsuarioDao;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class TestsEjemplarServicio {

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
    public void ejemplarServicioNoEsNullTest() {
        assertNotNull(ejemplarServicio);
    }

    @UsingDataSet("dbunit/dataset2.xml")
    @Cleanup(phase = TestExecutionPhase.BEFORE, strategy = CleanupStrategy.USED_TABLES_ONLY)
    @Test
    public void numEjemplaresTotalesTest() {
        long numEjemplaresTotales = ejemplarServicio.numEjemplaresTotales(1L);
        assertEquals(3L, numEjemplaresTotales);
    }

    @UsingDataSet("dbunit/dataset2.xml")
    @Cleanup(phase = TestExecutionPhase.BEFORE, strategy = CleanupStrategy.USED_TABLES_ONLY)
    @Test
    public void numEjemplaresDisponiblesTest() {
        long numEjemplaresDisponibles = ejemplarServicio.numEjemplaresDisponibles(1L);
        assertEquals(2L, numEjemplaresDisponibles);
    }
}
