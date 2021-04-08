package org.expertojava.jbibrest.modelo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class Multa extends ClaseDominio {
    @NotNull
    @OneToOne
    @JoinColumn(nullable = false)
    private Usuario usuario;
    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date desde;
    @Temporal(TemporalType.DATE)
    private Date hasta;
    private Integer diasAcumulados;

    private static Log logger = LogFactory.getLog(Multa.class);

    public Multa() {}

    public Multa(Usuario usuario, Date desde, Date hasta) {
        String msg;

        if (usuario == null) {
            msg = "Error al crear multa: usuario es null";
            logger.error(msg);
            throw new IllegalArgumentException(msg);
        }
        if (desde == null) {
            msg = "Error al crear multa: fecha desde es null";
            logger.error(msg);
            throw new IllegalArgumentException(msg);
        }
        if (hasta == null) {
            msg = "Error al crear multa: fecha hasta es null";
            logger.error(msg);
            throw new IllegalArgumentException(msg);
        }
        this.usuario = usuario;
        this.desde = desde;
        this.hasta = hasta;
    }

    public Multa(Usuario usuario, Date desde, Integer diasPenalizacion) {
        String msg;

        if (usuario == null) {
            msg = "Error al crear multa: usuario es null";
            logger.error(msg);
            throw new IllegalArgumentException(msg);
        }
        if (desde == null) {
            msg = "Error al crear multa: fecha desde es null";
            logger.error(msg);
            throw new IllegalArgumentException(msg);
        }
        if (diasPenalizacion == null) {
            msg = "Error al crear multa: diasPenalizacion es null";
            logger.error(msg);
            throw new IllegalArgumentException(msg);
        }
        this.usuario = usuario;
        this.desde = desde;
        this.diasAcumulados = diasPenalizacion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    // No definimos un setter de usuario, porque
    // no vamos a poder cambiarlo una vez creada la multa

    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public Date getHasta() {
        return hasta;
    }

    public void setHasta(Date hasta) {
        this.hasta = hasta;
    }

    public Integer getDiasAcumulados() {
        return diasAcumulados;
    }

    public void setDiasAcumulados(Integer diasAcumulados) {
        this.diasAcumulados = diasAcumulados;
    }
}
