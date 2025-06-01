package swing.view;

import swing.components.CustomTablePanel;
import swing.components.CustomTextField;
import swing.components.PanelRoundedBorder;
import swing.configs.GeneralProperties;
import swing.controller.BookController;
import swing.model.Book;
import swing.model.dto.BookDTO;
import swing.util.ComponentUtils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

/**
 * Classe que representa a visão de livros.
 */
public class BookView extends JPanel {

    private final BookController bookController = new BookController(this);
    private CustomTablePanel<BookDTO> table;


    public BookView() {
        initComponents();
    }

    /**
     * Método para inicializar os componentes da visão de livros.
     */
    private void initComponents() {
        setLayout(new BorderLayout());

        this.setPreferredSize(new Dimension(900, 600));
        this.setMinimumSize(new Dimension(900, 600));
        this.setMaximumSize(new Dimension(900, 600));
        this.setSize(new Dimension(900, 600));
        loadTable();
        loadForm();
        setBackground(GeneralProperties.BACKGROUND_COLOR);
    }


    /**
     * Método para inicializar a tabela de livros
     */
    private void loadTable() {
        table = new CustomTablePanel<>(BookDTO.class,
                bookController::editBook,
                bookController::deleteBook,
                (livro) -> refreshBookTable(),
                (livro) -> openForm(null)

        );

        table.addCustomButton("Importar CSV", GeneralProperties.ICONS_IMPORT_PNG, e -> abrirImportarLivrosCSV());

        refreshBookTable();

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.RED);
        tablePanel.setPreferredSize(new Dimension(800, 25));
        Border border = new PanelRoundedBorder(2f, GeneralProperties.PRIMARY_RADIUS, GeneralProperties.PRIMARY_COLOR,
                true, true, true, true,
                new Insets(10, 10, 10, 10));
        tablePanel.setOpaque(false);
        tablePanel.setBorder(border);

        tablePanel.add(table, BorderLayout.CENTER);
        add(tablePanel);
        revalidate();
        repaint();
    }

    /**
     * Método para abrir o formulário de importação de livros a partir de um arquivo CSV
     */
    private void abrirImportarLivrosCSV() {
        ImportBooksView importBooksView = new ImportBooksView((JFrame) SwingUtilities.getWindowAncestor(this));
        importBooksView.setVisible(true);
        importBooksView.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent windowEvent) {
                refreshBookTable();
            }
        });
    }

    /**
     * Método para abrir o formulário de registro de livros
     *
     * @param book o livro a ser editado, ou null para criar um novo livro
     */
    public void openForm(Book book) {
        BookRegistrationView bookRegistrationView = new BookRegistrationView((JFrame) SwingUtilities.getWindowAncestor(this), book);
        bookRegistrationView.setVisible(true);
        bookRegistrationView.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent windowEvent) {
                refreshBookTable();
            }
        });
    }


    /**
     * Método para atualizar a tabela de livros com os dados mais recentes
     */
    public void refreshBookTable() {
        List<BookDTO> livros = bookController.getBooksDTO();
        table.refreshData(livros);
    }

    /**
     * Método para carregar o formulário de busca de livros
     */
    private void loadForm() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.setBackground(GeneralProperties.BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        Insets rotuloInsets = new Insets(5, 5, 0, 5);

        //Campo Search
        //Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 12;
        gbc.weightx = 0.0;
        gbc.insets = rotuloInsets;
        formPanel.add(new JLabel("Busca"), gbc);

        //Campo
        gbc.gridy = 1;
        gbc.gridwidth = 11;
        gbc.weightx = 1.0;
        CustomTextField campoBusca = new CustomTextField();

        ComponentUtils.addListenerSearchFieldTable(campoBusca, table.getTable());
        formPanel.add(campoBusca, gbc);
        add(formPanel, BorderLayout.NORTH);
    }
}
