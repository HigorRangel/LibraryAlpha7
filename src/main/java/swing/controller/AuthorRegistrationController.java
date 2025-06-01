package swing.controller;

import org.hibernate.exception.ConstraintViolationException;
import swing.model.Author;
import swing.service.AuthorService;
import swing.util.MessageUtils;
import swing.util.StringUtils;
import swing.view.AuthorRegistrationView;

public class AuthorRegistrationController extends AbstractController<AuthorRegistrationView, Author> {

    public AuthorRegistrationController(AuthorRegistrationView view) {
        super(view, new AuthorService());
    }


    /**
     * Método para abrir o formulário de registro de autor.
     *
     * @param author Autor a ser editado ou null para novo registro.
     */
    public boolean deleteRecord(Author author) {
        if (author == null) {
            throw new IllegalArgumentException("O autor não foi encontrado.");
        }
        if (author.getId() == null) {
            throw new IllegalArgumentException("O autor deve ter um ID válido para ser deletado.");
        }
        try {
            service.delete(author);
            MessageUtils.info(view, "Autor deletado com sucesso.");
            return true;
        } catch (ConstraintViolationException e) {
            MessageUtils.error(view, "Não é possível deletar o autor, pois ele está vinculado a livros.");
            return false;
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("ConstraintViolationException")) {
                MessageUtils.error(view, "Não é possível excluir o autor, pois ele está vinculado a livros.");
                return false;
            }
            MessageUtils.error(view, "Erro ao deletar autor: " + e.getMessage());
            return false;
        }
    }


    /**
     * Método para salvar um autor.
     *
     * @param id             ‘ID’ do autor, pode ser null para novo registro.
     * @param commercialName Nome comercial do autor, pode ser null (se fullName não for null).
     * @param fullName       Nome completo do autor, pode ser null (se commercialName não for null).
     * @param mainGenre      Gênero principal do autor, pode ser null.
     * @return true se o autor foi salvo com sucesso, false caso contrário.
     */
    public boolean saveAuthor(String id, String commercialName, String fullName, String mainGenre) {
        boolean isEdit = id != null && !id.isEmpty();
        Author author = new Author();

        if (isEdit) {
            author = service.findById(Long.parseLong(id));
            if (author == null) {
                MessageUtils.error(view, "Autor não encontrado.");
                return false;
            }
        }

        if (validateFieldsValues(id, commercialName, fullName, author)) return false;

        author.setCommercialName(StringUtils.removeExcessiveSpaces(commercialName));
        author.setFullName(StringUtils.removeExcessiveSpaces(fullName));
        author.setMainGenre(mainGenre);

        try {
            if (isEdit) {
                service.update(author);
            } else {
                service.insert(author);
            }
            MessageUtils.info(view, "Autor salvo com sucesso!");
        } catch (Exception e) {
            MessageUtils.error(view, "Erro ao salvar o autor: " + e.getMessage());
            return false;
        }
        return true;
    }


    /**
     * Valida os campos do formulário de registro de autor.
     *
     * @param id             ‘ID’ do autor, pode ser null para novo registro.
     * @param commercialName Nome comercial do autor, pode ser null (se fullName não for null).
     * @param fullName       Nome completo do autor, pode ser null (se commercialName não for null).
     * @param author         Autor a ser validado.
     * @return true se houver algum erro de validação, false caso contrário.
     */
    private boolean validateFieldsValues(String id, String commercialName, String fullName, Author author) {
        if (id != null && !id.isEmpty()) {
            author.setId(Long.parseLong(id));
        }

        if (StringUtils.isNullOrEmpty(commercialName) && StringUtils.isNullOrEmpty(fullName)) {
            MessageUtils.error(view, "Nome comercial ou Nome Completo é obrigatório.");
            return true;
        }
        return false;
    }
}
