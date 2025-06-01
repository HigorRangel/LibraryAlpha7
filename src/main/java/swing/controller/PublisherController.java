package swing.controller;

import org.hibernate.exception.ConstraintViolationException;
import swing.model.Publisher;
import swing.model.dto.PublisherDTO;
import swing.service.PublisherService;
import swing.util.MessageUtils;
import swing.view.PublisherView;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador responsável por gerir as operações relacionadas aos livros.
 */
public class PublisherController extends AbstractController<PublisherView, Publisher> {


    public PublisherController(PublisherView publisherView) {
        super(publisherView, new PublisherService());
    }

    /**
     * Obtém uma lista de PublisherDTOs representando todos os livros.
     *
     * @return uma lista de PublisherDTOs.
     */
    public List<PublisherDTO> getPublishersDTO() {
        List<Publisher> publishers = service.findAll();
        return publishers.stream()
                .map(PublisherDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Abre o formulário de cadastro de editora para edição.
     *
     * @param rowValue a editora a ser editado, representado por um PublisherDTO.
     */
    public void editPublisher(PublisherDTO rowValue) {
        Publisher publisher = service.findById(rowValue.getId());
        if (publisher != null) {
            view.openForm(publisher);
        } else {
            MessageUtils.error(view, "Editora não encontrado.");
        }
    }

    /**
     * Exclui uma editora selecionado.
     *
     * @param selectedPublisher a editora a ser excluído.
     */
    public void deletePublisher(PublisherDTO selectedPublisher) {
        if (selectedPublisher == null) {
            MessageUtils.error(view, "Nenhum editora selecionado para exclusão.");
            return;
        }

        if (!MessageUtils.confirm(view, "Deseja realmente excluir a editora: " + selectedPublisher.getName() + "?")) {
            return;
        }
        Publisher publisher = service.findById(selectedPublisher.getId());
        if (publisher != null) {
            try {
                service.delete(publisher);
                view.refreshPublisherTable();
                MessageUtils.info(view, "editora excluído com sucesso.");
            } catch (ConstraintViolationException e) {
                MessageUtils.error(view, "Erro ao excluir editora. Existe um vínculo com um livro existente.");
            } catch (Exception e) {
                if (e.getMessage() != null && e.getMessage().contains("ConstraintViolationException")) {
                    MessageUtils.error(view, "Não é possível excluir a editora, pois ela está vinculada a livros.");
                    return;
                }
                MessageUtils.error(view, "Erro ao excluir editora: " + e.getMessage());
            }
        } else {
            MessageUtils.error(view, "editora não encontrado.");
        }
    }
}
