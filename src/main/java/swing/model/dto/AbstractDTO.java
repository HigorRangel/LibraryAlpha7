package swing.model.dto;

import swing.annotations.DisplayableName;
import swing.enums.Status;
import swing.interfaces.Identifiable;
import swing.util.DateUtils;

import java.time.Instant;

/**
 * Classe abstrata que representa um DTO (Data Transfer Object) com campos comuns
 * para identificação, data de criação, data da última modificação e status.
 */
public abstract class AbstractDTO implements Identifiable {

    @DisplayableName("ID")
    Long id;

    @DisplayableName("Criado em")
    String createdAt;

    @DisplayableName("Última Modificação")
    String lastModifiedAt;

    @DisplayableName("Status")
    String status;

    Instant lastModifiedDateTime;

    Instant createdAtDateTime;

    protected AbstractDTO() {
    }

    protected AbstractDTO(Long id, Instant createdAt, Instant lastModifiedAt, Status status) {
        this.id = id;
        this.setCreatedAt(createdAt);
        this.setLastModifiedAt(lastModifiedAt);
        this.status = (status == null) ? null : status.name();
    }

    //<editor-fold desc="Getters and Setters">
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return createdAtDateTime;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAtDateTime = createdAt;
        if (createdAt == null) {
            this.createdAt = null;
            return;
        }
        this.createdAt = DateUtils.dateToText(createdAt, false);
    }

    public Instant getLastModifiedAt() {
        return lastModifiedDateTime;
    }

    public void setLastModifiedAt(Instant date) {
        this.lastModifiedDateTime = date;
        if (date == null) {
            this.lastModifiedAt = null;
            return;
        }
        this.lastModifiedAt = DateUtils.dateToText(date, false);
    }

    /**
     * Retorna a data da última modificação formatada como ‘String’.
     *
     * @return A data da última modificação formatada como ‘String’ no formato "dd/MM/yyyy HH:mm:ss".
     */
    public String getUltimaModificacaoString() {
        return DateUtils.dateToText(createdAtDateTime, false);
    }

    public Status getStatus() {
        return this.status == null ? null : Status.fromCharacter(this.status);
    }

    @Override
    public void setStatus(Status status) {
        if (status == null) {
            this.status = null;
        } else {
            this.status = status.name();
        }
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Retorna o status como uma ‘String’.
     *
     * @return A String representando o status, ou null se o status for nulo.
     */
    public String getStatusString() {
        return this.status;
    }

    //</editor-fold>
}
