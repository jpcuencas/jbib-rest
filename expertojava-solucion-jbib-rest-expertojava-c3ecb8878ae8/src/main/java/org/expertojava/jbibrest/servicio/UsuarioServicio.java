package org.expertojava.jbibrest.servicio;

import org.expertojava.jbibrest.modelo.*;
import org.expertojava.jbibrest.persistencia.*;
import org.expertojava.jbibrest.utils.BibliotecaBR;
import org.expertojava.jbibrest.utils.BibliotecaException;
import org.expertojava.jbibrest.utils.Utils;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional
public class UsuarioServicio {

    @Inject
    UsuarioDao usuarioDao;
    @Inject
    LibroDao libroDao;
    @Inject
    EjemplarDao ejemplarDao;
    @Inject
    PrestamoDao prestamoDao;
    @Inject
    PrestamoHistoricoDao prestamoHistoricoDao;
    @Inject
    MultaDao multaDao;

    public Usuario buscaUsuarioPorId(long usuarioId) {
        return usuarioDao.find(usuarioId);
    }

    public Usuario recuperarUsuario(String login) {
       return usuarioDao.findUsuarioPorLogin(login);
    }

    public List<Prestamo> prestamosActivos(long usuarioId) {
        Usuario usuario = usuarioDao.find(usuarioId);
        if (usuario == null)
            throw new BibliotecaException(BibliotecaException.USUARIO_NO_EXISTENTE);

        List<Prestamo> prestamosActivos = new ArrayList<>();
        for (Prestamo prestamo : usuario.getPrestamos()) {
            prestamosActivos.add(prestamo);
        }
        return prestamosActivos;
    }

    public Prestamo solicitaPrestamo(long usuarioId, long libroId) {
        Libro libro = libroDao.find(libroId);
        List<Ejemplar> disponibles = ejemplarDao.listEjemplaresDisponibles(libro.getId());
        if (disponibles.size() == 0)
            throw new BibliotecaException(BibliotecaException.NO_HAY_EJEMPLARES_DISPONIBLES);
        Ejemplar ejemplar = disponibles.get(0);
        return this.realizarPrestamo(usuarioId, ejemplar.getId());
    }

    public Prestamo solicitaPrestamoIsbn(long usuarioId, String isbn) {
        Libro libro = libroDao.findLibroPorIsbn(isbn);
        List<Ejemplar> disponibles = ejemplarDao.listEjemplaresDisponibles(libro.getId());
        if (disponibles.size() == 0)
            throw new BibliotecaException(BibliotecaException.NO_HAY_EJEMPLARES_DISPONIBLES);
        Ejemplar ejemplar = disponibles.get(0);
        return this.realizarPrestamo(usuarioId, ejemplar.getId());
    }

    public Prestamo realizarPrestamo(long usuarioId, long ejemplarId) {
        Ejemplar ejemplar = ejemplarDao.find(ejemplarId);
        if (ejemplar == null)
            throw new BibliotecaException(BibliotecaException.EJEMPLAR_NO_EXISTENTE);
        Usuario usuario = usuarioDao.find(usuarioId);
        if (usuario == null)
            throw new BibliotecaException(BibliotecaException.USUARIO_NO_EXISTENTE);

        // Lanzamos excepción si el ejemplar no está disponible
        if (ejemplar.getPrestamo() != null)
            throw new BibliotecaException(BibliotecaException.EJEMPLAR_NO_DISPONIBLE);

        // Obtenemos la fecha actual del generador de fechas actuales
        // Si se está ejecutando un test, se habrá inyectado un generador
        // que devuelva una fecha fija. Si no, se devolverá la fecha actual real.
        Date fechaActual = BibliotecaBR.getInstance()
                .getGeneradorFechaActual().getFechaActual();

        // Lanzamos excepción si el usuario no está ACTIVO

        EstadoUsuario estado = usuario.getEstado(fechaActual);
        if (estado == EstadoUsuario.MOROSO)
            throw new BibliotecaException(BibliotecaException.USUARIO_MOROSO);
        else if (estado == EstadoUsuario.MULTADO)
            throw new BibliotecaException(BibliotecaException.USUARIO_MULTADO);

        // Lanzamos excepción si el cupo de operaciones
        List<Prestamo> prestamosActivos = this.prestamosActivos(usuarioId);
        BibliotecaBR.getInstance().
                compruebaCupoOperaciones(usuario, prestamosActivos.size() + 1);

        // Se cumplen todas las precondiciones y creamos el préstamo

        Date fechaDevolucion = BibliotecaBR.getInstance()
                .fechaDevolucionPrestamo(usuario, fechaActual);
        Prestamo prestamo = new Prestamo(usuario,ejemplar,fechaActual,fechaDevolucion);
        prestamo = prestamoDao.create(prestamo);
        usuario.getPrestamos().add(prestamo);
        ejemplar.setPrestamo(prestamo);
        return prestamo;
    }

