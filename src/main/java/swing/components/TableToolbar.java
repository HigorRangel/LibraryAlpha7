package swing.components;

import swing.configs.GeneralProperties;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

public class TableToolbar extends JToolBar {

    public TableToolbar() {
        setBorderPainted(false);
        setBorder(null);
        setFloatable(false);
        super.setBackground(Color.GREEN);
        setBackground(Color.green);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        g2.setColor(GeneralProperties.BACKGROUND_COLOR);

        Path2D.Float path = new Path2D.Float();
        path.moveTo(0, h);
        path.lineTo(0, GeneralProperties.PRIMARY_RADIUS);
        path.quadTo(0, 0, GeneralProperties.PRIMARY_RADIUS, 0);
        path.lineTo(w - GeneralProperties.PRIMARY_RADIUS, 0);
        path.quadTo(w, 0, w, GeneralProperties.PRIMARY_RADIUS);
        path.lineTo(w, h);
        path.closePath();

        g2.fill(path);

        g2.setColor(GeneralProperties.TRANSPARENT_COLOR);
        g2.draw(path);

        g2.dispose();
    }
}
