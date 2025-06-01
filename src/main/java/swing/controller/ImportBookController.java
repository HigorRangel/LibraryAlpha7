package swing.controller;

import swing.enums.LogErrorType;
import swing.model.AbstractModel;
import swing.model.Author;
import swing.model.Book;
import swing.model.Publisher;
import swing.model.dto.CSVImportableBookDTO;
import swing.service.AuthorService;
import swing.service.BookService;
import swing.service.PublisherService;
import swing.util.CSVUtils;
import swing.util.GeneralUtils;
import swing.util.MessageUtils;
import swing.view.ImportBooksView;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * Controlador responsável por gerenciar a importação de livros a partir de um arquivo CSV.
 */
public class ImportBookController extends AbstractController<ImportBooksView, Book> {


    public ImportBookController(ImportBooksView importBooksView) {
        super(importBooksView, new BookService());
    }

    /**
     * Método para importar livros de um file.
     *
     * @param file o file que contém os dados dos livros a serem importados.
     */
    public void importBooks(File file) {
        view.addLog(null, "====================== Iniciando importação de livros... ======================");
        if (file == null || !file.exists()) {
            MessageUtils.error(view, "Arquivo inválido ou não encontrado.");
            view.addLog(LogErrorType.ERROR, "Arquivo inválido ou não encontrado.");
            return;
        }
        if (!file.getName().endsWith(".csv")) {
            MessageUtils.error(view, "Formato de file inválido. Por favor, selecione um file CSV.");
            view.addLog(LogErrorType.ERROR, "Formato de file inválido. Por favor, selecione um file CSV.");
            return;
        }

        view.addLog(LogErrorType.INFO, "Lendo file: " + file.getAbsolutePath() + "...");

        try (FileReader reader = new FileReader(file)) {
            Set<CSVImportableBookDTO> books = CSVUtils.csvToObject(reader, CSVImportableBookDTO.class);
            if (books.isEmpty()) {
                MessageUtils.info(view, "Nenhum livro encontrado no file.");
                view.addLog(LogErrorType.WARNING, "Nenhum livro encontrado no file.");
                return;
            }
            view.addLog(LogErrorType.INFO, "Total de linhas encontrados: " + books.size());
            view.addLog(LogErrorType.INFO, "Iniciando a conversão dos books...");
            processBooks(books);

        } catch (IOException e) {
            MessageUtils.error(view, "Erro ao ler o file: " + e.getMessage());
            view.addLog(LogErrorType.ERROR, "Erro ao ler o file: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            MessageUtils.error(view, "Erro ao converter o file: " + e.getMessage());
            view.addLog(LogErrorType.ERROR, "Erro ao converter o file: " + e.getMessage());
        }
    }

    /**
     * Processa um conjunto de livros importados do CSV.
     *
     * @param books o conjunto de livros a serem processados.
     */
    private void processBooks(Set<CSVImportableBookDTO> books) {
        int currentLine = 0;
        for (CSVImportableBookDTO csvBook : books) {
            GeneralUtils.pause(1000);
            currentLine++;

            view.addLog(null, "\n\n\n ===============================================================");
            view.addLog(null, String.format("\n ℹ️ Processando linha %d: %s",
                    currentLine, (csvBook.getTitle() != null && !csvBook.getTitle().isEmpty()) ? csvBook.getTitle() : "(Sem título)"));

            try {
                if (csvBook.getImportErrors() != null && !csvBook.getImportErrors().isEmpty()) {
                    view.addLog(LogErrorType.ERROR, "❌ Livro com erros: " + csvBook.getTitle() + ": ");
                    for (String error : csvBook.getImportErrors()) {
                        view.addLog(null, "    - " + error);
                    }
                    continue;
                }
                view.addLog(LogErrorType.INFO, "Livro é válido:");
                view.addLog(null, "    - Título: " + csvBook.getTitle());
                String authors = csvBook.getAuthors().stream()
                        .map(Author::getCommercialName)
                        .collect(Collectors.joining(", "));
                view.addLog(null, "    - Autores: " + authors);
                view.addLog(null, "    - ISBN: " + csvBook.getIsbn());
                String publishers = csvBook.getPublishers().stream()
                        .map(Publisher::getName)
                        .collect(Collectors.joining(", "));
                view.addLog(null, "    - Editoras: " + publishers);
                view.addLog(null, "    - Data de publicação: " + csvBook.getPublicationDate());

                Book book = ((BookService) service).findBookByIsbn(csvBook.getIsbn());
                if (book == null) {
                    view.addLog(LogErrorType.INFO, "\uD83D\uDD04 Livro não existe. Criando...");
                    book = new Book();
                } else {
                    view.addLog(LogErrorType.INFO, "Livro já existe no banco de dados. ID =  " + book.getId());
                    view.addLog(LogErrorType.INFO, "\uD83D\uDD04 Atualizando livro existente...");
                    compareAndUpdate(book, csvBook);
                    continue;
                }

                book.setTitle(csvBook.getTitle());
                book.setIsbn(csvBook.getIsbn());
                book.setPublicationDate(csvBook.getPublicationDate());
                processAuthors(book, csvBook);
                processPublishers(book, csvBook);
                view.addLog(LogErrorType.INFO, "\uD83D\uDD04 Salvando livro...");

                try {
                    book = service.insert(book);
                } catch (Exception e) {
                    view.addLog(LogErrorType.ERROR, "❌ Erro ao salvar livro: " + csvBook.getTitle() + " - " + e.getMessage());
                    continue;
                }

                if (book == null) {
                    view.addLog(LogErrorType.ERROR, "❌ Erro ao salvar livro: " + csvBook.getTitle());
                    continue;
                }
                view.addLog(LogErrorType.INFO, "✅ Livro criado com sucesso com o ID '" + book.getId() + "'");
            } catch (Exception e) {
                view.addLog(LogErrorType.ERROR, "❌ Erro ao importar csvBook: " + csvBook.getTitle() + " - " + e.getMessage());
            } finally {
                view.addLog(null, "====================== ✅ Fim do processamento da linha " + currentLine + " ======================");
            }
        }
    }

    /**
     * Compara os dados de um livro existente com os dados de um livro CSV e atualiza o livro se necessário.
     *
     * @param book    o livro existente a ser atualizado.
     * @param bookCSV o livro CSV com os dados a serem comparados.
     */
    private void compareAndUpdate(Book book, CSVImportableBookDTO bookCSV) {
        boolean changed = false;
        if (!book.getTitle().equalsIgnoreCase(bookCSV.getTitle())) {
            view.addLog(LogErrorType.INFO, "Título do livro changed (anterior: " + book.getTitle() + ", novo: " + bookCSV.getTitle() + ")");
            book.setTitle(bookCSV.getTitle());
            changed = true;
        }
        if (book.getIsbn() == null || !book.getIsbn().equalsIgnoreCase(bookCSV.getIsbn())) {
            view.addLog(LogErrorType.INFO, "ISBN do livro changed (anterior: " + book.getIsbn() + ", novo: " + bookCSV.getIsbn() + ")");
            book.setIsbn(bookCSV.getIsbn());
            changed = true;
        }

        if (book.getPublicationDate() == null || !book.getPublicationDate().equals(bookCSV.getPublicationDate())) {
            view.addLog(LogErrorType.INFO, "Data de publicação do livro alterada (anterior: " + book.getPublicationDate() + ", nova: " + bookCSV.getPublicationDate() + ")");
            book.setPublicationDate(bookCSV.getPublicationDate());
            changed = true;
        }

        if (!book.getPublishers().containsAll(bookCSV.getPublishers())
                || !bookCSV.getPublishers().containsAll(book.getPublishers())) {
            processPublishers(book, bookCSV);
            changed = true;
        }

        if (!book.getAuthors().containsAll(bookCSV.getAuthors())
                || !bookCSV.getAuthors().containsAll(book.getAuthors())) {
            processAuthors(book, bookCSV);
            changed = true;
        }

        if (changed) {
            view.addLog(LogErrorType.INFO, "Salvando livro atualizado...");
            service.update(book);
        } else {
            view.addLog(LogErrorType.INFO, "Nenhuma alteração necessária para o livro: " + book.getTitle());
        }
    }

    /**
     * Se as editoras do livro foram alteradas, atualiza o livro com as novas editoras.
     * Se não existirem editoras no livro, adiciona as editoras do CSV.
     *
     * @param book    o livro a ser atualizado.
     * @param bookCSV o livro CSV com as novas editoras.
     */
    private void processPublishers(Book book, CSVImportableBookDTO bookCSV) {
        updateRelashionship(
                "Editora",
                new HashSet<>(book.getPublishers()),
                new HashSet<>(bookCSV.getPublishers()),
                book.getPublishers()::add,
                book.getPublishers()::remove,
                publisher -> {
                    PublisherService service = new PublisherService();
                    Publisher existPublisher = service.findByName(publisher.getName());
                    if (existPublisher != null) {
                        return existPublisher;
                    }
                    return service.insert(publisher);
                }
        );
    }

    /**
     * Se os autores do livro foram alterados, atualiza o livro com os novos autores.
     *
     * @param book    o livro a ser atualizado.
     * @param bookCSV o livro CSV com os novos autores.
     */
    private void processAuthors(Book book, CSVImportableBookDTO bookCSV) {
        updateRelashionship(
                "Autor",
                new HashSet<>(book.getAuthors()),
                new HashSet<>(bookCSV.getAuthors()),
                book.getAuthors()::add,
                book.getAuthors()::remove,
                author -> {
                    AuthorService service = new AuthorService();
                    if (author.getId() == null || author.getId() <= 0) {
                        Author existAuthor = service.findByName(author.getCommercialName());
                        if (existAuthor != null) return existAuthor;
                        return service.insert(author);
                    }
                    return author;
                }
        );
    }

    /**
     * Atualiza um relacionamento ManyToMany de um livro com base nos dados do CSV.
     *
     * @param <T>              Tipo do relacionamento (Autor, Editora, etc.)
     * @param entityName       Nome da entidade para exibição no ‘log’.
     * @param entityBefore     Conjunto original da entidade no livro.
     * @param entityAfter      Conjunto da entidade vinda do CSV.
     * @param addConsumer      Função que realiza a adição da entidade ao livro.
     * @param deleteConsumer   Função que realiza a remoção da entidade do livro.
     * @param optionalResolver (opcional) resolvedor que, dado um ‘item’ do CSV, retorna a versão persistida ou salva no banco.
     */
    private <T extends AbstractModel> void updateRelashionship(
            String entityName,
            Set<T> entityBefore,
            Set<T> entityAfter,
            Consumer<T> addConsumer,
            Consumer<T> deleteConsumer,
            UnaryOperator<T> optionalResolver
    ) {
        List<T> addedEntities = entityAfter.stream()
                .filter(e -> !entityBefore.contains(e))
                .collect(Collectors.toList());

        List<T> removedEntities = entityBefore.stream()
                .filter(e -> !entityAfter.contains(e))
                .collect(Collectors.toList());

        if (addedEntities.isEmpty() && removedEntities.isEmpty()) return;

        for (T item : addedEntities) {
            view.addLog(LogErrorType.INFO, "Adicionando " + entityName + ": " + (item.getId() == null ? "" : item.getId().toString()));

            T finalItem = item;

            if (optionalResolver != null) {
                try {
                    T resolved = optionalResolver.apply(item);
                    if (resolved != null) finalItem = resolved;
                } catch (Exception e) {
                    view.addLog(LogErrorType.ERROR, "Erro ao resolver " + entityName + ": " + item + " - " + e.getMessage());
                    continue;
                }
            }
            addConsumer.accept(finalItem);
        }
        for (T item : removedEntities) {
            view.addLog(LogErrorType.INFO, "Removendo " + entityName + ": " + (item.getId() == null ? "" : item.getId().toString()));
            deleteConsumer.accept(item);
        }
        view.addLog(LogErrorType.INFO, entityName + " atualizados. Total após atualização: " + entityAfter.size());
    }
}
