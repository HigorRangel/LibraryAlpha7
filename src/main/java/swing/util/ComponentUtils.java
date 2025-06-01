package swing.util;

import swing.annotations.DisplayableName;
import swing.components.CustomTable;
import swing.components.CustomTextField;
import swing.interfaces.Identifiable;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Classe utilitária para manipulação de componentes Swing,
 */
public class ComponentUtils {

    private ComponentUtils() {
    }

    /**
     * Método para adicionar o listener ao campo de busca. Quando digitado,
     * realiza a filtragem dos livros na table.
     *
     * @param fieldSearch Campo de busca
     */
    public static <T extends Identifiable> void addListenerSearchFieldTable(
            CustomTextField fieldSearch, CustomTable<T> table) {

        fieldSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filter();
            }

            private void filter() {
                String text = fieldSearch.getText();
                table.filterTable(text);
            }
        });
    }


    /**
     * Obtem os nomes das colunas de uma classe,
     *
     * @param clazz           a classe a ser inspecionada
     * @param hasDeleteColumn indica se deve incluir a coluna de exclusão
     * @param hasEditColumn   indica se deve incluir a coluna de edição
     * @return um mapa onde a chave é o nome do campo e o valor é o nome exibido
     */
    public static Map<String, String> getColumnNames(Class<?> clazz, boolean hasDeleteColumn, boolean hasEditColumn) {
        Map<String, String> columnNames = new LinkedHashMap<>();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            DisplayableName displayName = field.getAnnotation(DisplayableName.class);
            if (displayName == null || displayName.value().isEmpty()) {
                continue;
            }

            columnNames.put(field.getName(), displayName.value());
        }

        if (hasEditColumn) {
            columnNames.put("edit", " ");
        }

        if (hasDeleteColumn) {
            columnNames.put("delete", " ");
        }
        return columnNames;
    }

    /**
     * Obtém o valor de um campo de um objeto com base no nome da coluna.
     *
     * @param object     o objeto do qual se deseja obter o valor
     * @param columnName o nome da coluna (ou campo) do qual se deseja obter o valor
     * @param <T>        o tipo do objeto, que deve implementar a interface Identifiable
     * @return o valor do campo correspondente ao nome da coluna
     */
    public static <T extends Identifiable> Object getValueByColumnName(T object, String columnName) {
        try {
            Field field = object.getClass().getDeclaredField(columnName);
            field.setAccessible(true);
            return field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }
}
