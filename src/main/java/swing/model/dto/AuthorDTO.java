package swing.model.dto;

import com.sun.istack.NotNull;
import swing.annotations.DisplayableName;
import swing.model.Author;

/**
 * Data Transfer Object (DTO) para a entidade Autor.
 */
public class AuthorDTO extends AbstractDTO {

    @DisplayableName("Nome Comercial")
    @NotNull
    private String commercialName;

    @DisplayableName("Nome Completo")
    @NotNull
    private String fullName;

    @DisplayableName("Gênero Principal")
    private String mainGenre;

    public AuthorDTO() {
    }

    public AuthorDTO(Author author) {
        this.setId(author.getId());
        this.setCreatedAt(author.getCreatedAt());
        this.setLastModifiedAt(author.getLastModifiedAt());
        this.setStatus(author.getStatus());
        this.commercialName = author.getCommercialName();
        this.fullName = author.getFullName();
        this.mainGenre = author.getMainGenre();
    }

    // <editor-fold desc="Getters and setters">
    public String getCommercialName() {
        return commercialName;
    }

    public void setCommercialName(String commercialName) {
        this.commercialName = commercialName;
    }

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

    @Override
    public String getDisplayName() {
        return this.getCommercialName() != null ? this.getCommercialName() : this.getFullName();
    }

    @Override
    public String getDescription() {
        return this.getFullName() != null ? this.getFullName() : this.getCommercialName();
    }
    // </editor-fold>
}
