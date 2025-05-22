package swing.model;

import swing.annotations.NomeExibicao;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "usuario")
public class Usuario extends AbstractModel{

    @NomeExibicao("Nome")
    private String nome;

    @NomeExibicao("Email")
    private String email;

    public Usuario() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
