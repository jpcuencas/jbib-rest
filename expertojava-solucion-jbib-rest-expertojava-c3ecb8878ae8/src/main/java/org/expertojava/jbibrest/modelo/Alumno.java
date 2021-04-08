package org.expertojava.jbibrest.modelo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "ALUMNO")
public class Alumno extends Usuario {
    private String telefonoPadres;

    private static Log logger = LogFactory.getLog(Alumno.class);

    public Alumno() {}

    public Alumno(String login) {
        this.setLogin(login);
    }

    public String getTelefonoPadres() {
        return telefonoPadres;
    }

    public void setTelefonoPadres(String telefonoPadres) {
        this.telefonoPadres = telefonoPadres;
    }

    public String toString() {
        return this.getLogin() + " : "
                + this.getNombre() + " "
                + this.getApellido1() + " "
                + this.getApellido2() + " "
                + "(ALUMNO)";
    }
}
