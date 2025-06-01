package swing.components;

import swing.interfaces.Identifiable;
import swing.util.ComponentUtils;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * CustomTableModel é uma classe que estende AbstractTableModel e é usada para
 * exibir e manipular dados de objetos identificáveis numa tabela Swing.
 *
 * @param <T> o tipo de objeto que implementa a interface Identifiable.
 */
public class CustomTableModel<T extends Identifiable> extends AbstractTableModel {
    final Class<T> clazz;
    final boolean hasEditColumn;
    final boolean hasDeleteColumn;
    private final Map<String, String> columnNames;
    List<T> listObjects;

    public CustomTableModel(Class<T> clazz, List<T> listObjects) {
        this(clazz, listObjects, false, false);
    }

    public CustomTableModel(Class<T> clazz, List<T> listObjects, boolean hasEditColumn, boolean hasDeleteColumn) {
        this.clazz = clazz;
        this.listObjects = listObjects;
        this.hasEditColumn = hasEditColumn;
        this.hasDeleteColumn = hasDeleteColumn;
        this.columnNames = ComponentUtils.getColumnNames(clazz, hasDeleteColumn, hasEditColumn);
    }

    /**
     * Retorna o número de linhas na tabela.
     *
     * @return o número de linhas na tabela, que é o tamanho da lista de objetos.
     */
    @Override
    public int getRowCount() {
        return listObjects.size();
    }

    /**
     * Retorna o número de colunas na tabela.
     *
     * @return o número de colunas na tabela, que é o tamanho do mapa de nomes de colunas.
     */
    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    /**
     * Retorna o nome da coluna na posição especificada.
     *
     * @param column coluna índice da qual se deseja obter o nome.
     * @return o nome da coluna correspondente ao índice fornecido, ou null se o índice for inválido.
     */
    @Override
    public String getColumnName(int column) {
        if (column < 0 || column >= columnNames.size()) {
            return null;
        }
        return columnNames.values().toArray(new String[0])[column];
    }

    /**
     * Retorna o valor na célula especificada por linha e coluna.
     *
     * @param rowIndex    a linha cuja célula é a ser consultada
     * @param columnIndex a coluna cuja célula é a ser consultada
     * @return o valor na célula especificada, ou null se os índices forem inválidos.
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= listObjects.size()) {
            return null;
        }
        if (columnIndex < 0 || columnIndex >= columnNames.size()) {
            return null;
        }

        T object = listObjects.get(rowIndex);
        String columnKey = columnNames.keySet().toArray(new String[0])[columnIndex];
        return ComponentUtils.getValueByColumnName(object, columnKey);
    }

    /**
     * Retorna o objeto associado à linha da tabela.
     *
     * @param viewRowIndex índice da linha na visualização da tabela
     * @param table        a referência à tabela
     * @return o objeto associado à linha especificada, ou null se o índice for inválido
     */
    public T getObjectByRowNum(int viewRowIndex, JTable table) {
        int modelRowIndex = table.convertRowIndexToModel(viewRowIndex);
        if (modelRowIndex < 0 || modelRowIndex >= listObjects.size()) {
            return null;
        }
        return listObjects.get(modelRowIndex);
    }

    /**
     * Verifica se a célula na posição especificada é editável.
     *
     * @param rowIndex    a linha sendo consultada
     * @param columnIndex a coluna sendo consultada
     * @return true se a célula for editável, false caso contrário.
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (hasEditColumn && columnIndex == columnNames.size() - 2) {
            return true;
        }
        return hasDeleteColumn && columnIndex == columnNames.size() - 1;
    }

    /**
     * Verifica se o modelo de tabela possui uma coluna de edição.
     *
     * @return true se houver uma coluna de edição, false caso contrário.
     */
    public boolean hasEditColumn() {
        return hasEditColumn;
    }

    /**
     * Verifica se o modelo de tabela possui uma coluna de exclusão.
     *
     * @return true se houver uma coluna de exclusão, false caso contrário.
     */
    public boolean hasDeleteColumn() {
        return hasDeleteColumn;
    }

    /**
     * Define a lista de objetos que serão exibidos na tabela.
     *
     * @param listObjects a lista de objetos a serem exibidos
     */
    public void setListObjects(List<T> listObjects) {
        this.listObjects = new ArrayList<>(listObjects);
    }

    /**
     * Método auxiliar para filtro - obtém valor como String
     */
    public String getValueAsString(int row, int col) {
        Object value = getValueAt(row, col);
        return value != null ? value.toString().toLowerCase() : "";
    }
}
