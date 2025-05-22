package swing.model.dto;

import swing.annotations.NomeExibicao;
import swing.enums.Status;
import swing.model.Autor;
import swing.model.Livro;
import swing.util.DataUtils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LivroDTO extends AbstractDTO {

    @NomeExibicao("Título")
    private String titulo;

    @NomeExibicao("Autores")
    private String autores;

    @NomeExibicao("Data de Publicação")
    private String dataPublicacao;

    @NomeExibicao("ISBN")
    private String ISBN;

    @NomeExibicao("Editora")
    private String editora;

    @NomeExibicao("Livros Semelhantes")
    private String livrosSemelhantes;

    private LocalDate dataPublicacaoLocalDate;

    private List<Autor> autoresList = new ArrayList<>();


    public LivroDTO() {
    }

    public LivroDTO(Long id, Status status, Instant criadoEm, Instant ultimaModificacao,
                    String titulo, List<Autor> autores, LocalDate dataPublicacao, String ISBN, String editora, String livrosSemelhantes) {
        super(id, criadoEm, ultimaModificacao, status);
        this.titulo = titulo;
        this.setAutores(autores);
        this.dataPublicacao = DataUtils.formatarDataParaString(dataPublicacao, false);
        this.dataPublicacaoLocalDate = dataPublicacao;
        this.ISBN = ISBN;
        this.editora = editora;
        this.livrosSemelhantes = livrosSemelhantes;
        this.id = id;
        this.status = (status == null) ? Status.ACTIVE.name() : status.name();
        this.criadoEm = DataUtils.formatarDataParaString(criadoEm, false);
        this.ultimaModificacao = DataUtils.formatarDataParaString(ultimaModificacao, false);
    }


    public LivroDTO(Livro livro) {
        super(livro.getId(), livro.getCriadoEm(), livro.getUltimaModificacao(), livro.getStatus());
        this.titulo = livro.getTitulo();
        this.setAutores(livro.getAutores());
        this.dataPublicacao = DataUtils.formatarDataParaString(livro.getDataPublicacao(), false);
        this.dataPublicacaoLocalDate = livro.getDataPublicacao();
        this.ISBN = livro.getISBN();
        this.editora = livro.getEditora();
        this.livrosSemelhantes = livro.getLivrosSemelhantes();
        this.id = livro.getId();
        this.status = (livro.getStatus() == null) ? Status.ACTIVE.name() : livro.getStatus().name();
        this.criadoEm = DataUtils.formatarDataParaString(livro.getCriadoEm(), false);
        this.ultimaModificacao = DataUtils.formatarDataParaString(livro.getUltimaModificacao(), false);
    }

    // <editor-fold desc="Getters and setters">
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores.stream()
                .map(Autor::getNomeComercial)
                .collect(Collectors.joining(", "));
        this.autoresList = autores;
    }

    public String getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(LocalDate dataPublicacao) {
        this.dataPublicacaoLocalDate = dataPublicacao;
        if (dataPublicacao == null) {
            this.dataPublicacao = null;
            return;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        this.dataPublicacao = formatter.format(dataPublicacao);
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
