package swing.components;

import swing.configs.GeneralProperties;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

public class RoundedButtonUI extends BasicButtonUI {

    @Override
    public void paint(Graphics g, JComponent c) {
        AbstractButton b = (AbstractButton) c;
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (b.getModel().isRollover()) {
            g2.setColor(new Color(200, 200, 255));
        } else {
            g2.setColor(new Color(230, 230, 230));
        }
        g2.fillRoundRect(0, 0, b.getWidth(), b.getHeight(), GeneralProperties.SECONDARY_RADIUS, GeneralProperties.SECONDARY_RADIUS);

        g2.setColor(Color.LIGHT_GRAY);
        g2.setStroke(new BasicStroke(1));
        g2.drawRoundRect(0, 0, b.getWidth() - 1, b.getHeight() - 1, GeneralProperties.SECONDARY_RADIUS, GeneralProperties.SECONDARY_RADIUS);

        super.paint(g2, b);
        g2.dispose();
    }
}
