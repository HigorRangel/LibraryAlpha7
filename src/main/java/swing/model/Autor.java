package swing.model;

import swing.annotations.NomeExibicao;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "autor")
public class Autor extends AbstractModel {

    @NomeExibicao("Nome Completo")
    private String nomeCompleto;

    @NomeExibicao("Nome Comercial")
    private String nomeComercial;

    @NomeExibicao("Gênero Principal")
    private String principalGenero;

    @NomeExibicao("Livros")
    @ManyToMany(mappedBy = "autores", fetch = FetchType.EAGER)
    Set<Livro> livros = new HashSet<>();

    public Autor() {
    }

    public Autor(String nomeCompleto, String nomeComercial, String principalGenero, Set<Livro> livros) {
        this.nomeCompleto = nomeCompleto;
        this.principalGenero = principalGenero;
        this.livros = livros;
        this.nomeComercial = nomeComercial;
    }


    // <editor-fold desc="Getters and setters">
    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getPrincipalGenero() {
        return principalGenero;
    }

    public void setPrincipalGenero(String principalGenero) {
        this.principalGenero = principalGenero;
    }

    public Set<Livro> getLivros() {
        return livros;
    }

    public void setLivros(Set<Livro> livros) {
        this.livros = livros;
    }

    public String getNomeComercial() {
        return nomeComercial;
    }

    public void setNomeComercial(String nomeComercial) {
        this.nomeComercial = nomeComercial;
    }

    // </editor-fold>
}
