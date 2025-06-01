package swing.view.components;

import swing.interfaces.Identifiable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * Classe que implementa um diálogo de seleção múltipla com uma tabela.
 * Permite ao usuário selecionar itens de uma lista filtrável.
 */
public class MultiSelectTableDialog<T extends Identifiable> extends JDialog {
    private List<T> selectedItems;
    private boolean canceled = true;
    private ItemTableModel tableModel;
    private Supplier<T> onNewItemClick;

    public MultiSelectTableDialog(Window parent, String title, List<T> allItems, Supplier<T> onNewItemClick) {
        super(parent, title, ModalityType.APPLICATION_MODAL);
        this.onNewItemClick = onNewItemClick;
        initialize(allItems);
    }


    /**
     * Inicializa o modal com os itens disponíveis.
     *
     * @param allItems Lista de itens que serão exibidos na tabela.
     */
    private void initialize(List<T> allItems) {
        JPanel searchPanel = new JPanel(new BorderLayout());
        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 25));
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                filter();
            }

            public void insertUpdate(DocumentEvent e) {
                filter();
            }

            public void removeUpdate(DocumentEvent e) {
                filter();
            }

            private void filter() {
                String text = searchField.getText().toLowerCase();
                tableModel.filter(text);
            }
        });

        searchPanel.add(new JLabel("Buscar:"), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.NORTH);


        setLayout(new BorderLayout());
        setSize(600, 400);
        setLocationRelativeTo(getParent());

        tableModel = new ItemTableModel(allItems);

        JTable table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        TableColumnModel columnModel = table.getColumnModel();

        TableColumn checkboxColumn = columnModel.getColumn(0);
        checkboxColumn.setPreferredWidth(20);
        checkboxColumn.setMinWidth(20);
        checkboxColumn.setMaxWidth(20);
        checkboxColumn.setCellRenderer(new CheckBoxRenderer());
        checkboxColumn.setCellEditor(new CheckBoxEditor(new JCheckBox()));

        TableColumn idColumn = columnModel.getColumn(1);
        idColumn.setPreferredWidth(50);
        idColumn.setMinWidth(50);
        idColumn.setMaxWidth(100);
        idColumn.setCellRenderer(centerRenderer);

        TableColumn descColumn = columnModel.getColumn(2);
        descColumn.setPreferredWidth(200);

        TableColumn detailColumn = columnModel.getColumn(3);
        detailColumn.setPreferredWidth(200);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = getButtonPanel();

        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Cria o painel de botões com as ações de seleção, confirmação e cancelamento.
     *
     * @return Um JPanel contendo os botões de ação.
     */
    private JPanel getButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        CustomButton selectAllButton = new CustomButton("Selecionar Todos");
        selectAllButton.addActionListener(e -> selectAll(true));

        CustomButton deselectAllButton = new CustomButton("Deselecionar Todos");
        deselectAllButton.addActionListener(e -> selectAll(false));

        CustomButton okButton = new CustomButton("OK");
        okButton.addActionListener(e -> confirmSelection());

        CustomButton cancelButton = new CustomButton("Cancelar");
        cancelButton.addActionListener(e -> dispose());

        CustomButton newItemButton = new CustomButton("Novo Item");
        newItemButton.addActionListener(e -> createNewItem());

        buttonPanel.add(okButton);
        buttonPanel.add(selectAllButton);
        buttonPanel.add(deselectAllButton);
        if (onNewItemClick != null) {
            buttonPanel.add(newItemButton);
        }
        buttonPanel.add(cancelButton);
        return buttonPanel;
    }

    /**
     * Cria um ‘item’ do tipo T, utilizando o fornecedor definido.
     */
    private void createNewItem() {
        if (onNewItemClick != null) {
            T newItem = onNewItemClick.get();
            if (newItem != null) {
                tableModel.items.add(newItem);
                tableModel.filteredItems.add(newItem);
                tableModel.selectionStates = Arrays.copyOf(tableModel.selectionStates, tableModel.items.size());
                tableModel.fireTableDataChanged();
            }
        }
    }

    /**
     * Seleciona ou desseleciona todos os itens na tabela.
     *
     * @param select Se true, seleciona todos os itens; se false, desseleciona todos.
     */
    private void selectAll(boolean select) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt(select, i, 0);
        }
    }

    /**
     * Confirma a seleção dos itens escolhidos pelo usuário.
     */
    private void confirmSelection() {
        selectedItems = tableModel.getSelectedItems();
        canceled = false;
        dispose();
    }

    /**
     * Retorna a lista de itens selecionados pelo usuário.
     *
     * @return Lista de itens selecionados.
     */
    public List<T> getSelectedItems() {
        return tableModel != null ? tableModel.getSelectedItems() : Collections.emptyList();
    }

    /**
     * Define os itens selecionados na tabela.
     *
     * @param selectedItems Lista de itens selecionados a serem definidos na tabela.
     */
    public void setSelectedItems(List<T> selectedItems) {
        this.selectedItems = selectedItems != null ? new ArrayList<>(selectedItems) : new ArrayList<>();

        if (tableModel != null) {
            tableModel.updateSelection(this.selectedItems);
        }
    }

    /**
     * Verifica se o diálogo foi cancelado.
     *
     * @return true se o diálogo foi cancelado, false se a seleção foi confirmada.
     */
    public boolean isCanceled() {
        return canceled;
    }

    /**
     * Define todos os itens disponíveis na tabela.
     *
     * @param allItems Lista de itens que serão exibidos na tabela.
     */
    public void setAllItems(List<T> allItems) {
        if (tableModel != null) {
            tableModel.items = new ArrayList<>(allItems);
            tableModel.filteredItems = new ArrayList<>(allItems);
            tableModel.selectionStates = new boolean[allItems.size()];
            tableModel.fireTableDataChanged();
        }
    }

    /**
     * Retorna o fornecedor que será chamado quando o usuário clicar no botão "Novo ‘Item’".
     *
     * @return O fornecedor que cria um ‘item’.
     */
    public Supplier<T> getOnNewItemClick() {
        return onNewItemClick;
    }

    /**
     * Define o fornecedor que será chamado quando o usuário clicar no botão "Novo ‘Item’".
     *
     * @param onNewItemClick O fornecedor que cria um ‘item’.
     */
    public void setOnNewItemClick(Supplier<T> onNewItemClick) {
        this.onNewItemClick = onNewItemClick;
    }

    /**
     * Classe interna para o renderizador de checkboxes na tabela.
     */
    private static class CheckBoxRenderer extends JCheckBox implements TableCellRenderer {
        public CheckBoxRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
            setVerticalAlignment(SwingConstants.CENTER);
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setSelected((value != null && (Boolean) value));
            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());

            setBorder(BorderFactory.createEmptyBorder(0, table.getColumnModel().getColumn(column).getWidth() / 2 - 10, 0, 0));
            return this;
        }
    }

    /**
     * Classe interna para o editor de checkboxes na tabela.
     */
    private static class CheckBoxEditor extends DefaultCellEditor {
        public CheckBoxEditor(JCheckBox checkBox) {
            super(checkBox);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            JCheckBox editor = (JCheckBox) super.getTableCellEditorComponent(table, value, isSelected, row, column);
            editor.setSelected((value != null && (Boolean) value));
            return editor;
        }
    }

    /**
     *  Modelo de tabela para exibir os itens selecionáveis.
     */
    private class ItemTableModel extends AbstractTableModel {
        private final String[] columnNames = {"", "ID", "Nome", "Descrição"};
        private List<T> items;
        private List<T> filteredItems;
        private boolean[] selectionStates;

        public ItemTableModel(List<T> items) {
            this.items = new ArrayList<>(items);
            this.selectionStates = new boolean[items.size()];
            this.filteredItems = new ArrayList<>(items);

            if (selectedItems != null) {
                for (int i = 0; i < items.size(); i++) {
                    selectionStates[i] = selectedItems.contains(items.get(i));
                }
            }
        }

        public void updateSelection(List<T> selected) {
            Arrays.fill(selectionStates, false);
            for (int i = 0; i < items.size(); i++) {
                if (selected.contains(items.get(i))) {
                    selectionStates[i] = true;
                }
            }
            fireTableDataChanged();
        }

        public void updateSelectionStates(List<T> selectedItems) {
            for (int i = 0; i < items.size(); i++) {
                selectionStates[i] = selectedItems.contains(items.get(i));
            }
            fireTableDataChanged();
        }

        public void filter(String searchText) {
            filteredItems.clear();
            if (searchText.isEmpty()) {
                filteredItems.addAll(items);
            } else {
                for (T item : items) {
                    if (item.getDisplayName().toLowerCase().contains(searchText) ||
                            item.getDescription().toLowerCase().contains(searchText) ||
                            String.valueOf(item.getId()).contains(searchText)) {
                        filteredItems.add(item);
                    }
                }
            }
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return filteredItems.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return columnIndex == 0 ? Boolean.class : Object.class;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            T item = filteredItems.get(rowIndex);
            int actualIndex = items.indexOf(item);

            switch (columnIndex) {
                case 0:
                    return selectionStates[actualIndex];
                case 1:
                    return item.getId();
                case 2:
                    return item.getDisplayName();
                case 3:
                    return item.getDescription();
                default:
                    return null;
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 0;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                T item = filteredItems.get(rowIndex);
                int actualIndex = items.indexOf(item);
                selectionStates[actualIndex] = (Boolean) aValue;
                fireTableCellUpdated(rowIndex, columnIndex);
            }
        }

        /**
         * Obtém o item na linha especificada.
         * @param row índice da linha
         * @return o item correspondente à linha
         */
        public T getItemAt(int row) {
            return items.get(row);
        }

        /**
         *  Obtém a lista de itens selecionados atualmente.
         * @return Lista de itens selecionados.
         */
        public List<T> getSelectedItems() {
            List<T> selected = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                if (selectionStates[i]) {
                    selected.add(items.get(i));
                }
            }
            return selected;
        }
    }
}
