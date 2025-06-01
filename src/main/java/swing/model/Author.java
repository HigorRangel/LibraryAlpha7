package swing.model;

import swing.annotations.DisplayableName;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Representa um autor de livros.
 */
@Entity
@Table(name = "autor")
public class Author extends AbstractModel {

    @DisplayableName("Livros")
    @ManyToMany(mappedBy = "authors", fetch = FetchType.EAGER)
    Set<Book> books = new HashSet<>();

    @DisplayableName("Nome Completo")
    private String fullName;

    @DisplayableName("Nome Comercial")
    private String commercialName;

    @DisplayableName("Gênero Principal")
    private String mainGenre;

    public Author() {
    }

    public Author(String fullName, String commercialName, String mainGenre, Set<Book> books) {
        this.fullName = fullName;
        this.mainGenre = mainGenre;
        this.books = books;
        this.commercialName = commercialName;
    }


    // <editor-fold desc="Getters and setters">
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMainGenre() {
        return mainGenre;
    }

    public void setMainGenre(String mainGenre) {
        this.mainGenre = mainGenre;
    }

    public Set<Book> getLivros() {
        return books;
    }

    public void setLivros(Set<Book> books) {
        this.books = books;
    }

    public String getCommercialName() {
        return commercialName;
    }

    public void setCommercialName(String commercialName) {
        this.commercialName = commercialName;
    }

    @Override
    public String getDisplayName() {
        return this.fullName;
    }

    @Override
    public String getDescription() {
        return this.commercialName;
    }

    // </editor-fold>


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(getId(), author.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }


}
