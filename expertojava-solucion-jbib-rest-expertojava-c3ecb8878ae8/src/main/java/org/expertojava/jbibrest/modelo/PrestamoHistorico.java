package org.expertojava.jbibrest.modelo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.expertojava.jbibrest.utils.BibliotecaException;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
public class PrestamoHistorico extends ClaseDominio {
    private Long usuarioId;
    private Long ejemplarId;
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Temporal(TemporalType.DATE)
    private Date deberiaDevolverseEl;
    @Temporal(TemporalType.DATE)
    private Date devuelto;
    private Long multaActivaId;
    private Long multaHistoricaId;

    private static Log logger = LogFactory.getLog(PrestamoHistorico.class);

    public PrestamoHistorico() {}

    public PrestamoHistorico(Long usuarioId, Long ejemplarId, Date fecha,
                             Date deberiaDevolverseEl, Date devuelto) {
        if (fecha.after(deberiaDevolverseEl)) {
            String msg = "La fecha de inicio del préstamo no puede ser " +
                    "mayor que la de devolución";
            logger.error(msg);
            throw new BibliotecaException(msg);
        }
        this.usuarioId = usuarioId;
        this.ejemplarId = ejemplarId;
        this.fecha = fecha;
        this.deberiaDevolverseEl = deberiaDevolverseEl;
        this.devuelto = devuelto;
    }

    // Si el préstamo ha tenido multa, se guarda el id de la multa
    // actual y cuando la multa se cierra, se actualiza con el id de
    // la multa histórica

    public void setMultaActivaIdId(Long multaId) {
        this.multaActivaId = multaId;
    }

    public void setMultaHistoricaId(Long multaHistoricaId) {
        this.multaActivaId = null;
        this.multaHistoricaId = multaHistoricaId;
    }

    // Una entidad histórica no tiene setters: una vez creada no
    // pueden cambiarse sus atributos (a parte de los setters anteriores de
    // las multas)

    public Long getUsuarioId() {
        return usuarioId;
    }

    public Long getEjemplar() {
        return ejemplarId;
    }

    public Date getFecha() {
        return fecha;
    }

    public Date getDeberiaDevolverseEl() {
        return deberiaDevolverseEl;
    }

    public Date getDevuelto() {
        return devuelto;
    }

    public Long getMultaActivaId() {
        return multaActivaId;
    }

    public Long getMultaHistoricaId() {
        return multaHistoricaId;
    }
}