    public ResultadoDevolucion devolverPrestamo(long ejemplarId) {
        ResultadoDevolucion resultadoDevolucion;

        Ejemplar ejemplar = ejemplarDao.find(ejemplarId);
        Prestamo prestamo = ejemplar.getPrestamo();
        Usuario usuario = prestamo.getUsuario();

        BibliotecaBR bibliotecaBR = BibliotecaBR.getInstance();
        Date fechaActual = bibliotecaBR.getGeneradorFechaActual().getFechaActual();

        if (bibliotecaBR.esPrestamoRetrasado(prestamo, fechaActual)) {
            eliminaPrestamo(prestamo, fechaActual);
            if (usuario.getMulta() == null) {
                creaMultaConDiasPenalizacion(prestamo, fechaActual);
            } else {
                actualizaMulta(prestamo, fechaActual);
            }
            if (usuario.getPrestamos().size() == 0) {
                cierraFechaFinalizaciónMulta(usuario.getMulta(), fechaActual);
            }
            resultadoDevolucion = ResultadoDevolucion.DEVOLUCION_FUERA_DE_PLAZO;
        } else {
            eliminaPrestamo(prestamo, fechaActual);
            resultadoDevolucion = ResultadoDevolucion.DEVOLUCION_CORRECTA;
        }
        return resultadoDevolucion;
    }

    private void eliminaPrestamo(Prestamo prestamo, Date fechaActual) {
        Usuario usuario = prestamo.getUsuario();
        Ejemplar ejemplar = prestamo.getEjemplar();
        PrestamoHistorico prestamoHistorico = new PrestamoHistorico(usuario.getId(),
                ejemplar.getId(), prestamo.getFecha(), prestamo.getDeberiaDevolverseEl(),
                fechaActual);
        prestamoHistoricoDao.create(prestamoHistorico);
        ejemplar.quitaPrestamo();
        usuario.quitaPrestamo(prestamo);
    }

    // Crea una multa sin fecha de finalización.
    // Se llama cuando el usuario tiene préstamos sin
    // devolver que pueden ampliar la multa
    private void creaMultaConDiasPenalizacion(Prestamo prestamo, Date fechaActual) {
        Usuario usuario = prestamo.getUsuario();
        BibliotecaBR bibliotecaBR = BibliotecaBR.getInstance();
        int diasPenalizacion = bibliotecaBR.diasPenalizacion(prestamo, fechaActual);
        Multa multa = new Multa(usuario, fechaActual, diasPenalizacion);
        multa = multaDao.create(multa);
        usuario.setMulta(multa);
    }

    // Actualiza el número de días de penalización, sumándole
    // la penalización del préstamo que se acaba de devolver
    private void actualizaMulta(Prestamo prestamo, Date fechaActual) {
        Usuario usuario = prestamo.getUsuario();
        Multa multa = usuario.getMulta();
        int diasAcumulados = multa.getDiasAcumulados();

        BibliotecaBR bibliotecaBR = BibliotecaBR.getInstance();
        int diasPenalizacion = bibliotecaBR.diasPenalizacion(prestamo, fechaActual);
        multa.setDiasAcumulados(diasAcumulados+diasPenalizacion);
        multa.setHasta(Utils.sumaDias(multa.getDesde(), multa.getDiasAcumulados()));
        multaDao.update(multa);
    }

    // Define la fecha de finalización de la multa, sumándole
    // los días de penalización acumulados a la fecha actual en la que se
    // devuelve el préstamo
    // Se llama cuando el usuario devuelve el último préstamo.
    private void cierraFechaFinalizaciónMulta(Multa multa, Date fechaActual) {
        multa.setHasta(Utils.sumaDias(fechaActual, multa.getDiasAcumulados()));
        multaDao.update(multa);
    }
}
