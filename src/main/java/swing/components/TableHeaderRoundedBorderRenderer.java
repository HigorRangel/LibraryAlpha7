package swing.components;

import sun.swing.table.DefaultTableCellHeaderRenderer;
import swing.configs.GeneralProperties;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

public class TableHeaderRoundedBorderRenderer extends DefaultTableCellHeaderRenderer {
    private static final int PADDING_TOP = 5;
    private static final int PADDING_LEFT = 10;
    private static final int PADDING_BOTTOM = 5;
    private static final int PADDING_RIGHT = 10;
    JTable table;
    private int columnIndexView = -1;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        this.columnIndexView = column;


        this.table = table;
        setOpaque(false);
        setHorizontalAlignment(SwingConstants.CENTER);
        setFont(new Font("Arial", Font.BOLD, 12));
        setForeground(Color.WHITE);

        setBorder(BorderFactory.createEmptyBorder(
                PADDING_TOP, PADDING_LEFT, PADDING_BOTTOM, PADDING_RIGHT));

        return this;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int radius = 0;

        g2.setColor(GeneralProperties.PRIMARY_COLOR);

        if (columnIndexView == 0) {
            Path2D.Float path = new Path2D.Float();
            path.moveTo(radius, 0);
            path.quadTo(0, 0, 0, radius);
            path.lineTo(0, height);
            path.lineTo(width, height);
            path.lineTo(width, 0);
            path.closePath();
            g2.fill(path);
        } else if (columnIndexView == table.getColumnModel().getColumnCount() - 1) {
            Path2D.Float path = new Path2D.Float();
            path.moveTo(0, 0);
            path.lineTo(width - radius, 0);
            path.quadTo(width, 0, width, radius);
            path.lineTo(width, height);
            path.lineTo(0, height);
            path.closePath();
            g2.fill(path);
        } else {
            g2.fillRect(0, 0, width, height);
        }

        g2.setColor(GeneralProperties.PRIMARY_COLOR);
        g2.drawLine(0, height - 1, width, height - 1);

        super.paintComponent(g2);
        g2.dispose();
    }
}