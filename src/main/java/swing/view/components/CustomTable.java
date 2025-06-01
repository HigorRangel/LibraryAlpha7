package swing.view.components;

import swing.configs.GeneralProperties;
import swing.interfaces.Identifiable;
import swing.model.table.CustomTableModel;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


/**
 * CustomTable é uma extensão de JTable que permite a personalização de ações de edição e exclusão
 * para objetos do tipo Identifiable. Ele inclui funcionalidades de hover, renderização de ícones
 * e configuração de largura de colunas.
 *
 * @param <T> o tipo de objeto que a tabela irá manipular, deve estender Identifiable.
 */
public class CustomTable<T extends Identifiable> extends JTable {
    private int hoveredRow = -1;
    private int hoveredCol = -1;

    /**
     * Construtor da CustomTable. Configura a tabela com um modelo CustomTableModel
     *
     * @param clazz        o tipo de objeto que a tabela irá manipular, deve estender Identifiable.
     * @param editAction   ação a ser executada ao clicar no botão de edição, pode ser null se não houver ação de edição.
     * @param deleteAction ação a ser executada ao clicar no botão de exclusão, pode ser null se não houver ação de exclusão.
     */
    public CustomTable(Class<T> clazz, Consumer<T> editAction, Consumer<T> deleteAction) {
        super(new CustomTableModel<>(clazz, new ArrayList<>(), editAction != null, deleteAction != null));
        setBackground(GeneralProperties.BACKGROUND_COLOR);

        this.setOpaque(false);
        this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        this.setGridColor(Color.LIGHT_GRAY);
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.getTableHeader().setPreferredSize(new Dimension(getWidth(), 35));
        this.getTableHeader().setUI(new BasicTableHeaderUI());

        this.getTableHeader().setBackground(GeneralProperties.TRANSPARENT_COLOR);
        this.getTableHeader().setOpaque(true);
        this.getTableHeader().setDefaultRenderer(new TableHeaderRoundedBorderRenderer());

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point p = e.getPoint();
                int newHoveredRow = rowAtPoint(p);
                int newHoveredCol = columnAtPoint(p);

                if (newHoveredRow != hoveredRow || newHoveredCol != hoveredCol) {
                    hoveredRow = newHoveredRow;
                    hoveredCol = newHoveredCol;
                    updateCursor();
                    repaint();
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                hoveredRow = -1;
                hoveredCol = -1;
                setCursor(Cursor.getDefaultCursor());
                repaint();
            }
        });

        // Edição
        if (editAction != null) {
            int editColumn = getColumnCount() - (deleteAction != null ? 2 : 1);
            getColumnModel().getColumn(editColumn).setCellRenderer(new TableIconButtonRenderer(GeneralProperties.ICONS_EDIT_PNG));
            getColumnModel().getColumn(editColumn).setCellEditor(new TableIconButtonEditor<>(GeneralProperties.ICONS_EDIT_PNG, editAction));
        }

        // Exclusão
        if (deleteAction != null) {
            int deleteColumn = getColumnCount() - 1;
            getColumnModel().getColumn(deleteColumn).setCellRenderer(new TableIconButtonRenderer(GeneralProperties.ICONS_DELETE_PNG));
            getColumnModel().getColumn(deleteColumn).setCellEditor(new TableIconButtonEditor<>(GeneralProperties.ICONS_DELETE_PNG, deleteAction));
        }
        setColumnHeight();
        setRowHeight(25);
        setAutoCreateRowSorter(true);
    }

    /**
     * Configura a largura das colunas da tabela.
     */
    @SuppressWarnings("unchecked")
    public void setColumnHeight() {
        TableColumnModel columnModel = getColumnModel();
        CustomTableModel<T> model = (CustomTableModel<T>) getModel();

        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setPreferredWidth(150);
        }

        if (model.hasEditColumn()) {
            int editColumn = columnModel.getColumnCount() - (model.hasDeleteColumn() ? 2 : 1);
            columnModel.getColumn(editColumn).setPreferredWidth(25);
            columnModel.getColumn(editColumn).setMaxWidth(25);
        }

        if (model.hasDeleteColumn()) {
            int deleteColumn = columnModel.getColumnCount() - 1;
            columnModel.getColumn(deleteColumn).setPreferredWidth(25);
            columnModel.getColumn(deleteColumn).setMaxWidth(25);
        }

        setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }


    /**
     * Atualiza os dados da tabela com uma nova lista de objetos.
     *
     * @param newData a nova lista de objetos que substituirá os dados atuais da tabela.
     */
    @SuppressWarnings("unchecked")
    public void refreshData(List<T> newData) {
        CustomTableModel<T> model = (CustomTableModel<T>) getModel();
        model.setListObjects(newData);
        model.fireTableDataChanged();

        if (model.hasEditColumn()) {
            int editColumn = getColumnCount() - (model.hasDeleteColumn() ? 2 : 1);
            getColumnModel().getColumn(editColumn).setCellRenderer(new TableIconButtonRenderer(GeneralProperties.ICONS_EDIT_PNG));
        }

        if (model.hasDeleteColumn()) {
            int deleteColumn = getColumnCount() - 1;
            getColumnModel().getColumn(deleteColumn).setCellRenderer(new TableIconButtonRenderer(GeneralProperties.ICONS_DELETE_PNG));
        }
    }

    /**
     * Retorna a linha e coluna atualmente sobrevoadas pelo mouse.
     *
     * @return a linha atualmente sobrevoada pelo mouse.
     */
    public int getHoveredRow() {
        return hoveredRow;
    }

    /**
     * Retorna a coluna atualmente sobrevoada pelo mouse.
     *
     * @return a coluna atualmente sobrevoada pelo mouse.
     */
    public int getHoveredCol() {
        return hoveredCol;
    }

    /**
     * Atualiza o cursor da tabela com base na linha e coluna atualmente sobrevoadas.
     */
    private void updateCursor() {
        if (hoveredRow >= 0 && hoveredCol >= 0) {
            TableModel model = getModel();
            if (model instanceof CustomTableModel) {
                CustomTableModel<?> customModel = (CustomTableModel<?>) model;
                int columnCount = customModel.getColumnCount();

                boolean isActionColumn = (customModel.hasEditColumn() && hoveredCol == columnCount - 2) ||
                        (customModel.hasDeleteColumn() && hoveredCol == columnCount - 1);

                if (isActionColumn) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    repaint();
                    return;
                }
            }
        }
        setCursor(Cursor.getDefaultCursor());
        repaint();
    }


    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component c = super.prepareRenderer(renderer, row, column);

        if (isRowSelected(row)) {
            c.setBackground(GeneralProperties.TABLE_SELECTION_COLOR);
            c.setForeground(getSelectionForeground());
        } else {
            c.setBackground(getBackground());
            c.setForeground(getForeground());

            if (hoveredRow == row) {
                c.setBackground(GeneralProperties.TABLE_HOVER_COLOR);
            }
        }

        return c;
    }


    /**
     * Inclui placeholder para quando não houver dados na tabela.
     *
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (getRowCount() == 0) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            String message = "Nenhum dado encontrado";
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(message)) / 2;
            int y = getHeight() / 2;

            g2.setColor(Color.LIGHT_GRAY);
            g2.drawString(message, x, y);
            g2.dispose();
        }
    }


    @Override
    public void changeSelection(int row, int column, boolean toggle, boolean extend) {
        super.changeSelection(row, column, toggle, extend);

        if (getModel() instanceof CustomTableModel) {
            CustomTableModel<?> model = (CustomTableModel<?>) getModel();
            if (model.hasEditColumn() || model.hasDeleteColumn()) {
                repaint();
            }
        }
    }

    /**
     * Filtra os dados da tabela com base no texto fornecido
     *
     * @param searchText Texto para filtrar (ignora maiúsculas/minúsculas)
     * @param columns    Índices das colunas onde a busca deve ser aplicada
     *                   (null para buscar em todas as colunas)
     */
    @SuppressWarnings("unchecked")
    public void filterTable(String searchText, int... columns) {
        TableRowSorter<CustomTableModel<T>> sorter = new TableRowSorter<>((CustomTableModel<T>) getModel());
        this.setRowSorter(sorter);

        if (searchText == null || searchText.trim().isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            RowFilter<CustomTableModel<T>, Integer> filter = new RowFilter<CustomTableModel<T>, Integer>() {
                @Override
                public boolean include(Entry<? extends CustomTableModel<T>, ? extends Integer> entry) {
                    if (columns == null || columns.length == 0) {
                        for (int i = 0; i < entry.getModel().getColumnCount(); i++) {
                            if (entry.getModel().getValueAsString(entry.getIdentifier(), i)
                                    .contains(searchText.toLowerCase())) {
                                return true;
                            }
                        }
                    } else {
                        for (int col : columns) {
                            if (entry.getModel().getValueAsString(entry.getIdentifier(), col)
                                    .contains(searchText.toLowerCase())) {
                                return true;
                            }
                        }
                    }
                    return false;
                }
            };
            sorter.setRowFilter(filter);
        }
    }
}
