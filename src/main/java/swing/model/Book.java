package swing.model;

import swing.annotations.DisplayableName;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Representa um livro no sistema.
 */
@Entity
@Table(name = "livro")
public class Book extends AbstractModel {

    @DisplayableName("Título")
    private String title;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "livro_autor",
            joinColumns = @JoinColumn(name = "livro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id"))
    @DisplayableName("Autores")
    private Set<Author> authors = new LinkedHashSet<>();

    @DisplayableName("Data de Publicação")
    private LocalDate publicationDate;

    @DisplayableName("ISBN")
    private String isbn;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "livro_editora",
            joinColumns = @JoinColumn(name = "livro_id"),
            inverseJoinColumns = @JoinColumn(name = "editora_id"))

    @DisplayableName("Editoras")
    private Set<Publisher> publishers = new LinkedHashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "livro_semelhante",
            joinColumns = @JoinColumn(name = "livro_id"),
            inverseJoinColumns = @JoinColumn(name = "semelhante_id")
    )
    private Set<Book> similarBooks = new LinkedHashSet<>();

    public Book() {
    }

    public Book(String title, Set<Author> authors, LocalDate publicationDate, String isbn, Set<Publisher> publishers, Set<Book> similarBooks) {
        this.title = title;
        this.authors = authors;
        this.publicationDate = publicationDate;
        this.isbn = isbn;
        this.publishers = publishers;
        this.similarBooks = similarBooks;
    }

    // <editor-fold desc="Getters and setters">
    public String getTitle() {
        return title;
    }

    public void setTitle(String titulo) {
        this.title = titulo;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> autores) {
        this.authors = autores;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate dataPublicacao) {
        this.publicationDate = dataPublicacao;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Set<Publisher> getPublishers() {
        return publishers;
    }

    public void setPublishers(Set<Publisher> publishers) {
        this.publishers = publishers;
    }

    public Set<Book> getSimilarBooks() {
        return similarBooks;
    }

    public void setSimilarBooks(Set<Book> similarBooks) {
        this.similarBooks = similarBooks;
    }

    @Override
    public String getDisplayName() {
        return this.title;
    }

    @Override
    public String getDescription() {
        return isbn;
    }
    // </editor-fold>


    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", authors=" + authors +
                ", publicationDate=" + publicationDate +
                ", isbn='" + isbn + '\'' +
                ", publishers=" + publishers +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Book book = (Book) object;
        return Objects.equals(title, book.title) && Objects.equals(authors, book.authors) && Objects.equals(publicationDate, book.publicationDate) && Objects.equals(isbn, book.isbn) && Objects.equals(publishers, book.publishers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, authors, publicationDate, isbn, publishers);
    }
}
