package swing.service;

import swing.model.Author;
import swing.repository.AuthorRepository;
import swing.util.StringUtils;

import java.util.List;

/**
 * Serviço responsável por gerenciar as operações relacionadas aos autores.
 * Extende a classe AbstractService para fornecer funcionalidades básicas de CRUD.
 */
public class AuthorService extends AbstractService<Author> {

    final AuthorRepository authorRepository = new AuthorRepository();

    /**
     * Busca um autor pelo nome.
     *
     * @param nome o nome do autor a ser buscado.
     * @return o autor encontrado ou null se não houver nenhum autor com o nome especificado.
     */
    public Author findByName(String nome) {
        List<Author> autores = authorRepository.findAuthorByName(nome);
        if (autores.isEmpty()) {
            return null;
        }
        return autores.get(0);
    }

    /**
     * Valida as informações do autor antes de inseri-lo ou editá-lo.
     *
     * @param isEdition indica se é uma edição de um autor existente.
     * @param author    o autor a ser validado.
     */
    private void validateAuthor(boolean isEdition, Author author) {
        if (author == null) {
            throw new IllegalArgumentException("O autor não pode ser nulo.");
        }
        if (StringUtils.isNullOrEmpty(author.getFullName()) && StringUtils.isNullOrEmpty(author.getCommercialName())) {
            throw new IllegalArgumentException("O nome do autor não pode ser vazio.");
        }
        if (!isEdition && (findByName(author.getCommercialName()) != null || findByName(author.getFullName()) != null)) {
            throw new IllegalArgumentException("Já existe um autor com o nome: " + author.getCommercialName());
        }
    }


    @Override
    public void beforeInsert(Author entity) {
        super.beforeInsert(entity);
        validateAuthor(false, entity);
        if (StringUtils.isNullOrEmpty(entity.getCommercialName())) {
            entity.setCommercialName(entity.getFullName());
        } else {
            entity.setCommercialName(StringUtils.removeExcessiveSpaces(entity.getCommercialName()));
        }
        entity.setFullName(StringUtils.removeExcessiveSpaces(entity.getFullName()));
        entity.setCommercialName(StringUtils.removeAccents(entity.getCommercialName()));
    }

    @Override
    public void beforeUpdate(Author newEntity, Author oldEntity) {
        super.beforeUpdate(newEntity, oldEntity);
        validateAuthor(true, newEntity);
        if (StringUtils.isNullOrEmpty(newEntity.getCommercialName())) {
            newEntity.setCommercialName(newEntity.getFullName());
        } else {
            newEntity.setCommercialName(StringUtils.removeExcessiveSpaces(newEntity.getCommercialName()));
        }
        newEntity.setFullName(StringUtils.removeExcessiveSpaces(newEntity.getFullName()));
        newEntity.setCommercialName(StringUtils.removeAccents(newEntity.getCommercialName()));
    }
}
