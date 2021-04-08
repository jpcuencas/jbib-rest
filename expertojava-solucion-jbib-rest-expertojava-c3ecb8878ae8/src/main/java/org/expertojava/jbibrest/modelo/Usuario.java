package org.expertojava.jbibrest.modelo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.expertojava.jbibrest.utils.BibliotecaBR;
import org.expertojava.jbibrest.utils.BibliotecaException;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo", discriminatorType= DiscriminatorType.STRING)
public abstract class Usuario extends ClaseDominio {
    @NotNull
    @Column(unique=true, nullable = false)
    private String login;
    @Column(unique=true)
    private String eMail;
    private String nombre;
    private String apellido1;
    private String apellido2;
    @Embedded
    private Direccion viveEn;
    @OneToOne(mappedBy = "usuario")
    private Multa multa;
    @OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER)
    private Set<Prestamo> prestamos = new HashSet<>();

    private static Log logger = LogFactory.getLog(Usuario.class);

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public Direccion getViveEn() {
        return viveEn;
    }

    public void setViveEn(Direccion viveEn) {
        this.viveEn = viveEn;
    }

    public EstadoUsuario getEstado(Date fechaActual) {
        EstadoUsuario estado = EstadoUsuario.ACTIVO;
        if (multa != null) {
            estado = EstadoUsuario.MULTADO;
        }
        else {
            for (Prestamo prestamo : this.getPrestamos()) {
                if (BibliotecaBR.getInstance().esPrestamoRetrasado(prestamo, fechaActual)) {
                    estado = EstadoUsuario.MOROSO;
                    break;
                }
            }
        }
        return estado;
    }

    public Multa getMulta() {
        return multa;
    }

    public void setMulta(Multa multa) {
        if (multa == null) {
            throw new IllegalArgumentException("Multa no puede ser null");
        }
        this.multa = multa;
    }

    // Usamos el nombre quitaMulta por mantener la misma
    // terminología que en otras entidades y que vimos
    // teoría de JPA.
    // El servicio deberá eliminar la multa y
    // crear la multa histórica
    public void quitaMulta() {
        this.multa = null;
    }

    public Set<Prestamo> getPrestamos() {
        return prestamos;
    }

    public void añadePrestamo(Prestamo prestamo) {
        if (prestamo == null) {
            throw new IllegalArgumentException("Prestamo no puede ser null");
        }
        this.getPrestamos().add(prestamo);
    }

    // Se quita el préstamo de la lista de prestamos en memoria.
    // El servicio deberá eliminar el préstamo y crear un
    // préstamo histórico
    public void quitaPrestamo(Prestamo prestamo) {
        if (!this.getPrestamos().contains(prestamo)) {
            throw new BibliotecaException("Usuario no tiene préstamo");
        }
        this.getPrestamos().remove(prestamo);
    }
}
