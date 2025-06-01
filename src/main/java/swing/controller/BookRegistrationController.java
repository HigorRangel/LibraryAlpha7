package swing.controller;

import swing.model.Author;
import swing.model.Book;
import swing.model.Publisher;
import swing.model.dto.BookDTO;
import swing.service.AuthorService;
import swing.service.BookService;
import swing.service.PublisherService;
import swing.util.CollectionUtils;
import swing.util.DateUtils;
import swing.util.MessageUtils;
import swing.view.BookRegistrationView;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controlador para gerenciar o registro de livros.
 * Este controlador lida com a validação de ISBN, salvamento de livros,
 * carregamento de autores e editoras, e exclusão de livros.
 */
public class BookRegistrationController extends AbstractController<BookRegistrationView, Book> {

    final AuthorService authorService = new AuthorService();

    public BookRegistrationController(BookRegistrationView view) {
        super(view, new BookService());
    }


    /**
     * Salva um livro no sistema.
     *
     * @param id              o ‘ID’ do livro, se estiver a editar um livro existente.
     * @param title           o título do livro.
     * @param authors         uma lista de ‘IDs’ dos authors do livro.
     * @param publishers      uma lista de ‘IDs’ dos publishers do livro.
     * @param isbn            o ISBN do livro, deve ser um ISBN-13 válido.
     * @param publicationDate a data de publicação do livro no formato dd/MM/yyyy.
     * @param similarBooks    um conjunto de livros similares ao livro atual, pode ser vazio.
     * @return true se o livro foi salvo com sucesso, false caso contrário.
     */
    public boolean saveBook(String id, String title, List<Long> authors, List<Long> publishers, String isbn,
                            String publicationDate, Set<Book> similarBooks) {
        boolean isEdit = id != null && !id.isEmpty();
        Book book = new Book();

        if (isEdit) {
            book = service.findById(Long.parseLong(id));
            if (book == null) {
                MessageUtils.error(view, "Livro não encontrado.");
                return false;
            }
        }

        PublisherService publisherService = new PublisherService();
        book.setTitle(title);
        book.setAuthors(new LinkedHashSet<>(authorService.findByIds(authors)));
        book.setPublishers(new LinkedHashSet<>(publisherService.findByIds(publishers)));
        book.setIsbn(isbn);
        book.setSimilarBooks(similarBooks);
        if (validateFieldsValues(id, title, isbn, publicationDate, book)) return false;

        try {
            if (isEdit) {
                service.update(book);
            } else {
                service.insert(book);
            }
            MessageUtils.info(view, "Livro salvo com sucesso!");
        } catch (Exception e) {
            MessageUtils.error(view, "Erro ao salvar o livro: " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Valida os campos do livro antes de salvar.
     *
     * @param id              o ‘ID’ do livro, se estiver editando um livro existente.
     * @param title           o título do livro.
     * @param isbn            o ISBN do livro, deve ser um ISBN-13 válido.
     * @param publicationDate a data de publicação do livro no formato dd/MM/yyyy.
     * @param book            o objeto Livro a ser validado.
     * @return true se houver algum erro de validação, false caso contrário.
     */
    private boolean validateFieldsValues(String id, String title, String isbn, String publicationDate, Book book) {
        if (id != null && !id.isEmpty()) {
            book.setId(Long.parseLong(id));
        }

        if (title == null || title.isEmpty()) {
            MessageUtils.error(view, "Título é obrigatório.");
            return true;
        }
        if (CollectionUtils.isEmpty(book.getPublishers())) {
            boolean response = MessageUtils.confirm(view, "Pelo menos uma editora é obrigatória. Deseja associar a uma editora desconhecida?");
            if (!response) {
                return true;
            }
        }

        if (CollectionUtils.isEmpty(book.getAuthors())) {
            boolean response = MessageUtils.confirm(view, "Pelo menos um autor é obrigatório. Deseja associar " +
                    " a um autor desconhecido?");
            if (!response) {
                return true;
            }
        }

        if (isbn == null || isbn.isEmpty()) {
            MessageUtils.error(view, "ISBN é obrigatório.");
            return true;
        }

//        if (!((BookService) service).isValidISBN13(isbn)) {
//            MessageUtils.error(view, "ISBN inválido. Deve ser um ISBN-13 válido.");
//            return true;
//        }

        try {
            if (publicationDate != null && !publicationDate.isEmpty()) {
                book.setPublicationDate(DateUtils.textToDate(publicationDate, LocalDate.class));
            }
        } catch (DateTimeParseException e) {
            MessageUtils.error(view, "Data de publicação inválida. Use o formato dd/MM/yyyy.");
            return true;
        }

        if (DateUtils.isDateAfterToday(book.getPublicationDate())) {
            MessageUtils.error(view, "Data de publicação não pode ser no futuro.");
            return true;
        }
        return false;
    }

    /**
     * Carrega os autores disponíveis no sistema.
     *
     * @return um conjunto de autores.
     */
    public Set<Author> loadAuthors() {
        return new LinkedHashSet<>(authorService.findAll());
    }

    /**
     * Carrega as editoras disponíveis no sistema.
     *
     * @return um conjunto de editoras.
     */
    public Set<Publisher> loadPublishers() {
        PublisherService publisherService = new PublisherService();
        return new LinkedHashSet<>(publisherService.findAll());
    }

    /**
     * Exclui um livro do sistema.
     *
     * @param book o livro a ser excluído.
     */
    public boolean deleteBook(Book book) {
        if (book == null) {
            MessageUtils.error(view, "Livro não encontrado.");
            return false;
        }

        //Confirmar exclusão
        boolean response = MessageUtils.confirm(view, "Tem certeza que deseja excluir o livro '" + book.getTitle() + "'?");
        if (!response) {
            return false;
        }

        try {
            service.delete(book);
            MessageUtils.info(view, "Livro excluído com sucesso!");
            return true;
        } catch (Exception e) {
            MessageUtils.error(view, "Erro ao excluir o livro: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca um livro pelo ISBN.
     *
     * @param text o ISBN a ser buscado, pode conter hífens e espaços.
     * @return um LivroDTO com os dados do livro encontrado, ou null se não encontrado.
     */
    public BookDTO findByIsbn(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }

        Book book = ((BookService) service).findBookByIsbnAPI(text);
        if (book == null) {
            return null;
        }
        return new BookDTO(book);
    }

    /**
     * Preenche a lista de authorNames selecionados com base na string de authorNames.
     *
     * @param authorNames ‘String’ contendo os authorNames separados por vírgula.
     * @return Lista de itens selecionáveis com os authorNames preenchidos.
     */
    public List<Author> getFilledAuthors(String authorNames) {
        if (authorNames == null || authorNames.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> listAuthorNames = Arrays.asList(authorNames.split(","));
        if (listAuthorNames.isEmpty()) {
            return new ArrayList<>();
        }

        return listAuthorNames.stream()
                .map(authorName -> {
                    Author author = authorService.findByName(authorName.trim());
                    if (author == null) {
                        boolean shouldCreate = MessageUtils.confirm(view,
                                "Autor '" + authorName.trim() + "' não encontrado. Deseja criar um novo autor?");
                        if (shouldCreate) {
                            author = createNewAuthor(authorName);
                        } else {
                            return null;
                        }
                    }
                    return author;
                })
                .filter(Objects::nonNull).distinct().collect(Collectors.toList());
    }


    /**
     * Cria um autor com o nome fornecido.
     *
     * @param authorName Nome do autor a ser criado.
     * @return O autor criado ou null se ocorrer um error.
     */
    private Author createNewAuthor(String authorName) {
        Author author;
        author = new Author();
        author.setFullName(authorName.trim());
        author.setCommercialName(authorName.trim());
        try {
            author = authorService.insert(author);
            return author;
        } catch (Exception e) {
            MessageUtils.error(view, "Erro ao criar novo autor: " + e.getMessage());
            return null;
        }
    }

    /**
     * Preenche a lista de editoras selecionadas com base na string de publisherNames.
     *
     * @param publisherNames String contendo os publisherNames separados por vírgula.
     * @return Lista de itens selecionáveis com as editoras preenchidas.
     */
    public List<Publisher> getFilledPublishers(String publisherNames) {
        if (publisherNames == null || publisherNames.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> listPublisherNames = Arrays.asList(publisherNames.split(","));
        if (listPublisherNames.isEmpty()) {
            return new ArrayList<>();
        }
        PublisherService publisherService = new PublisherService();

        return listPublisherNames.stream()
                .map(publisherName -> {
                    Publisher publisher = publisherService.findByName(publisherName.trim());
                    if (publisher == null) {
                        boolean shouldCreate = MessageUtils.confirm(view,
                                "Editora '" + publisherName.trim() + "' não encontrada. Deseja criar uma nova editora?");
                        if (shouldCreate) {
                            publisher = new Publisher(publisherName.trim());
                            try {
                                publisher = publisherService.insert(publisher);
                            } catch (Exception e) {
                                MessageUtils.error(view, "Erro ao criar nova editora: " + e.getMessage());
                                return null;
                            }
                        } else {
                            return null;
                        }
                    }
                    return publisher;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Carrega todos os livros com exceção do livro atual.
     *
     * @param book o livro atual para evitar duplicação.
     * @return uma lista de livros similares ao livro atual.
     */
    public List<Book> loadSimilarBooks(Book book) {
        if (book == null) {
            return Collections.emptyList();
        }
        List<Book> allBooks = service.findAll();
        if (CollectionUtils.isEmpty(allBooks)) {
            return Collections.emptyList();
        }
        allBooks.removeIf(b -> b.getId().equals(book.getId()) ||
                b.getTitle().equalsIgnoreCase(book.getTitle()) ||
                b.getIsbn().equalsIgnoreCase(book.getIsbn()));

        return allBooks;
    }
}
