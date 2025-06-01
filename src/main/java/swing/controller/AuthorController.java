package swing.controller;

import swing.model.Author;
import swing.model.dto.AuthorDTO;
import swing.service.AuthorService;
import swing.util.MessageUtils;
import swing.view.AuthorView;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador responsável por gerir as operações relacionadas aos livros.
 */
public class AuthorController extends AbstractController<AuthorView, Author> {


    public AuthorController(AuthorView authorView) {
        super(authorView, new AuthorService());
    }

    /**
     * Obtém uma lista de AutorDTOs representando todos os livros.
     *
     * @return uma lista de AutorDTOs.
     */
    public List<AuthorDTO> getAuthorsDTO() {
        List<Author> authors = service.findAll();
        return authors.stream()
                .map(AuthorDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Abre o formulário de cadastro de autor para edição.
     *
     * @param rowValue o autor a ser editado, representado por um AutorDTO.
     */
    public void editAuthor(AuthorDTO rowValue) {
        Author author = service.findById(rowValue.getId());
        if (author != null) {
            view.openForm(author);
        } else {
            MessageUtils.error(view, "autor não encontrado.");
        }
    }

    /**
     * Exclui um autor selecionado.
     *
     * @param selectedAuthor o autor a ser excluído.
     */
    public void deleteAuthor(AuthorDTO selectedAuthor) {
        if (selectedAuthor == null) {
            MessageUtils.error(view, "Nenhum autor selecionado para exclusão.");
            return;
        }

        if (!MessageUtils.confirm(view, "Deseja realmente excluir o autor: " + selectedAuthor.getCommercialName() + "?")) {
            return;
        }
        Author author = service.findById(selectedAuthor.getId());
        if (author != null) {
            try {
                service.delete(author);
                view.refreshAuthorTable();
                MessageUtils.info(view, "Autor excluído com sucesso.");
            } catch (Exception e) {
                if (e.getMessage() != null && e.getMessage().contains("ConstraintViolationException")) {
                    MessageUtils.error(view, "Não é possível excluir o autor, pois ele está vinculado a livros.");
                } else {
                    MessageUtils.error(view, "Erro ao deletar autor: " + e.getMessage());
                }
            }
        } else {
            MessageUtils.error(view, "Autor não encontrado.");
        }
    }
}
