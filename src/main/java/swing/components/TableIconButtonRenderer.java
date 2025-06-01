package swing.components;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * Um renderer de botão com ícone para células de tabela.
 * Este componente exibe um ícone que muda de aparência quando o mouse passa sobre ele.
 */
public class TableIconButtonRenderer extends JButton implements TableCellRenderer {
    private final ImageIcon normalIcon;
    private final ImageIcon hoverIcon;

    public TableIconButtonRenderer(String iconPath) {
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setHorizontalAlignment(SwingConstants.CENTER);

        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        this.normalIcon = createIcon(iconPath, 1.0f);
        this.hoverIcon = createIcon(iconPath, 0.7f);

        setIcon(normalIcon);
    }


    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        setIcon(table instanceof CustomTable &&
                ((CustomTable<?>) table).getHoveredRow() == row &&
                ((CustomTable<?>) table).getHoveredCol() == column ?
                hoverIcon : normalIcon);
        return this;
    }

    /**
     * Cria um ícone a partir de um caminho de imagem e aplica uma opacidade.
     *
     * @param path    o caminho da imagem do ícone
     * @param opacity o nível de opacidade do ícone (0.0f a 1.0f)
     * @return um ImageIcon com a opacidade aplicada
     */
    private ImageIcon createIcon(String path, float opacity) {
        try {
            BufferedImage img = ImageIO.read(Objects.requireNonNull(getClass().getResource(path)));
            BufferedImage resized = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = resized.createGraphics();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
            g2.drawImage(img.getScaledInstance(16, 16, Image.SCALE_SMOOTH), 0, 0, null);
            g2.dispose();
            return new ImageIcon(resized);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();

        Icon icon = getIcon();
        if (icon != null) {
            int x = (getWidth() - icon.getIconWidth()) / 2;
            int y = (getHeight() - icon.getIconHeight()) / 2;
            icon.paintIcon(this, g, x, y);
        }
    }

}