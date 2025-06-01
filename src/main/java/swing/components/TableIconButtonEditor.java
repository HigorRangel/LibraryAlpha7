package swing.components;

import swing.configs.GeneralProperties;
import swing.interfaces.Identifiable;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;


/**
 * TableIconButtonEditor é um editor de célula personalizado para JTable que exibe um botão com ícone.
 *
 * @param <T> o tipo de objeto que o editor manipula, deve implementar a interface Identifiable.
 */
public class TableIconButtonEditor<T extends Identifiable> extends DefaultCellEditor {
    private final String iconPath;
    private final JPanel panel;
    private T currentObject;


    public TableIconButtonEditor(String iconPath, Consumer<T> action) {
        super(new JCheckBox());
        this.iconPath = iconPath;
        panel = new JPanel(new BorderLayout());
        panel.setOpaque(true);

        JButton button = new JButton(createIcon(1.0f)) {
            @Override
            protected void paintComponent(Graphics g) {
                getIcon().paintIcon(this, g,
                        (getWidth() - getIcon().getIconWidth()) / 2,
                        (getHeight() - getIcon().getIconHeight()) / 2);
            }
        };

        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFocusable(false);

        button.addActionListener(e -> {
            if (action != null && currentObject != null) {
                action.accept(currentObject);
            }
            stopCellEditing();
        });

        panel.add(button, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createEmptyBorder());
    }

    /**
     * Cria um ícone a partir do caminho especificado, aplicando uma opacidade.
     *
     * @param opacity a opacidade do ícone, onde 1.0 é totalmente opaco e 0.0 é totalmente transparente.
     * @return um ImageIcon com a opacidade aplicada, ou null se ocorrer um erro ao carregar a imagem.
     */
    private ImageIcon createIcon(float opacity) {
        try {
            BufferedImage img = ImageIO.read(Objects.requireNonNull(getClass().getResource(iconPath)));
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

    @SuppressWarnings("unchecked")
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        panel.setBackground(GeneralProperties.TABLE_SELECTION_COLOR);

        currentObject = ((CustomTableModel<T>) table.getModel()).getObjectByRowNum(row, table);

        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }

}