package swing.util;

import javax.swing.*;
import java.awt.*;

public class SwingErrorHandler {

    public static void showError(Component parentComponent, String message, Exception e) {
        JOptionPane.showMessageDialog(
                parentComponent,
                message + "\nDetalhes: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void showInfo(Component parentComponent, String message) {
        JOptionPane.showMessageDialog(
                parentComponent,
                message,
                "Informação",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
