package swing.service;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import swing.configs.GeneralProperties;
import swing.model.Author;
import swing.model.Book;
import swing.model.Publisher;
import swing.repository.BookRepository;
import swing.util.CollectionUtils;
import swing.util.StringUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Serviço responsável por gerenciar as operações relacionadas aos livros.
 * Extende a classe AbstractService para fornecer funcionalidades básicas de CRUD.
 */
public class BookService extends AbstractService<Book> {

    private static final String BASE_URL = "https://openlibrary.org/api/books";
    private static final String FORMAT = "?bibkeys=ISBN:%s&jscmd=data&format=json";
    final BookRepository bookRepository = new BookRepository();
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Preenche os autores e editoras do livro com valores padrão caso estejam vazios.
     *
     * @param entity O livro a ser preenchido.
     */
    private static void fillAuthorAndPublisher(Book entity) {
        AuthorService authorService = new AuthorService();
        PublisherService publisherService = new PublisherService();
        if (CollectionUtils.isEmpty(entity.getAuthors())) {
            entity.setAuthors(new LinkedHashSet<>(Collections.singleton(authorService.findByName(GeneralProperties.NAME_UNKNOWN_AUTHOR))));
        }
        if (CollectionUtils.isEmpty(entity.getPublishers())) {
            entity.setPublishers(new LinkedHashSet<>(Collections.singleton(publisherService.findByName(GeneralProperties.NAME_UNKNOWN_PUBLISHER))));
        }
    }

    /**
     * Busca um livro pelo ISBN no banco de dados.
     *
     * @param isbn O ISBN do livro a ser buscado.
     * @return Um objeto Livro contendo as informações do livro, ou null se não encontrado.
     */
    public Book findBookByIsbn(String isbn) {
        if (isbn == null || isbn.isEmpty()) {
            return null;
        }

        List<Book> books = bookRepository.findBookByIsbn(isbn);
        if (books == null || books.isEmpty()) {
            return null;
        }
        return books.get(0);
    }

