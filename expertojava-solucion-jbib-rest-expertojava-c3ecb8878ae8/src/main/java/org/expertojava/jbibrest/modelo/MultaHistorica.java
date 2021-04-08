package org.expertojava.jbibrest.modelo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
public class MultaHistorica extends ClaseDominio {
    private Long usuarioId;
    @Temporal(TemporalType.DATE)
    private Date desde;
    @Temporal(TemporalType.DATE)
    private Date hasta;

    private static Log logger = LogFactory.getLog(MultaHistorica.class);

    public MultaHistorica() {}

    public MultaHistorica(Long usuarioId, Date desde, Date hasta) {
        String msg;

        if (desde == null) {
            msg = "Error al crear multa: fecha desde es null";
            logger.error(msg);
            throw new IllegalArgumentException(msg);
        }
        if (hasta == null) {
            msg = "Error al crear multa: usuario es null";
            logger.error(msg);
            throw new IllegalArgumentException(msg);
        }
        this.usuarioId = usuarioId;
        this.desde = desde;
        this.hasta = hasta;
    }

    // Una entidad histórica no tiene setters: una vez creada no
    // pueden cambiarse sus atributos

    public Long getUsuarioId() {
        return usuarioId;
    }

    public Date getDesde() {
        return desde;
    }

    public Date getHasta() {
        return hasta;
    }
}