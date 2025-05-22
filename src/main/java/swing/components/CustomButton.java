package swing.components;

import javax.swing.*;

public class CustomButton extends JButton {

    public CustomButton(String text) {
        super(text);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(true);
    }

    public CustomButton() {
        this("");
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        if (getModel().isPressed()) {
            g.setColor(getBackground().darker());
        } else {
            g.setColor(getBackground());
        }
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }
}
