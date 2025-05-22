package swing.view;

import swing.controller.LivroController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MainView extends JFrame {

    private JLabel titleLabel;
    private JPanel painelConteudo;
    JPanel navBar;
    CardLayout cardLayout = new CardLayout();
    JToolBar toolBar1;


    Map<String, JButton> navButtons = new HashMap<>();

    public MainView() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 600));
        setMinimumSize(new Dimension(800, 600));
        setMaximumSize(new Dimension(800, 600));
        setSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Sistema de Controle de Livros");
        criaMenuSuperior();
        iniciarPainelConteudo();
        //        pack();
//        setVisible(true);

        setVisible(true);
//        setBackground(Color.white);


//        titleLabel = new JLabel("Sistema de Controle de Livros");
//        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
//        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
//        titleLabel.setPreferredSize(new Dimension(800, 25));
//        add(titleLabel);
//
//        iniciarTabela();


    }

    private void criaMenuSuperior() {
        navBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String[] telas = {"Livro", "Autor"};
        toolBar1 = new JToolBar();
        toolBar1.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        for (String nome : telas) {
            JButton btn = new JButton(nome);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            btn.addActionListener(e -> {
                trocarTela(nome);
                destacarBotaoAtivo(nome);
            });
            navButtons.put(nome, btn);

            toolBar1.add(btn);
        }

        add(toolBar1, BorderLayout.NORTH);
    }


    private void iniciarPainelConteudo() {
        painelConteudo = new JPanel();
        cardLayout = new CardLayout();
        painelConteudo.setLayout(cardLayout);

        painelConteudo.add(new LivroView(), "Livro");
        painelConteudo.add(new AutorView(), "Autor");

        add(painelConteudo, BorderLayout.CENTER);

        // Iniciar com "Livro"
        trocarTela("Livro");
        destacarBotaoAtivo("Livro");
    }


    private void trocarTela(String nome) {
        cardLayout.show(painelConteudo, nome);
    }

    private void destacarBotaoAtivo(String ativo) {
        for (Map.Entry<String, JButton> entry : navButtons.entrySet()) {
            JButton btn = entry.getValue();
            if (entry.getKey().equals(ativo)) {
                btn.setBackground(Color.LIGHT_GRAY);
                btn.setFont(btn.getFont().deriveFont(Font.BOLD));
            } else {
                btn.setBackground(null);
                btn.setFont(btn.getFont().deriveFont(Font.PLAIN));
            }
        }
    }
}
