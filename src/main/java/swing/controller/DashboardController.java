package swing.controller;

import swing.model.AbstractModel;
import swing.model.Author;
import swing.model.Book;
import swing.service.AuthorService;
import swing.service.BookService;
import swing.service.PublisherService;
import swing.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;

public class DashboardController {


    BookService bookService = new BookService();
    AuthorService authorService = new AuthorService();
    PublisherService publisherController = new PublisherService();

    /**
     * Método que retorna a quantidade de livros cadastrados no sistema.
     *
     * @return ‘String’ com a quantidade de livros.
     */
    public String getBookCount() {
        List<Book> books = bookService.findAll();
        return CollectionUtils.isEmpty(books) ? "0" : String.valueOf(books.size());
    }

    /**
     * Método que retorna a quantidade de autores cadastrados no sistema.
     *
     * @return ‘String’ com a quantidade de autores.
     */
    public String getAuthorCount() {
        List<Author> authors = authorService.findAll();
        return CollectionUtils.isEmpty(authors) ? "0" : String.valueOf(authors.size());
    }

    /**
     * Método que retorna a quantidade de editoras cadastradas no sistema.
     *
     * @return ‘String’ com a quantidade de editoras.
     */
    public String getPublisherCount() {
        return String.valueOf(publisherController.findAll().size());
    }

    /**
     * Método que retorna o nome do último livro cadastrado no sistema.
     *
     * @return ‘String’ com o nome do último livro ou "Nenhum" se não houver livros.
     */
    public String getLastBookName() {
        List<Book> books = bookService.findAll();
        if (CollectionUtils.isEmpty(books)) {
            return "Nenhum";
        }
        Book lastBook = books.stream()
                .max(Comparator.comparing(AbstractModel::getCreatedAt)).orElse(null);
        return (lastBook != null) ? lastBook.getTitle() : "Nenhum";
    }
}
