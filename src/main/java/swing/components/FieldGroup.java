package swing.components;

import javax.swing.*;
import java.awt.*;

/**
 * Um componente de ‘interface’ gráfica que agrupa um campo de entrada (como JTextField, JComboBox, etc.)
 * com um rótulo e uma mensagem de erro.
 * Este componente serve para exibir campos de entrada com rótulos e mensagens de erro
 *
 * @param <T>
 */
public class FieldGroup<T extends JComponent> extends JPanel {

    final T component;
    final JLabel errorMessage;

    public FieldGroup(T component, String rotulo) {
        this.component = component;
        this.errorMessage = new JLabel();
        this.errorMessage.setForeground(Color.RED);
        this.errorMessage.setText(" ");

        this.errorMessage.setFont(new Font("Arial", Font.BOLD, 10));

        setLayout(new BorderLayout(0, 0));
        setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        JLabel label = new JLabel(rotulo);
        label.setLabelFor(component);
        add(label, BorderLayout.NORTH);
        add(component, BorderLayout.CENTER);
        add(errorMessage, BorderLayout.SOUTH);
    }


    /**
     * Exibe uma mensagem de error abaixo do campo de entrada.
     *
     * @param mensagem A mensagem de error a ser exibida.
     */
    public void showError(String mensagem) {
        errorMessage.setText(mensagem);
    }

    /**
     * Limpa a mensagem de error exibida abaixo do campo de entrada.
     */
    public void cleanError() {
        errorMessage.setText(" ");
    }

}
