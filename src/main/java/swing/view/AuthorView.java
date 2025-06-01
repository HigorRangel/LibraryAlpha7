package swing.view;

import swing.components.CustomTablePanel;
import swing.components.CustomTextField;
import swing.components.PanelRoundedBorder;
import swing.configs.GeneralProperties;
import swing.controller.AuthorController;
import swing.model.Author;
import swing.model.dto.AuthorDTO;
import swing.util.ComponentUtils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

/**
 * Um JPanel que representa a visualização do autor.
 */
public class AuthorView extends JPanel {

    private final AuthorController authorController = new AuthorController(this);
    private CustomTablePanel<AuthorDTO> table;


    public AuthorView() {
        initComponents();
    }

    /**
     * Método para inicializar os componentes da visão de autors.
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
     * Método para inicializar a tabela de autors
     */
    private void loadTable() {
        table = new CustomTablePanel<>(AuthorDTO.class,
                authorController::editAuthor,
                authorController::deleteAuthor,
                (autor) -> refreshAuthorTable(),
                (autor) -> openForm(null)

        );

        refreshAuthorTable();

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
     * Método para abrir o formulário de registro de autors
     *
     * @param author o autor a ser editado, ou null para criar um novo autor
     */
    public void openForm(Author author) {
        AuthorRegistrationView authorRegistrationView = new AuthorRegistrationView((JFrame) SwingUtilities.getWindowAncestor(this), author);
        authorRegistrationView.setVisible(true);
        authorRegistrationView.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent windowEvent) {
                refreshAuthorTable();
            }
        });
    }


    /**
     * Método para atualizar a tabela de autors com os dados mais recentes
     */
    public void refreshAuthorTable() {
        List<AuthorDTO> autors = authorController.getAuthorsDTO();
        table.refreshData(autors);
    }

    /**
     * Método para carregar o formulário de busca de autors
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
