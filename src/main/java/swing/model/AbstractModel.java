package swing.model;


import swing.annotations.NomeExibicao;
import swing.enums.Status;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.Instant;

@MappedSuperclass
public abstract class AbstractModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NomeExibicao("ID")
    private Long id;

    @NomeExibicao("Status")
    private Status status;

    @NomeExibicao("Última Modificação")
    private Instant ultimaModificacao;

    @NomeExibicao("Criado em")
    private Instant criadoEm;

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

    public Instant getUltimaModificacao() {
        return ultimaModificacao;
    }

    public void setUltimaModificacao(Instant ultimaModificacao) {
        this.ultimaModificacao = ultimaModificacao;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(Instant criadoEm) {
        this.criadoEm = criadoEm;
    }
}
