package swing.model.form;

import javax.swing.*;

public abstract class FormFieldAbstractModel<T> {

    String label;
    String technicalName;
    boolean required;

    protected FormFieldAbstractModel(String label, String name, boolean required) {
        this.label = label;
        this.technicalName = name;
        this.required = required;
    }

    /**
     * Cria o componente do campo (JTextField, JTable, JCheckBox, etc)
     *
     * @return JComponent que representa o campo
     */
    protected abstract JComponent createComponent();

    /**
     * Obtém o valor do campo.
     *
     * @return Valor do campo, do tipo T.
     */
    public abstract T getValue();

    /**
     * Define o valor do campo.
     *
     * @param value Valor a ser definido no campo, do tipo T.
     */
    public abstract void setValue(T value);

    /**
     * Preenche as propriedades do campo com os valores fornecidos.
     *
     * @param properties Valores a serem preenchidos nas propriedades do campo.
     */
    protected abstract void fillProperties(Object... properties);
}
