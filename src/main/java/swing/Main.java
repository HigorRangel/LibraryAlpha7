package swing;

import swing.configs.DatabaseInitializer;
import swing.view.MainView;

import javax.swing.*;

/**
 * Classe que representa a classe principal da aplicação.
 */
public class Main {

    public static void main(String[] args) {
        new DatabaseInitializer().init();
        SwingUtilities.invokeLater(MainView::new);
    }
}