package swing.view;

import swing.components.CustomTablePanel;
import swing.components.CustomTextField;
import swing.components.PanelRoundedBorder;
import swing.configs.GeneralProperties;
import swing.controller.PublisherController;
import swing.model.Publisher;
import swing.model.dto.PublisherDTO;
import swing.util.ComponentUtils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

/**
 * Um JPanel que representa a visualização do editora.
 */
public class PublisherView extends JPanel {

    private final PublisherController publisherController = new PublisherController(this);
    private CustomTablePanel<PublisherDTO> table;


    public PublisherView() {
        initComponents();
    }

    /**
     * Método para inicializar os componentes da visão de editoras.
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
     * Método para inicializar a tabela de editoras
     */
    private void loadTable() {
        table = new CustomTablePanel<>(PublisherDTO.class,
                publisherController::editPublisher,
                publisherController::deletePublisher,
                (editora) -> refreshPublisherTable(),
                (editora) -> openForm(null)

        );

        refreshPublisherTable();

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
     * Método para abrir o formulário de registro de editoras
     *
     * @param publisher o editora a ser editado, ou null para criar um novo editora
     */
    public void openForm(Publisher publisher) {
        PublisherRegistrationView publisherRegistrationView = new PublisherRegistrationView((JFrame) SwingUtilities.getWindowAncestor(this), publisher);
        publisherRegistrationView.setVisible(true);
        publisherRegistrationView.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent windowEvent) {
                refreshPublisherTable();
            }
        });
    }


    /**
     * Método para atualizar a tabela de editoras com os dados mais recentes
     */
    public void refreshPublisherTable() {
        List<PublisherDTO> editoras = publisherController.getPublishersDTO();
        table.refreshData(editoras);
    }

    /**
     * Método para carregar o formulário de busca de editoras
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
