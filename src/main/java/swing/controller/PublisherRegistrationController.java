package swing.controller;

import org.hibernate.exception.ConstraintViolationException;
import swing.model.Publisher;
import swing.service.PublisherService;
import swing.util.MessageUtils;
import swing.util.StringUtils;
import swing.view.PublisherRegistrationView;

public class PublisherRegistrationController extends AbstractController<PublisherRegistrationView, Publisher> {

    public PublisherRegistrationController(PublisherRegistrationView view) {
        super(view, new PublisherService());
    }

    /**
     * Método para abrir o formulário de registro de editora.
     *
     * @param publisher editora a ser editado ou null para novo registro.
     */
    public boolean deleteRecord(Publisher publisher) {
        if (publisher == null) {
            throw new IllegalArgumentException("A editora não foi encontrado.");
        }
        if (publisher.getId() == null) {
            throw new IllegalArgumentException("A editora deve ter um ID válido para ser deletado.");
        }
        try {
            service.delete(publisher);
            MessageUtils.info(view, "Editora deletado com sucesso.");
            return true;
        } catch (ConstraintViolationException e) {
            MessageUtils.error(view, "Não é possível deletar a editora, pois ela está vinculada a livros.");
            return false;
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("ConstraintViolationException")) {
                MessageUtils.error(view, "Não é possível excluir a editora, pois ela está vinculada a livros.");
                return false;
            }
            MessageUtils.error(view, "Erro ao deletar editora: " + e.getMessage());
            return false;
        }
    }


    /**
     * Método para salvar um editora.
     *
     * @param id   ‘ID’ da editora, pode ser null para novo registro.
     * @param name Nome completo da editora, pode ser null (se commercialName não for null).
     * @return true se a editora foi salvo com sucesso, false caso contrário.
     */
    public boolean savePublisher(String id, String name) {
        boolean isEdit = id != null && !id.isEmpty();
        Publisher publisher = new Publisher();

        if (isEdit) {
            publisher = service.findById(Long.parseLong(id));
            if (publisher == null) {
                MessageUtils.error(view, "Editora não encontrada.");
                return false;
            }
        }

        if (validateFieldsValues(id, name, publisher)) return false;

        publisher.setName(StringUtils.removeExcessiveSpaces(name));

        try {
            if (isEdit) {
                service.update(publisher);
            } else {
                service.insert(publisher);
            }
            MessageUtils.info(view, "Editora salva com sucesso!");
        } catch (Exception e) {
            MessageUtils.error(view, "Erro ao salvar a editora: " + e.getMessage());
            return false;
        }
        return true;
    }


    /**
     * Valida os campos do formulário de registro de editora.
     *
     * @param id        ‘ID’ da editora, pode ser null para novo registro.
     * @param name      Nome comercial da editora, pode ser null (se fullName não for null).
     * @param publisher editora a ser validado.
     * @return true se houver algum erro de validação, false caso contrário.
     */
    private boolean validateFieldsValues(String id, String name, Publisher publisher) {
        if (id != null && !id.isEmpty()) {
            publisher.setId(Long.parseLong(id));
        }

        if (StringUtils.isNullOrEmpty(name)) {
            MessageUtils.error(view, "Nome da Editora é obrigatório.");
            return true;
        }
        return false;
    }
}
