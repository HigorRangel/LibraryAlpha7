package swing.model.dto;

import swing.annotations.NomeExibicao;
import swing.enums.Status;
import swing.util.DataUtils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.Instant;

public class AbstractDTO  {

    @NomeExibicao("ID")
    Long id;

    @NomeExibicao("Criado em")
    String criadoEm;

    @NomeExibicao("Última Modificação")
    String ultimaModificacao;

    @NomeExibicao("Status")
    String status;

    Instant ultimaModificacaoDateTime;

    Instant criadoEmDateTime;
    
    public AbstractDTO() {
    }
    
    public AbstractDTO(Long id, Instant criadoEm, Instant ultimaModificacao, Status status) {
        this.id = id;
        this.setCriadoEm(criadoEm);
        this.setUltimaModificacao(ultimaModificacao);
        this.status = (status == null) ? null : status.name();
    }

    //<editor-fold desc="Getters and Setters">
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCriadoEm() {
        return criadoEm;
    }

    public Instant getCriadoEmDateTime() {
        return criadoEmDateTime;
    }

    public void setCriadoEm(Instant criadoEmDateTime) {
        this.criadoEmDateTime = criadoEmDateTime;
        if(criadoEmDateTime == null) {
            this.criadoEm = null;
            return;
        }
        this.criadoEm = DataUtils.formatarDataParaString(criadoEmDateTime, false);
    }


    public String getUltimaModificacao() {
        return ultimaModificacao;
    }

    public Instant getUltimaModificacaoDateTime() {
        return ultimaModificacaoDateTime;
    }

    public void setUltimaModificacao(Instant date) {
        this.ultimaModificacaoDateTime = date;
        if(date == null) {
            this.ultimaModificacao = null;
            return;
        }
        this.ultimaModificacao = DataUtils.formatarDataParaString(date, false);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    //</editor-fold>
}
