package swing.controller;

import org.hibernate.exception.ConstraintViolationException;
import swing.model.Book;
import swing.model.dto.BookDTO;
import swing.service.BookService;
import swing.util.MessageUtils;
import swing.view.BookView;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador responsável por gerir as operações relacionadas aos livros.
 */
public class BookController extends AbstractController<BookView, Book> {


    public BookController(BookView bookView) {
        super(bookView, new BookService());
    }

    /**
     * Obtém uma lista de LivroDTOs representando todos os livros.
     *
     * @return uma lista de LivroDTOs.
     */
    public List<BookDTO> getBooksDTO() {
        List<Book> books = service.findAll();
        return books.stream()
                .map(BookDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Abre o formulário de cadastro de livro para edição.
     *
     * @param rowValue o livro a ser editado, representado por um LivroDTO.
     */
    public void editBook(BookDTO rowValue) {
        Book book = service.findById(rowValue.getId());
        if (book != null) {
            view.openForm(book);
        } else {
            MessageUtils.error(view, "Livro não encontrado.");
        }
    }

    /**
     * Exclui um livro selecionado.
     *
     * @param selectedBook o livro a ser excluído.
     */
    public void deleteBook(BookDTO selectedBook) {
        if (selectedBook == null) {
            MessageUtils.error(view, "Nenhum livro selecionado para exclusão.");
            return;
        }

        if (!MessageUtils.confirm(view, "Deseja realmente excluir o livro: " + selectedBook.getTitle() + "?")) {
            return;
        }
        Book book = service.findById(selectedBook.getId());
        if (book != null) {
            try {
                service.delete(book);
                view.refreshBookTable();
                MessageUtils.info(view, "Livro excluído com sucesso.");
            } catch (ConstraintViolationException e) {
                MessageUtils.error(view, "Não é possível excluir o livro, pois ele está vinculado a autores ou editoras.");
            } catch (Exception e) {
                MessageUtils.error(view, "Erro ao excluir livro: " + e.getMessage());
            }
        } else {
            MessageUtils.error(view, "Livro não encontrado.");
        }
    }
}
