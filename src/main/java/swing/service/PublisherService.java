package swing.service;

import swing.model.Publisher;
import swing.repository.PublisherRepository;
import swing.util.StringUtils;

import java.util.List;

/**
 * Serviço responsável por gerenciar as operações relacionadas às editoras.
 * Extende a classe AbstractService para fornecer funcionalidades básicas de CRUD.
 */
public class PublisherService extends AbstractService<Publisher> {

    final PublisherRepository publisherRepository = new PublisherRepository();

    /**
     * Busca uma editora pelo nome.
     *
     * @param nome o nome da editora a ser buscada.
     * @return a editora encontrada ou null se não houver nenhuma editora com o nome especificado.
     */
    public Publisher findByName(String nome) {
        List<Publisher> publishers = publisherRepository.findPublisherByName(nome);
        if (publishers.isEmpty()) {
            return null;
        }
        return publishers.get(0);
    }

    /**
     * Valida as informações da editora antes de inseri-la ou editá-la.
     *
     * @param isEdition indica se é uma edição de uma editora existente.
     * @param publisher a editora a ser validada.
     */
    private void validatePublisher(boolean isEdition, Publisher publisher) {
        if (publisher == null) {
            throw new IllegalArgumentException("A editora não pode ser nula.");
        }
        if (StringUtils.isNullOrEmpty(publisher.getName())) {
            throw new IllegalArgumentException("O nome da editora não pode ser vazio.");
        }
        if (!isEdition && findByName(publisher.getName()) != null) {
            throw new IllegalArgumentException("Já existe uma editora com o nome: " + publisher.getName());
        }
    }

    @Override
    public void beforeInsert(Publisher entity) {
        super.beforeInsert(entity);
        validatePublisher(false, entity);

        entity.setName(StringUtils.removeExcessiveSpaces(entity.getName()));
    }

    @Override
    public void beforeUpdate(Publisher newEntity, Publisher oldEntity) {
        super.beforeUpdate(newEntity, oldEntity);
        validatePublisher(true, newEntity);
        newEntity.setName(StringUtils.removeExcessiveSpaces(newEntity.getName()));
        newEntity.setName(StringUtils.removeAccents(newEntity.getName()));
    }
}
