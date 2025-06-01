package swing.configs;

import swing.model.Author;
import swing.model.Publisher;
import swing.service.AuthorService;
import swing.service.PublisherService;

/**
 * Classe responsável por inicializar o banco de dados com dados padrão.
 */
public class DatabaseInitializer {

    AuthorService authorService = new AuthorService();
    PublisherService publisherService = new PublisherService();

    public void init() {
        initAuthor();
        initPublisher();
    }

    /**
     * Método para inicializar o autor padrão no banco de dados.
     * Se o autor já existir, não faz nada.
     */
    private void initAuthor() {
        String authorName = "Autor Desconhecido";
        if (authorService.findByName(authorName) == null) {
            Author author = new Author();
            author.setFullName(authorName);
            author.setCommercialName(authorName);
            authorService.insert(author);
        }
    }

    /**
     * Método para inicializar a editora padrão no banco de dados.
     */
    private void initPublisher() {
        String publisherName = "Editora Desconhecida";
        if (publisherService.findByName(publisherName) == null) {
            Publisher publisher = new Publisher();
            publisher.setName(publisherName);
            publisherService.insert(publisher);
        }
    }
}
