package swing.view;

import swing.controller.DashboardController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Objects;

/**
 * Tela principal de dashboard com estatísticas da biblioteca.
 */
public class DashboardView extends JPanel {

    JLabel labelLivros;
    JLabel labelAutores;
    JLabel labelEditoras;
    JLabel labelUltimoLivro;

    DashboardController controller = new DashboardController();

    public DashboardView() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Logo
        JLabel logoLabel = new JLabel(new ImageIcon(Objects.requireNonNull(DashboardView.class.getResource("/images/a7-logo.png"))));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(logoLabel, BorderLayout.NORTH);

        // Painel central com os quadrantes
        JPanel panelCenter = new JPanel(new GridLayout(2, 2, 20, 20));
        panelCenter.setBorder(new EmptyBorder(20, 40, 40, 40));
        panelCenter.setBackground(Color.WHITE);

        // Quadrantes
        labelLivros = createCard("📚 Total de Livros", controller.getBookCount());
        labelAutores = createCard("👤 Total de Autores", controller.getAuthorCount());
        labelEditoras = createCard("🏢 Total de Editoras", controller.getPublisherCount());
        labelUltimoLivro = createCard("🆕 Último Livro", controller.getLastBookName());

        panelCenter.add(labelLivros.getParent());
        panelCenter.add(labelAutores.getParent());
        panelCenter.add(labelEditoras.getParent());
        panelCenter.add(labelUltimoLivro.getParent());

        add(panelCenter, BorderLayout.CENTER);
    }

    /**
     * Cria um painel de estatística com título e valor.
     *
     * @param titulo Título do quadrante
     * @param valor  Valor a ser exibido
     * @return JLabel que pode ser atualizado com novos valores
     */
    private JLabel createCard(String titulo, String valor) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        card.setBackground(new Color(245, 245, 245));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel title = new JLabel(titulo);
        title.setFont(new Font("SansSerif", Font.BOLD, 14));
        title.setForeground(Color.DARK_GRAY);

        JLabel value = new JLabel(valor);
        value.setFont(new Font("SansSerif", Font.PLAIN, 24));
        value.setHorizontalAlignment(SwingConstants.RIGHT);
        value.setForeground(new Color(60, 60, 60));

        card.add(title, BorderLayout.NORTH);
        card.add(value, BorderLayout.CENTER);

        return value;
    }

    public void setTotalLivros(int total) {
        labelLivros.setText(String.valueOf(total));
    }

    public void setTotalAutores(int total) {
        labelAutores.setText(String.valueOf(total));
    }

    public void setTotalEditoras(int total) {
        labelEditoras.setText(String.valueOf(total));
    }

    public void setUltimoLivro(String nomeLivro) {
        labelUltimoLivro.setText(nomeLivro);
    }
}