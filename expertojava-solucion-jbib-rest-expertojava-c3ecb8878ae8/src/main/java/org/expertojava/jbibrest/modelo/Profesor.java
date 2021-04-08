package org.expertojava.jbibrest.modelo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "PROFESOR")
public class Profesor extends Usuario {
    private String departamento;

    private static Log logger = LogFactory.getLog(Profesor.class);

    public Profesor() {}

    public Profesor(String login) {
        this.setLogin(login);
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String toString() {
        return this.getLogin() + " : "
                + this.getNombre() + " "
                + this.getApellido1() + " "
                + this.getApellido2() + " "
                + "(PROFESOR)";
    }

}
