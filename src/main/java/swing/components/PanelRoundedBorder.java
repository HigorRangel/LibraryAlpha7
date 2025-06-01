package swing.components;

import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.Path2D;

/**
 * Uma borda com cantos arredondados para painéis.
 * Esta classe estende AbstractBorder e implementa o método paintBorder
 * para desenhar uma borda arredondada personalizada.
 */
public class PanelRoundedBorder implements Border {
    float thickness;
    Color color;
    float arc;
    boolean topLeft, topRight, bottomLeft, bottomRight;
    Insets padding;

    public PanelRoundedBorder(float thickness, float arc, Color color,
                              boolean topLeft, boolean topRight, boolean bottomLeft, boolean bottomRight,
                              Insets padding) {
        this.thickness = thickness;
        this.arc = arc;
        this.color = color;
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;
        this.padding = padding;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        float offset = thickness / 2f;

        int innerX = x + padding.left;
        int innerY = y + padding.top;
        int innerWidth = width - padding.left - padding.right;
        int innerHeight = height - padding.top - padding.bottom;

        Path2D.Float path = new Path2D.Float();

        if (topLeft) {
            path.moveTo(innerX + offset + arc, innerY + offset);
        } else {
            path.moveTo(innerX + offset, innerY + offset);
        }

        // Topo
        if (topRight) {
            path.lineTo(innerX + innerWidth - offset - arc, innerY + offset);
            path.quadTo(innerX + innerWidth - offset, innerY + offset,
                    innerX + innerWidth - offset, innerY + offset + arc);
        } else {
            path.lineTo(innerX + innerWidth - offset, innerY + offset);
        }

        // Direita
        if (bottomRight) {
            path.lineTo(innerX + innerWidth - offset, innerY + innerHeight - offset - arc);
            path.quadTo(innerX + innerWidth - offset, innerY + innerHeight - offset,
                    innerX + innerWidth - offset - arc, innerY + innerHeight - offset);
        } else {
            path.lineTo(innerX + innerWidth - offset, innerY + innerHeight - offset);
        }

        // Inferior
        if (bottomLeft) {
            path.lineTo(innerX + offset + arc, innerY + innerHeight - offset);
            path.quadTo(innerX + offset, innerY + innerHeight - offset,
                    innerX + offset, innerY + innerHeight - offset - arc);
        } else {
            path.lineTo(innerX + offset, innerY + innerHeight - offset);
        }

        // Esquerda
        if (topLeft) {
            path.lineTo(innerX + offset, innerY + offset + arc);
            path.quadTo(innerX + offset, innerY + offset, innerX + offset + arc, innerY + offset);
        } else {
            path.lineTo(innerX + offset, innerY + offset);
        }

        g2.setColor(color);
        g2.setStroke(new BasicStroke(thickness));
        g2.draw(path);
        g2.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        int pad = Math.round(thickness);
        return new Insets(
                pad + padding.top,
                pad + padding.left,
                pad + padding.bottom,
                pad + padding.right
        );
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }
}