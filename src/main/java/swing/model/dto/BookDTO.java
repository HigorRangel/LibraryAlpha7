package swing.model.dto;

import com.sun.istack.NotNull;
import swing.annotations.DisplayableName;
import swing.annotations.NotFilterable;
import swing.enums.Status;
import swing.model.Author;
import swing.model.Book;
import swing.model.Publisher;
import swing.util.DateUtils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Data Transfer Object (DTO) para a entidade Livro.
 */
public class BookDTO extends AbstractDTO {

    @NotNull
    @DisplayableName("Título")
    private String title;

    @NotNull
    @DisplayableName("Autores")
    private String authors;

    @DisplayableName("Data de Publicação")
    private String publicationDate;

    @NotNull
    @DisplayableName("ISBN")
    private String isbn;

    @NotNull
    @DisplayableName("Editoras")
    private String publishers;

    @NotFilterable
    @DisplayableName("Livros Semelhantes")
    private String similarBooks;

    public BookDTO() {
    }

    public BookDTO(Long id, Status status, Instant createdAt, Instant lastModifiedAt,
                   String title, Set<Author> authors, LocalDate publicationDate, String isbn, Set<Publisher> publishers, Set<Book> similarBooks) {
        super(id, createdAt, lastModifiedAt, status);
        this.title = title;
        this.setAuthors(authors);
        this.publicationDate = DateUtils.dateToText(publicationDate, false);
        this.isbn = isbn;
        this.setPublishers(publishers);
        this.setSimilarBooks(similarBooks);
        this.id = id;
        this.status = (status == null) ? Status.ACTIVE.name() : status.name();
        this.createdAt = DateUtils.dateToText(createdAt, false);
        this.lastModifiedAt = DateUtils.dateToText(lastModifiedAt, false);
    }


    public BookDTO(Book book) {
        super(book.getId(), book.getCreatedAt(), book.getLastModifiedAt(), book.getStatus());
        this.title = book.getTitle();
        this.setAuthors(book.getAuthors());
        this.publicationDate = DateUtils.dateToText(book.getPublicationDate(), false);
        this.isbn = book.getIsbn();
        this.setPublishers(book.getPublishers());
        this.setSimilarBooks(book.getSimilarBooks());
        this.id = book.getId();
        this.status = (book.getStatus() == null) ? Status.ACTIVE.name() : book.getStatus().name();
        this.createdAt = DateUtils.dateToText(book.getCreatedAt(), false);
        this.lastModifiedAt = DateUtils.dateToText(book.getLastModifiedAt(), false);
    }

    // <editor-fold desc="Getters and setters">
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors.stream()
                .map(Author::getCommercialName)
                .collect(Collectors.joining(", "));
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        if (publicationDate == null) {
            this.publicationDate = null;
            return;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        this.publicationDate = formatter.format(publicationDate);
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String Isbn) {
        this.isbn = Isbn;
    }

    public String getPublishers() {
        return publishers;
    }

    public void setPublishers(Set<Publisher> publishers) {
        this.publishers = publishers.stream()
                .map(Publisher::getName)
                .collect(Collectors.joining(", "));
    }

    public String getSimilarBooks() {
        return similarBooks;
    }

    public void setSimilarBooks(Set<Book> similarBooks) {
        this.similarBooks = similarBooks.stream()
                .map(Book::getTitle)
                .collect(Collectors.joining(", "));
    }

    @Override
    public String getDisplayName() {
        return this.title;
    }

    @Override
    public String getDescription() {
        return this.isbn;
    }
    // </editor-fold>
}
