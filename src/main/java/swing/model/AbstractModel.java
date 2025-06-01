package swing.model;


import swing.annotations.DisplayableName;
import swing.enums.Status;
import swing.interfaces.Identifiable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.Instant;

/**
 * Classe abstrata que representa um modelo base para todas as entidades do sistema.
 */
@MappedSuperclass
public abstract class AbstractModel implements Identifiable, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @DisplayableName("ID")
    private Long id;

    @DisplayableName("Status")
    private Status status;

    @DisplayableName("Última Modificação")
    private Instant lastModifiedAt;

    @DisplayableName("Criado em")
    private Instant createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Instant getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(Instant ultimaModificacao) {
        this.lastModifiedAt = ultimaModificacao;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
