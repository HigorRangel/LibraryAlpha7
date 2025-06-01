package swing.interfaces;

import swing.configs.GeneralProperties;

import java.awt.*;

public interface ButtonProperties {
    // Cores principais
    Color PRIMARY_BACKGROUND = GeneralProperties.PRIMARY_COLOR;
    Color PRIMARY_HOVER = new Color(0, 100, 190);
    Color PRIMARY_PRESSED = new Color(0, 80, 170);

    // Cor do texto
    Color TEXT_COLOR = Color.WHITE;
}
