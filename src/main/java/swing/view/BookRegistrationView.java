package swing.view;

import swing.components.*;
import swing.controller.BookRegistrationController;
import swing.enums.InputType;
import swing.model.Author;
import swing.model.Book;
import swing.model.Publisher;
import swing.model.dto.BookDTO;
import swing.util.DateUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Tela de cadastro/edição de Livro.
 */
public class BookRegistrationView extends JDialog {

    final boolean isEdition;
    private final JPanel mainPanel;
    private final BookRegistrationController controller = new BookRegistrationController(this);
    Book book;
    private CustomTextField fieldTxtId;
    private CustomTextField fieldTxtTitle;
    private FieldGroupMultiSelect<Author> fieldAuthors;
    private FieldGroupMultiSelect<Publisher> fieldPublishers;
    private FieldGroupMultiSelect<Book> fieldBooks;
    private CustomMaskedText fieldPublicationDate;
    private CustomTextField txtISBN;
    private FieldGroup<CustomTextField> fieldGroupId;

    public BookRegistrationView(JFrame parent) {
        this(parent, null);
    }

    public BookRegistrationView(JFrame parent, Book book) {
        super(parent, "Cadastro de Livro", true);
        isEdition = book != null;

        this.book = book;
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        loadForm();
        cleanDate();
        drawForm();
        setContentPane(mainPanel);
        setMinimumSize(new Dimension(400, 300));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(parent);
        setResizable(false);

        if (book == null) {
            return;
        }
        fieldTxtTitle.setText(book.getTitle());
        fieldPublishers.setSelectedItems(new ArrayList<>(book.getPublishers()));
        fieldAuthors.setSelectedItems(new ArrayList<>(book.getAuthors()));
        fieldPublicationDate.setText(DateUtils.dateToText(book.getPublicationDate(), true));
        txtISBN.setText(book.getIsbn());
    }


    /**
     * Desenha o formulário de cadastro/edição de livro.
     */
    private void drawForm() {
        JPanel formPanel = getFormPanel();

        // Botões
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // Botão de salvar
        JButton btnSave = new CustomButton("Salvar");
        btnSave.addActionListener(e -> {
            if (book == null) {
                book = new Book();
            }
            boolean successfully = controller.saveBook(getId(), getTitle(), getIdAuthors(), getIdPublishers(),
                    getISBN(), getPublicationDate(), getSimilarBooks());
            if (successfully) {
                dispose();
            }
        });

        // Botão de excluir
        JButton btnDelete = new CustomButton("Excluir");
        btnDelete.addActionListener(e -> deleteRecord());

        // Botão de cancelar
        JButton btnCancel = new CustomButton("Cancelar");
        btnCancel.addActionListener(e -> {
            cleanDate();
            dispose();
        });

        buttonsPanel.add(btnSave);

        if (isEdition) {
            buttonsPanel.add(btnDelete);
        }

        buttonsPanel.add(btnCancel);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
    }

    /**
     * Retorna os livros semelhantes selecionados.
     *
     * @return Conjunto de livros semelhantes.
     */
    private Set<Book> getSimilarBooks() {
        return new LinkedHashSet<>(fieldBooks.getSelectedItems());
    }

    /**
     * Exclui o registro do livro.
     */
    private void deleteRecord() {
        boolean response = controller.deleteBook(book);
        if (response) {
            dispose();
        }
    }

    /**
     * Cria o painel do formulário de cadastro/edição de livro.
     * @return JPanel contendo os campos do formulário.
     */
    private JPanel getFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        fieldGroupId = new FieldGroup<>(fieldTxtId, "ID");
        formPanel.add(fieldGroupId);

        if (book != null) {
            fieldTxtId.setText(String.valueOf(book.getId()));
            fieldTxtId.setEnabled(false);
            fieldGroupId.setVisible(true);

        } else {
            fieldTxtId.setEnabled(false);
            fieldGroupId.setVisible(false);
        }

        FieldGroup<CustomTextField> fieldGroupISBN = new FieldGroup<>(txtISBN, "ISBN");
        formPanel.add(fieldGroupISBN);

        FieldGroup<CustomTextField> fieldGroupTitle = new FieldGroup<>(fieldTxtTitle, "Título");
        formPanel.add(fieldGroupTitle);

        List<Publisher> publishers = loadPublishers();
        fieldPublishers = new FieldGroupMultiSelect<>(this, publishers, () -> {
            Publisher newPublisher = new Publisher();
            newPublisher.setName("Nova Editora");
            return newPublisher;
        });
        FieldGroup<FieldGroupMultiSelect<Publisher>> fieldGroupPublisher = new FieldGroup<>(fieldPublishers, "Editoras");
        formPanel.add(fieldGroupPublisher);


        List<Book> similarBooks = loadSimilarBooks(this.book);
        fieldBooks = new FieldGroupMultiSelect<>(this, similarBooks, null);
        FieldGroup<FieldGroupMultiSelect<Book>> fieldGroupSimilarBooks = new FieldGroup<>(fieldBooks, "Livros Semelhantes");
        formPanel.add(fieldGroupSimilarBooks);

        List<Author> authors = loadAuthors();
        FieldGroup<FieldGroupMultiSelect<Author>> fieldGroupAuthor = new FieldGroup<>(fieldAuthors, "Autores");
        fieldAuthors = new FieldGroupMultiSelect<>(this, authors, () -> {
            openAuthorRegistrationForm();
            return null;
        });
        fieldGroupAuthor.add(fieldAuthors);

        // Adicionando ao painel do formulário
        formPanel.add(fieldGroupAuthor);


