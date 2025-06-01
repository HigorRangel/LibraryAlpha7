package swing.util;

import javax.swing.*;
import java.awt.*;

/**
 * Classe utilitária para exibir mensagens ao usuário. <br>
 * Fornece métodos para mostrar mensagens de error, warning, informação e confirmação.
 */
public class MessageUtils {

    private MessageUtils() {
    }

    /**
     * Exibe uma mensagem de error ao usuário.
     *
     * @param parent   Componente pai para a caixa de diálogo
     * @param mensagem Mensagem a ser exibida
     */
    public static void error(Component parent, String mensagem) {
        JOptionPane.showMessageDialog(parent, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Exibe uma mensagem de warning ao usuário.
     *
     * @param parent   Componente pai para a caixa de diálogo
     * @param mensagem Mensagem a ser exibida
     */
    public static void warning(Component parent, String mensagem) {
        JOptionPane.showMessageDialog(parent, mensagem, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Exibe uma mensagem de informação ao usuário.
     *
     * @param parent   Componente pai para a caixa de diálogo
     * @param mensagem Mensagem a ser exibida
     */
    public static void info(Component parent, String mensagem) {
        JOptionPane.showMessageDialog(parent, mensagem, "Informação", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Exibe uma caixa de diálogo de confirmação ao usuário.
     *
     * @param parent   Componente pai para a caixa de diálogo
     * @param mensagem Mensagem a ser exibida
     * @return true se o usuário confirm, false caso contrário
     */
    public static boolean confirm(Component parent, String mensagem) {
        int resultado = JOptionPane.showConfirmDialog(parent, mensagem, "Confirmação", JOptionPane.YES_NO_OPTION);
        return resultado == JOptionPane.YES_OPTION;
    }

}
