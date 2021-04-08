package org.expertojava.jbibrest.rest.dto;


import org.expertojava.jbibrest.modelo.Usuario;
import org.expertojava.jbibrest.utils.BibliotecaBR;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class UsuarioDetalle {
    public Long id;
    public String login;
    public String eMail;
    public String nombre;
    public String apellido1;
    public String apellido2;
    @XmlElement(name="multa")
    public MultaItem multaItem;
    @XmlElement(name="num_prestamos")
    public int numPrestamos;
    @XmlElement(name="tipo_usuario")
    public String tipo;
    public String estado;

    public UsuarioDetalle() {
    }

    public UsuarioDetalle(Usuario usuario) {

        MultaItem multaItem = null;

        if (usuario.getMulta() != null) {
            multaItem = new MultaItem();
            multaItem.hasta = usuario.getMulta().getHasta();
        }

        this.id = usuario.getId();
        this.login = usuario.getLogin();
        this.nombre = usuario.getNombre();
        this.apellido1 = usuario.getApellido1();
        this.apellido2 = usuario.getApellido2();
        this.eMail = usuario.geteMail();
        this.multaItem = multaItem;
        this.numPrestamos = usuario.getPrestamos().size();
        this.tipo = usuario.getClass().getSimpleName();
        this.estado = usuario.getEstado(BibliotecaBR.getInstance()
                                            .getGeneradorFechaActual()
                                            .getFechaActual()).toString();
    }
}