    /**
     * Busca informações de um livro pelo ISBN na Open Library.
     *
     * @param url URL da API da Open Library com o ISBN do livro.
     * @return Uma ‘String’ contendo os dados do livro em formato JSON.
     * @throws IOException Se ocorrer um error de entrada/saída durante a requisição HTTP.
     */
    private String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        try (Response response = call.execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erro inesperado: " + response);
            }
            if (response.body() == null) {
                return "";
            }
            return response.body().string();
        }
    }

    /**
     * Busca informações de um livro pelo ISBN. Busca na Open Library API.
     *
     * @param isbn O ISBN do livro a ser buscado.
     * @return Um objeto Livro contendo as informações do livro, ou null se não encontrado.
     */
    public Book findBookByIsbnAPI(String isbn) {
        String url = BASE_URL + String.format(FORMAT, isbn);
        try {
            String jsonResponse = get(url);
            if (jsonResponse.isEmpty()) {
                return null;
            }

            JSONObject jsonObject = new JSONObject(jsonResponse);
            if (!jsonObject.has(String.format("ISBN:%s", isbn))) {
                return null;
            }
            JSONObject bookData = jsonObject.getJSONObject(String.format("ISBN:%s", isbn));
            if (bookData == null) {
                return null;
            }

            Book book = findBookByIsbn(isbn);
            if (book == null) {
                book = new Book();
            }

            book.setTitle(bookData.optString("title", "Título não disponível"));
            book.setIsbn(isbn);
            book.setPublishers(getEditoras(bookData));
            book.setPublicationDate(converterData(bookData.optString("publish_date", "Data não disponível")));
            book.setAuthors(getAutores(bookData));

            return book;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Obtém a lista de editoras de um livro a partir dos dados JSON.
     *
     * @param bookData Os dados do livro em formato JSON.
     * @return Um conjunto de objeto Editora contendo as editoras do livro.
     */
    private Set<Publisher> getEditoras(JSONObject bookData) {
        if (!bookData.has("publishers")) {
            return new LinkedHashSet<>();
        }
        PublisherService publisherService = new PublisherService();
        Set<Publisher> publishers = new LinkedHashSet<>();
        for (Object authorObj : bookData.getJSONArray("publishers")) {
            JSONObject authorJson = (JSONObject) authorObj;
            String nome = authorJson.optString("name", "Editora não disponível");
            Publisher publisher = publisherService.findByName(nome);
            if (publisher == null) {
                publisher = new Publisher();
                publisher.setName(nome);
            }

            publishers.add(publisher);
        }
        return publishers;
    }

    /**
     * Obtém a lista de autores de um livro a partir dos dados JSON.
     *
     * @param bookData Os dados do livro em formato JSON.
     * @return Uma lista de objeto Autor contendo os autores do livro.
     */
    private Set<Author> getAutores(JSONObject bookData) {
        if (!bookData.has("authors")) {
            return new LinkedHashSet<>();
        }
        AuthorService authorService = new AuthorService();
        Set<Author> autores = new LinkedHashSet<>();
        for (Object authorObj : bookData.getJSONArray("authors")) {
            JSONObject authorJson = (JSONObject) authorObj;
            String nomeAutor = authorJson.optString("name", "Autor não disponível");
            Author authorExistente = authorService.findByName(nomeAutor);

            if (authorExistente != null) {
                autores.add(authorExistente);
                continue;
            }
            authorExistente = new Author();
            authorExistente.setFullName(nomeAutor);
            authorExistente.setCommercialName(nomeAutor);

            autores.add(authorExistente);
        }
        return autores;
    }

    /**
     * Converte uma ‘string’ de data para um objeto LocalDate.
     * Formatos possíveis: "April 15, 1997", "March 2009", "1985", "1st ed.".
     *
     * @param s A ‘string’ representando a data a ser convertida.
     * @return Um objeto LocalDate representando a data, ou null se não for possível converter.
     */
    private LocalDate converterData(String s) {
        if (s == null || s.isEmpty()) return null;

        s = s.trim();

        s = s.replaceAll("[?c]", "").trim();

        // ("April 15, 1997")
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
            return LocalDate.parse(s, formatter);
        } catch (DateTimeParseException ignored) {
        }

        // ("March 2009")
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH);
            YearMonth ym = YearMonth.parse(s, formatter);
            return ym.atDay(1);
        } catch (DateTimeParseException ignored) {
        }

        // ("1985")
        try {
            int year = Integer.parseInt(s);
            return LocalDate.of(year, 1, 1);
        } catch (NumberFormatException ignored) {
        }

        // ("1945-06-08")
        try {
            return LocalDate.parse(s);
        } catch (DateTimeParseException ignored) {
        }

        // 4. Outros casos (ex: "1st ed.")
        return null;
    }

    /**
     * Valida as informações de um livro antes de inseri-lo ou editá-lo.
     *
     * @param book O livro a ser validado.
     */
    private void validateBook(boolean isEdition, Book book) {
        if (book == null) {
            throw new IllegalArgumentException("O livro não pode ser nulo.");
        }
        if (StringUtils.isNullOrEmpty(book.getTitle())) {
            throw new IllegalArgumentException("O título do livro não pode ser vazio.");
        }
        if (StringUtils.isNullOrEmpty(book.getIsbn())) {
            throw new IllegalArgumentException("O ISBN do livro não pode ser vazio.");
        }
        if (!isEdition && findBookByIsbn(StringUtils.removeExcessiveSpaces(book.getIsbn())) != null) {
            throw new IllegalArgumentException("Já existe um livro com o ISBN: " + book.getIsbn());
        }
        if (!isEdition && findBookByTitle(StringUtils.removeExcessiveSpaces(book.getTitle())) != null) {
            throw new IllegalArgumentException("Já existe um livro com o título: " + book.getTitle());
        }
//        if (!isValidISBN13(book.getIsbn())) {
//            throw new IllegalArgumentException("O ISBN do livro é inválido.");
//        }
    }

    /**
     * Valida as informações de um livro antes de inseri-lo.
     *
     * @param title Título do livro a ser validado.
     * @return Um objeto Livro contendo as informações do livro, ou null se não encontrado.
     */
    private Book findBookByTitle(String title) {
        if (title == null || title.isEmpty()) {
            return null;
        }

        List<Book> books = bookRepository.findBookByTitle(title);
        if (CollectionUtils.isEmpty(books)) {
            return null;
        }
        return books.get(0);
    }

    @Override
    public void beforeInsert(Book entity) {
        super.beforeInsert(entity);
        validateBook(false, entity);
        entity.setTitle(StringUtils.removeExcessiveSpaces(entity.getTitle()));

        fillAuthorAndPublisher(entity);
    }

    @Override
    public void beforeUpdate(Book newEntity, Book oldEntity) {
        super.beforeUpdate(newEntity, oldEntity);
        validateBook(true, newEntity);
        newEntity.setTitle(StringUtils.removeExcessiveSpaces(newEntity.getTitle()));
        fillAuthorAndPublisher(newEntity);
    }

//    /**
//     * Valida se o ISBN é válido no formato ISBN-10 ou ISBN-13.
//     *
//     * @param input o ISBN a ser validado, pode conter hífens e espaços.
//     * @return true se o ISBN for válido, false caso contrário.
//     */
//    public boolean isValidISBN13(String input) {
//        if (input == null) return false;
//        if (input.startsWith("999")) return true;
//
//        String digits = input.replaceAll("[^\\d]", "");
//        if (digits.length() != 13) return false;
//
//        if (!digits.startsWith("978") && !digits.startsWith("979")) return false;
//
//        return isValidCheckDigit(digits);
//    }
//
//    /**
//     * Verifica se o dígito de verificação do ISBN-13 é válido.
//     *
//     * @param isbn o ISBN-13 a ser verificado, já sem formatação.
//     * @return true se o dígito de verificação for válido, false caso contrário.
//     */
//    private boolean isValidCheckDigit(String isbn) {
//        if (isbn.startsWith("999")) return true;
//
//        int sum = 0;
//        for (int i = 0; i < 12; i++) {
//            int digit = Character.getNumericValue(isbn.charAt(i));
//            sum += (i % 2 == 0) ? digit : digit * 3;
//        }
//        int expectedCheck = (10 - (sum % 10)) % 10;
//        int actualCheck = Character.getNumericValue(isbn.charAt(12));
//        return expectedCheck == actualCheck;
//    }
}
