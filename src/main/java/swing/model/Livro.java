package swing.model;

import swing.annotations.NomeExibicao;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "livro")
public class Livro extends AbstractModel {

    @NomeExibicao("Título")
    private String titulo;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "livro_autor",
            joinColumns = @javax.persistence.JoinColumn(name = "livro_id"),
            inverseJoinColumns = @javax.persistence.JoinColumn(name = "autor_id"))
    @NomeExibicao("Autores")
    private List<Autor> autores = new ArrayList<>();

    @NomeExibicao("Data de Publicação")
    private LocalDate dataPublicacao;

    @NomeExibicao("ISBN")
    private String ISBN;

    @NomeExibicao("Editora")
    private String editora;

    //TODO Mudar para lista
    @NomeExibicao("Livros Semelhantes")
    private String livrosSemelhantes;

    public Livro() {
    }

    public Livro(String titulo, List<Autor> autores, LocalDate dataPublicacao, String ISBN, String editora, String livrosSemelhantes) {
        this.titulo = titulo;
        this.autores = autores;
        this.dataPublicacao = dataPublicacao;
        this.ISBN = ISBN;
        this.editora = editora;
        this.livrosSemelhantes = livrosSemelhantes;
    }

    // <editor-fold desc="Getters and setters">
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }

    public LocalDate getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(LocalDate dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public String getLivrosSemelhantes() {
        return livrosSemelhantes;
    }

    public void setLivrosSemelhantes(String livrosSemelhantes) {
        this.livrosSemelhantes = livrosSemelhantes;
    }
    // </editor-fold>


}
