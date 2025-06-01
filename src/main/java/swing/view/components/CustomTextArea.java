package swing.view.components;

import javax.swing.*;
import java.awt.*;

/**
 * JTextArea customizado com suporte a quebra de linha,
 * linha de estilo, fonte e borda.
 */
public class CustomTextArea extends JTextArea {

    public CustomTextArea() {
        super();
        setLineWrap(true);
        setWrapStyleWord(true);
        setFont(new Font("Arial", Font.PLAIN, 14));
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);
    }

    public CustomTextArea(String text) {
        this();
        setText(text);
    }
}
