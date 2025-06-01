package swing.model;

import swing.annotations.DisplayableName;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Classe que representa uma editora de livros.
 */
@Entity
@Table(name = "editora")
public class Publisher extends AbstractModel {

    @DisplayableName("Livros")
    @ManyToMany(mappedBy = "publishers", fetch = FetchType.EAGER)
    Set<Book> books = new LinkedHashSet<>();

    @DisplayableName("Nome")
    private String name;

    public Publisher() {
    }

    public Publisher(String name) {
        this.name = name;
    }


    // <editor-fold desc="Getters and Setters">
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Book> getLivros() {
        return books;
    }

    public void setLivros(Set<Book> books) {
        this.books = books;
    }

    @Override
    public String getDisplayName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return "";
    }
    // </editor-fold>
}
