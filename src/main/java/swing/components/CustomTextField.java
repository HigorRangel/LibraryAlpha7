package swing.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomTextField extends JTextField {

    Color corNormal = Color.GRAY;
    Color corHover = new Color(100, 149, 237); // Azul suave
    Color corFoco = new Color(255, 0, 0);   // Azul mais forte
    Color corAtual = corNormal;

    public CustomTextField() {
        super();
        setOpaque(false);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 0, 0), 1),
                BorderFactory.createEmptyBorder(3, 3, 3, 3)));
        setBackground(new Color(255, 255, 255));
        setForeground(new Color(0, 0, 0));
        setPreferredSize(new Dimension(0, 28));

        // Hover
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                corAtual = corHover;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                corAtual = hasFocus() ? corFoco : corNormal;
                repaint();
            }
        });

        // Foco
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                corAtual = corFoco;
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                corAtual = corNormal;
                repaint();
            }
        });

    }

    @Override
    public void setText(String text) {
        super.setText(text);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(getBackground() != null ? getBackground() : Color.WHITE);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        super.paintComponent(g);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(corAtual);
        g2.setStroke(new BasicStroke(1));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
        g2.dispose();
    }
}
