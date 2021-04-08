package org.expertojava.jbibrest.utils;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.expertojava.jbibrest.modelo.*;

import java.util.Date;

/**
 * Reglas de Negocio de la Biblioteca BR = Business Rules
 * <p>
 * Lo implementamos como un singleton por si algun dia queremos leer las
 * constantes desde un fichero de configuración, lo podemos hacer desde el
 * constructor del singleton
 */
public class BibliotecaBR {
    private int numDiasPrestamoAlumno = 7;
    private int numDiasPrestamoProfesor = 30;
    private int cupoOperacionesAlumno = 5;
    private int cupoOperacionesProfesor = 8;

    // Generador de fecha actual
    GeneradorFechaActual generadorFechaActual = new GeneradorFechaActualReal();

    private static Log logger = LogFactory.getLog(BibliotecaBR.class);

    private static BibliotecaBR me = new BibliotecaBR();

    private BibliotecaBR() {
        logger.debug("Creada instancia de " + this.getClass().getSimpleName());
    }

    public static BibliotecaBR getInstance() {
        return me;
    }

    public void setGeneradorFechaActual(GeneradorFechaActual generadorFechaActual) {
        this.generadorFechaActual = generadorFechaActual;
    }

    public GeneradorFechaActual getGeneradorFechaActual() {
        return this.generadorFechaActual;
    }

    /**
     * Devuelve el numero de dias de plazo que tienen un usuario para
     * devolver un prestamo (Alumno = 7 , Profesor = 30)
     *
     * @param usuario objeto Usuario
     * @return numero de dias del prestamo en función de la clase de
     * Usuario, Alumno o Profesor
     * @throws BibliotecaException el usuario no es de la clase Alumno ni Profesor
     */
    public int calculaNumDiasPrestamo(Usuario usuario)
            throws BibliotecaException {
        if (usuario instanceof Alumno) {
            return numDiasPrestamoAlumno;
        } else if (usuario instanceof Profesor) {
            return numDiasPrestamoProfesor;
        } else {
            String msg = "Solo los alumnos y profesores pueden tener " +
                    "libros prestados";
            logger.error(msg);
            throw new BibliotecaException(msg);
        }
    }

    /**
     * Valida que el número de días que se ha tardado en devolver
     * un libro es inferior o igual que el plazo máximo
     *
     * @param usuario objeto Usuario
     * @param numDias número de días sin devolver
     * @throws BibliotecaException fuera de plazo
     * @throws BibliotecaException el tipo del usuario no es el esperado
     */
    public void compruebaNumDiasPrestamo(Usuario usuario, int numDias)
            throws BibliotecaException {
        String msg;
        if (!(usuario instanceof Alumno)
                && !(usuario instanceof Profesor)) {
            msg = "Solo los alumnos y profesores pueden tener libros " +
                    "prestados";
            logger.error(msg);
            throw new BibliotecaException(msg);
        }
        if ((usuario instanceof Alumno && numDias >
                numDiasPrestamoAlumno) ||
                (usuario instanceof Profesor && numDias >
                        numDiasPrestamoProfesor)) {
            msg = "Devolución fuera de plazo";
            logger.error(msg);
            throw new BibliotecaException(msg);
        }
    }

    /**
     * Devuelve el número máximo de préstamos que
     * puede realizar un determinado tipo de usuario
     *
     * @param usuario objeto UsuarioDomain
     * @return número máximo de operaciones del tipo de usuario
     * @throws BibliotecaException el tipo del usuario no es el esperado
     */
    public int cupoOperaciones(Usuario usuario)
            throws BibliotecaException {
        if (usuario instanceof Alumno)
            return cupoOperacionesAlumno;
        else if (usuario instanceof Profesor)
            return cupoOperacionesProfesor;
        else {
            String msg = "Solo los alumnos y profesores pueden tener libros prestados";
            logger.error(msg);
            throw new BibliotecaException(msg);
        }
    }

    /**
     * Valida que el número de prestamos realizadas por un determinado
     * tipo de usuario se inferior o igual al cupo definido
     *
     * @param usuario objeto Usuario
     * @param numOp   número de operación que ya tiene realizadas
     * @throws BibliotecaException el cupo de operacion esta lleno
     * @throws BibliotecaException el tipo del usuario no es el esperado
     */
    public void compruebaCupoOperaciones(Usuario usuario, int numOp)
            throws BibliotecaException {
        String msg;
        if (!(usuario instanceof Alumno)
                && !(usuario instanceof Profesor)) {
            msg = "Solo los alumnos y profesores pueden tener libros prestados";
            logger.error(msg);
            throw new BibliotecaException(msg);
        }
        if ((usuario instanceof Alumno && numOp >
                cupoOperacionesAlumno) ||
                (usuario instanceof Profesor && numOp > cupoOperacionesProfesor)) {
            msg = "El cupo de operaciones posibles esta lleno";
            logger.error(msg);
            throw new BibliotecaException(msg);
        }
    }

    /**
     * Devuelve una fecha de devolución de un préstamo,
     * sumando a la fecha del parámetro el número de días de préstamo
     * del tipo de usuario
     */
    public Date fechaDevolucionPrestamo(Usuario usuario, Date fechaActual) {
        return Utils.sumaDias(fechaActual, this.calculaNumDiasPrestamo(usuario));
    }

    /**
     * Devuelve los días de penalización de un préstamo retrasado
     */
    public int diasPenalizacion(Prestamo prestamo, Date fechaActual) {
        return Utils.diferenciaDias(prestamo.getDeberiaDevolverseEl(), fechaActual);
    }

    public boolean esPrestamoRetrasado(Prestamo prestamo, Date fechaActual) {
        return prestamo.getDeberiaDevolverseEl().before(fechaActual);
    }
}