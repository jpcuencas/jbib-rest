package org.expertojava.jbibrest.modelo;

import javax.persistence.Embeddable;

@Embeddable
public class Direccion {
    private String calle;
    private Integer numero;
    private String piso;
    private String ciudad;
    private String codigoPostal;

    public  Direccion() {}

    public Direccion(String calle) {
        this.calle = calle;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getPiso() {
        return piso;
    }

    public void setPiso(String piso) {
        this.piso = piso;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }
}
