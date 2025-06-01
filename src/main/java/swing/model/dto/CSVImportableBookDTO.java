package swing.model.dto;

import swing.annotations.CsvProperties;
import swing.converters.ConverterListAuthors;
import swing.converters.ConverterListPublishers;
import swing.converters.ConverterLocalDate;
import swing.enums.Status;
import swing.interfaces.CSVImportable;
import swing.model.Author;
import swing.model.Publisher;

import java.time.Instant;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Data Transfer Object (DTO) para importar livros via CSV.
 */
public class CSVImportableBookDTO implements CSVImportable {

    Set<String> importErrors = new LinkedHashSet<>();

    private Long id;

    @CsvProperties(required = true)
    private String title;

    @CsvProperties(required = true, csvConverter = ConverterListAuthors.class)
    private Set<Author> authors;

    @CsvProperties(csvConverter = ConverterListPublishers.class)
    private Set<Publisher> publishers;

    @CsvProperties(required = true)
    private String isbn;

    @CsvProperties(csvConverter = ConverterLocalDate.class)
    private LocalDate publicationDate;

    private Instant lastModifiedAt;

    private Instant createdAt;

    private Status status;

    public CSVImportableBookDTO() {
    }

    public CSVImportableBookDTO(Long id, String title, Set<Author> authors, Set<Publisher> publishers, String isbn, LocalDate publicationDate,
                                Instant lastModifiedAt, Instant createdAt, Status status) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.publishers = publishers;
        this.isbn = isbn;
        this.publicationDate = publicationDate;
        this.lastModifiedAt = lastModifiedAt;
        this.createdAt = createdAt;
        this.status = status;
    }

    // <editor-fold desc="Getters and setters">
    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Status getStatus() {
        return this.status;
    }

    @Override
    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public Instant getLastModifiedAt() {
        return this.lastModifiedAt;
    }

    @Override
    public void setLastModifiedAt(Instant ultimaModificacao) {
        this.lastModifiedAt = ultimaModificacao;
    }

    @Override
    public Instant getCreatedAt() {
        return this.createdAt;
    }

    @Override
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String getDisplayName() {
        return this.title;
    }

    @Override
    public String getDescription() {
        return this.isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public Set<Publisher> getPublishers() {
        return publishers;
    }

    public void setEditoras(Set<Publisher> publishers) {
        this.publishers = publishers;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    @Override
    public Set<String> getImportErrors() {
        return this.importErrors;
    }

    @Override
    public void addErroImportacao(String erro) {
        this.importErrors.add(erro);
    }
    // </editor-fold>
}
