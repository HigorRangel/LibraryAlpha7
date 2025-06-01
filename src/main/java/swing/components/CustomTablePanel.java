package swing.components;

import swing.configs.GeneralProperties;
import swing.interfaces.Identifiable;

import javax.swing.*;
import javax.swing.plaf.basic.BasicToolBarUI;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class CustomTablePanel<T extends Identifiable> extends JPanel {
    CustomTable<T> table;
    TableToolbar toolBar;
    Consumer<T> rowEditAction;
    Consumer<T> rowDeleteAction;
    Class<T> clazz;

    Consumer<T> tableRefreshAction;
    Consumer<T> tableNewAction;


    public CustomTablePanel(Class<T> clazz, Consumer<T> rowEditAction,
                            Consumer<T> rowDeleteAction, Consumer<T> tableRefreshAction, Consumer<T> tableNewAction) {
        this.tableRefreshAction = tableRefreshAction;
        this.tableNewAction = tableNewAction;
        this.rowEditAction = rowEditAction;
        this.rowDeleteAction = rowDeleteAction;

        this.clazz = clazz;
        this.setBackground(GeneralProperties.TRANSPARENT_COLOR);


        setLayout(new BorderLayout());
        initToolbar();
        initTable();
        addDefaultButtons();
    }

    /**
     * Adiciona botões padrão à barra de ferramentas, como "Atualizar" e "Novo".
     */
    private void addDefaultButtons() {
        if (tableRefreshAction != null) {
            addToolbarButton("Atualizar", GeneralProperties.ICONS_REFRESH_PNG, tableRefreshAction);
        }
        if (tableNewAction != null) {
            addToolbarButton("Novo", GeneralProperties.ICONS_ADD_PNG, tableNewAction);
        }
    }

    /**
     * Inicializa a barra de ferramentas com um layout personalizado e estilo.
     */
    private void initToolbar() {
        toolBar = new TableToolbar();
        toolBar.setFloatable(false);
        toolBar.setLayout(new FlowLayout(FlowLayout.RIGHT, 8, 5));
        toolBar.setOpaque(false);
        toolBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        toolBar.setUI(new BasicToolBarUI());
        add(toolBar, BorderLayout.NORTH);
    }


    /**
     * Adiciona um botão à barra de ferramentas com ícone e ação personalizada.
     *
     * @param tooltip  Dica de ferramenta do botão
     * @param iconPath Caminho do ícone do botão
     * @param onClick  Ação a ser executada ao clicar no botão
     */
    public void addToolbarButton(String tooltip, String iconPath, Consumer<T> onClick) {
        JButton button = new JButton();
        button.setToolTipText(tooltip);
        button.setIcon(loadIcon(iconPath));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        styleButton(button);
        button.addActionListener(e -> onClick.accept(null));
        if (toolBar != null) {
            toolBar.add(button, 0);
            toolBar.revalidate();
            toolBar.repaint();
        }
    }

    /**
     * Aplica estilo ao botão, definindo tamanho, UI personalizada e margens.
     *
     * @param button O botão a ser estilizado
     */
    private void styleButton(JButton button) {
        button.setPreferredSize(new Dimension(22, 22));
        button.setUI(new RoundedButtonUI());
        button.setMargin(new Insets(2, 2, 2, 2));

    }

    /**
     * Carrega um ícone a partir de um caminho especificado, redimensionando-o para 12x12 pixels.
     *
     * @param path Caminho do ícone a ser carregado
     * @return Icon redimensionado
     */
    private Icon loadIcon(String path) {
        ImageIcon originalIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(path)));
        Image scaledImage = originalIcon.getImage().getScaledInstance(12, 12, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    /**
     * Inicializa a tabela com os dados fornecidos.
     */
    private void initTable() {
        table = new CustomTable<>(this.clazz, rowEditAction, rowDeleteAction);
        table.setOpaque(false);
        table.setBackground(new Color(255, 255, 255, 200));
        table.setFillsViewportHeight(true);

        JPanel roundedPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
            }
        };
        roundedPanel.setOpaque(false);
        roundedPanel.setBackground(GeneralProperties.BACKGROUND_COLOR);
        roundedPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        roundedPanel.add(scrollPane, BorderLayout.CENTER);
        add(roundedPanel, BorderLayout.CENTER);
    }

    /**
     * Adiciona um botão personalizado à barra de ferramentas.
     *
     * @param tooltip  Dica de ferramenta do botão
     * @param iconPath Caminho do ícone do botão
     * @param onClick  Ação a ser executada ao clicar no botão
     */
    public void addCustomButton(String tooltip, String iconPath, Consumer<T> onClick) {
        addToolbarButton(tooltip, iconPath, onClick);
    }

    /**
     * Retorna a tabela associada a este painel.
     *
     * @return JTable associada a este painel
     */
    public CustomTable<T> getTable() {
        return table;
    }

    /**
     * Atualiza os dados da tabela com uma nova lista de ‘items’.
     *
     * @param items Lista de ‘items’ a serem exibidos na tabela
     */
    public void refreshData(List<T> items) {
        if (table != null) {
            table.refreshData(items);
        }
    }

    /**
     * Retorna o RowSorter associado a esta tabela.
     *
     * @return RowSorter associado a esta tabela
     */
    public RowSorter<? extends TableModel> getRowSorter() {
        return table.getRowSorter();
    }

}