        FieldGroup<CustomMaskedText> fieldGroupPublicationDate = new FieldGroup<>(fieldPublicationDate, "Data de Publicação");
        formPanel.add(fieldGroupPublicationDate);
        return formPanel;
    }

    /**
     * Carrega a lista de livros, com exceção do livro atual (se houver).
     *
     * @param book Livro atual, que não deve ser incluído na lista de semelhantes.
     * @return Lista de livros semelhantes.
     */
    private List<Book> loadSimilarBooks(Book book) {
        return controller.loadSimilarBooks(book);
    }

    /**
     * Abre o formulário de registro de autor.
     */
    private void openAuthorRegistrationForm() {
        AuthorRegistrationView authorRegistrationView = new AuthorRegistrationView((JFrame) SwingUtilities.getWindowAncestor(this));
        authorRegistrationView.setVisible(true);
        authorRegistrationView.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                fieldAuthors.setAllItems(loadAuthors());
            }
        });
    }

    /**
     * Carrega a lista de editoras disponíveis.
     *
     * @return Lista de editoras.
     */
    private List<Publisher> loadPublishers() {
        return new ArrayList<>(controller.loadPublishers());
    }


    /**
     * Carrega a lista de autores disponíveis.
     *
     * @return Lista de autores.
     */
    private List<Author> loadAuthors() {
        return new ArrayList<>(controller.loadAuthors());
    }

    /**
     * Carrega os componentes do formulário.
     */
    private void loadForm() {
        fieldTxtId = new CustomTextField();
        fieldTxtId.setEnabled(false);

        fieldTxtId.setHorizontalAlignment(SwingConstants.CENTER);

        txtISBN = new CustomTextField();
        txtISBN.setRequired(true);
        txtISBN.setMaxLength(13);
        txtISBN.setMinLength(13);
        txtISBN.setInputType(InputType.NUMERIC);
        txtISBN.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (txtISBN.getText().isEmpty() || txtISBN.getText().length() < 13) {
                    return;
                }
                if (isEdition) {
                    return;
                }
                BookDTO bookDTO = controller.findByIsbn(txtISBN.getText());
                if (bookDTO != null) {
                    loadFormData(bookDTO);
                }
            }
        });

        fieldTxtTitle = new CustomTextField();
        fieldTxtTitle.setRequired(true);
        fieldTxtTitle.setMaxLength(100);

        fieldAuthors = new FieldGroupMultiSelect<>(this, new ArrayList<>(), null);
        fieldAuthors.setRequired(true);

        fieldPublishers = new FieldGroupMultiSelect<>(this, new ArrayList<>(), null);
        fieldPublishers.setRequired(true);

        fieldPublicationDate = new CustomMaskedText("##/##/####");
        fieldPublicationDate.setMaxLength(10);
        fieldPublicationDate.setRequired(true);

        fieldAuthors = new FieldGroupMultiSelect<>(this, new ArrayList<>(), null);
    }

    /**
     * Preenche os fields do formulário com os dados do LivroDTO.
     *
     * @param bookDTO Objeto LivroDTO contendo os dados do livro.
     */
    private void loadFormData(BookDTO bookDTO) {
        fieldTxtTitle.setText(bookDTO.getTitle());
        List<Author> filledAuthors = controller.getFilledAuthors(bookDTO.getAuthors());
        List<Publisher> filledPublishers = controller.getFilledPublishers(bookDTO.getPublishers());

        fieldAuthors.setAllItems(loadAuthors());
        fieldPublishers.setAllItems(loadPublishers());

        fieldAuthors.setSelectedItemsByRawItems(filledAuthors);
        fieldPublishers.setSelectedItemsByRawItems(filledPublishers);
        String publisherDate = bookDTO.getPublicationDate();
        if (publisherDate != null && !publisherDate.isEmpty()) {
            LocalDate localDate = DateUtils.textToDate(publisherDate, LocalDate.class);
            if (localDate != null) {
                fieldPublicationDate.setText(DateUtils.dateToText(localDate, true));
            } else {
                fieldPublicationDate.setText("");
            }
        }
        txtISBN.setText(bookDTO.getIsbn());
    }


    /**
     * Limpa os campos do formulário.
     */
    private void cleanDate() {
        fieldTxtId.setText("");
        fieldTxtTitle.setText("");
        fieldAuthors.setSelectedItems(new ArrayList<>());
        fieldPublishers.setSelectedItems(new ArrayList<>());
        fieldPublicationDate.setText("");
        txtISBN.setText("");
        fieldTxtId.setEnabled(false);
        if (fieldGroupId != null) {
            fieldGroupId.setVisible(false);
        }
    }


    /**
     * Retorna o ID do livro.
     *
     * @return ID do livro.
     */
    public String getId() {
        return fieldTxtId.getText();
    }

    /**
     * Retorna o título do livro.
     *
     * @return Título do livro.
     */
    public String getTitle() {
        return fieldTxtTitle.getText();
    }

    /**
     * Retorna a lista de IDs dos autores selecionados.
     *
     * @return Lista de IDs dos autores selecionados.
     */
    public List<Long> getIdAuthors() {
        return fieldAuthors.getSelectedIds();
    }

    /**
     * Retorna a lista de IDs das editoras selecionadas.
     *
     * @return Lista de IDs das editoras selecionadas.
     */
    public List<Long> getIdPublishers() {
        return fieldPublishers.getSelectedIds();
    }

    /**
     * Retorna a data de publicação do livro.
     *
     * @return Data de publicação do livro no formato "dd/MM/yyyy".
     */
    public String getPublicationDate() {
        return fieldPublicationDate.getText();
    }

    /**
     * Retorna o ISBN do livro.
     *
     * @return ISBN do livro.
     */
    public String getISBN() {
        return txtISBN.getText();
    }

}
