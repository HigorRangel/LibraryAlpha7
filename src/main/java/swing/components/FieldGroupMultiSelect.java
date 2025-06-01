package swing.components;

import swing.configs.GeneralProperties;
import swing.interfaces.Identifiable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Campo de seleção múltipla que permite ao usuário selecionar vários itens de uma lista.
 */
public class FieldGroupMultiSelect<T extends Identifiable> extends JPanel {

    private final MultiSelectTableDialog<T> multiSelectDialog;
    boolean isRequired = false;
    private CustomTextField displayField;
    private List<T> selectedItems;
    private List<T> allItems;
    private Supplier<T> onNewItemClick;


    public FieldGroupMultiSelect(JDialog parent, List<T> items, Supplier<T> onNewItemClick) {
        this.allItems = new ArrayList<>(items);
        this.selectedItems = new ArrayList<>();
        this.multiSelectDialog = new MultiSelectTableDialog<>(parent, "Seleção Múltipla", allItems, onNewItemClick);
        this.onNewItemClick = onNewItemClick;
        initUI();
    }

    /**
     * Inicializa a ‘interface’ do usuário do campo de seleção múltipla.
     */
    private void initUI() {
        setLayout(new BorderLayout(5, 5));
        JPanel fieldPanel = new JPanel(new BorderLayout(5, 5));

        displayField = new CustomTextField();
        displayField.setEditable(false);
        displayField.setEnabled(false);
        displayField.setRequired(true);
        fieldPanel.add(displayField, BorderLayout.CENTER);

        CustomButton selectButton = new CustomButton(GeneralProperties.ICONS_ADD_PNG, true);
        selectButton.addActionListener(this::openMultiSelectDialog);
        selectButton.setPreferredSize(new Dimension(30, 20));
        fieldPanel.add(selectButton, BorderLayout.EAST);

        add(fieldPanel, BorderLayout.CENTER);
        updateDisplayField();
    }

    /**
     * Abre o diálogo de seleção múltipla quando o botão é clicado.
     *
     * @param e Evento de ação disparado pelo clique do botão.
     */
    private void openMultiSelectDialog(ActionEvent e) {
        multiSelectDialog.setSelectedItems(selectedItems);
        multiSelectDialog.setVisible(true);
        if (!multiSelectDialog.isCanceled()) {
            selectedItems = multiSelectDialog.getSelectedItems();
            updateDisplayField();
        }
    }

    /**
     * Atualiza o campo de exibição com os itens selecionados.
     */
    private void updateDisplayField() {
        StringBuilder sb = new StringBuilder();
        for (T item : selectedItems) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(item.getDisplayName());
        }
        displayField.setText(sb.toString());
    }

    /**
     * Obtém a lista de itens selecionados atualmente.
     *
     * @return Lista de itens selecionados.
     */
    public List<T> getSelectedItems() {
        return new ArrayList<>(selectedItems);
    }

    /**
     * Define os itens selecionados no campo de seleção múltipla.
     *
     * @param items Lista de itens a serem selecionados.
     */
    public void setSelectedItems(List<T> items) {
        this.selectedItems = new ArrayList<>(items);
        this.multiSelectDialog.setSelectedItems(items);
        updateDisplayField();
    }

    /**
     * Obtém os ‘IDs’ dos itens selecionados.
     *
     * @return Lista de IDs dos itens selecionados.
     */
    public List<Long> getSelectedIds() {
        List<Long> ids = new ArrayList<>();
        for (T item : selectedItems) {
            ids.add(item.getId());
        }
        return ids;
    }

    /**
     * Atualiza os itens selecionados com base numa lista de objetos brutos (por exemplo, Autor, Editora etc).
     * <p>
     * Os itens são buscados dentro da lista de todos os itens exibidos na tabela, garantindo que os checkboxes sejam marcados corretamente.
     *
     * @param rawItems Lista de objetos do tipo base (ex: Autor) que devem ser marcados como selecionados.
     */
    public void setSelectedItemsByRawItems(List<? extends Identifiable> rawItems) {
        if (rawItems == null) return;

        List<T> newSelectedItems = new ArrayList<>();

        for (Identifiable raw : rawItems) {
            allItems.stream()
                    .filter(selectable -> Objects.equals(selectable.getId(), raw.getId()))
                    .findFirst()
                    .ifPresent(newSelectedItems::add);
        }

        setSelectedItems(newSelectedItems);
    }

    /**
     * Obtém todos os itens disponíveis para seleção no diálogo de seleção múltipla.
     *
     * @return Lista de todos os itens que podem ser selecionados.
     */
    public List<T> getAllItems() {
        return allItems;
    }

    /**
     * Define todos os itens disponíveis para seleção no diálogo de seleção múltipla.
     *
     * @param allItems Lista de itens que serão exibidos no diálogo de seleção múltipla.
     */
    public void setAllItems(List<T> allItems) {
        this.allItems = allItems;
        this.multiSelectDialog.setAllItems(allItems);
    }

    /**
     * Verifica se o campo é obrigatório.
     *
     * @return true se o campo for obrigatório, false caso contrário.
     */
    public boolean isRequired() {
        return isRequired;
    }

    /**
     * * Define se o campo é obrigatório.
     *
     * @param required true para tornar o campo obrigatório, false caso contrário.
     */
    public void setRequired(boolean required) {
        isRequired = required;
        displayField.setRequired(required);
    }

    /**
     * Obtém o fornecedor que será chamado quando um novo ‘item’ for adicionado.
     *
     * @return Fornecedor que retorna um novo ‘item’ do tipo T.
     */
    public Supplier<T> getOnNewItemClick() {
        return onNewItemClick;
    }

    /**
     * Define o fornecedor que será chamado quando um novo ‘item’ for adicionado.
     *
     * @param onNewItemClick Fornecedor que retorna um novo ‘item’ do tipo T.
     */
    public void setOnNewItemClick(Supplier<T> onNewItemClick) {
        this.onNewItemClick = onNewItemClick;
    }
}
