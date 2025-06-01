package swing.view;

import swing.configs.GeneralProperties;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe principal da ‘interface’ gráfica do sistema de controlo de livros.
 */
public class MainView extends JFrame {

    /**
     * Mapa para armazenar os botões de navegação.
     */
    final Map<String, JButton> navButtons = new HashMap<>();

    /**
     * Painel de navegação superior que contém os botões para mudar de tela.
     */
    JPanel navBar;

    /**
     * Layout de cartões utilizado para alternar entre diferentes telas.
     */
    CardLayout cardLayout = new CardLayout();

    /**
     * Barra de ferramentas que contém os botões de navegação.
     */
    JToolBar toolBar1;

    /**
     * Painel de conteúdo que contém as diferentes telas do sistema.
     */
    private JPanel contentPanel;

    /**
     * Construtor da classe MainView.
     */
    public MainView() {
        initComponents();
    }

    /**
     * Inicializar os componentes da interface gráfica.
     */
    private void initComponents() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(900, 600));
        setMinimumSize(new Dimension(900, 600));
        setMaximumSize(new Dimension(900, 600));
        setBackground(GeneralProperties.BACKGROUND_COLOR);
        setSize(new Dimension(900, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Sistema de Controle de Livros");
        createTopMenu();
        initContentPanel();
        setVisible(true);
    }

    /**
     * Cria o menu superior com os botões de navegação.
     */
    private void createTopMenu() {
        navBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        navBar.setBackground(Color.RED);

        String[] telas = {"Dashboard", "Livro", "Autor", "Editora"};
        toolBar1 = new JToolBar();
        toolBar1.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        toolBar1.setBackground(GeneralProperties.BACKGROUND_COLOR);

        for (String nome : telas) {
            JButton btn = new JButton(nome);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            btn.addActionListener(e -> {
                changeScreen(nome);
                highlightActiveButton(nome);
            });
            navButtons.put(nome, btn);
            toolBar1.add(btn);
        }
        add(toolBar1, BorderLayout.NORTH);
    }


    /**
     * Inicializa o painel de conteúdo que contém as diferentes telas do sistema.
     */
    private void initContentPanel() {
        contentPanel = new JPanel();
        contentPanel.setBackground(GeneralProperties.BACKGROUND_COLOR);

        cardLayout = new CardLayout();

        contentPanel.setLayout(cardLayout);

        contentPanel.add(new BookView(), "Livro");
        contentPanel.add(new AuthorView(), "Autor");
        contentPanel.add(new PublisherView(), "Editora");
        contentPanel.add(new DashboardView(), "Dashboard");

        add(contentPanel, BorderLayout.CENTER);

        changeScreen("Dashboard");
        highlightActiveButton("Dashboard");
    }


    /**
     * Muda a tela exibida no painel de conteúdo.
     *
     * @param nome Nome da tela a ser exibida.
     */
    private void changeScreen(String nome) {
        cardLayout.show(contentPanel, nome);
    }


    /**
     * Destaca o botão ativo na barra de navegação.
     *
     * @param ativo Nome do botão ativo.
     */
    private void highlightActiveButton(String ativo) {
        for (Map.Entry<String, JButton> entry : navButtons.entrySet()) {
            JButton btn = entry.getValue();
            if (entry.getKey().equals(ativo)) {
                btn.setBackground(GeneralProperties.PRIMARY_COLOR);
                btn.setForeground(Color.WHITE);
                btn.setFont(btn.getFont().deriveFont(Font.BOLD));
            } else {
                btn.setBackground(null);
                btn.setForeground(Color.BLACK);
                btn.setFont(btn.getFont().deriveFont(Font.PLAIN));
            }
        }
    }
}
