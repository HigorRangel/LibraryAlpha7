package swing.interfaces;

import swing.configs.GeneralProperties;

import java.awt.*;

public interface ButtonProperties {
    // Cores principais
    Color PRIMARY_BACKGROUND = GeneralProperties.PRIMARY_COLOR;
    Color PRIMARY_HOVER = GeneralProperties.SECONDARY_COLOR;
    Color PRIMARY_PRESSED = new Color(0, 80, 170);

    // Cor do texto
    Color TEXT_COLOR = Color.WHITE;
}
