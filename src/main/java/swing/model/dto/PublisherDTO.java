package swing.model.dto;

import com.sun.istack.NotNull;
import swing.annotations.DisplayableName;
import swing.model.Publisher;

/**
 * Data Transfer Object (DTO) para a entidade Editora.
 */
public class PublisherDTO extends AbstractDTO {

    @DisplayableName("Nome")
    @NotNull
    private String name;

    public PublisherDTO() {
    }

    public PublisherDTO(Publisher publisher) {
        this.setId(publisher.getId());
        this.setCreatedAt(publisher.getCreatedAt());
        this.setLastModifiedAt(publisher.getLastModifiedAt());
        this.setStatus(publisher.getStatus());
        this.name = publisher.getName();
    }

    // <editor-fold desc="Getters and setters">

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDisplayName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.name;
    }
    // </editor-fold>
}
