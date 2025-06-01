package swing.components;

import swing.interfaces.ButtonProperties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;

/**
 * CustomButton é uma classe que estende JButton e implementa propriedades personalizadas para botões.
 */
public class CustomButton extends JButton implements ButtonProperties {

    /**
     * Construtor vazio.
     */
    public CustomButton() {
        this("", (Icon) null);
    }

    /**
     * Construtor com texto.
     *
     * @param text o texto do botão
     */
    public CustomButton(String text) {
        this(text, (Icon) null);
    }

    /**
     * Construtor com ícone.
     *
     * @param icon o ícone do botão, que pode ser um ImageIcon ou outro tipo de ícone
     */
    public CustomButton(Icon icon) {
        this("", icon);
    }

    /**
     * Construtor com texto e ícone.
     *
     * @param text o texto do botão
     * @param icon o ícone do botão, que pode ser um ImageIcon ou outro tipo de ícone
     */
    public CustomButton(String text, Icon icon) {
        super(text, icon);
        setupButton();
        setButtonHeight(30);
    }

    /**
     * Construtor com texto e caminho do ícone.
     *
     * @param text     o texto do botão
     * @param iconPath o caminho do ícone, que pode ser um arquivo local ou um recurso no classpath
     */
    public CustomButton(String text, String iconPath) {
        this(text, createIconFromPath(iconPath, 16, 16));
    }

    /**
     * Construtor com caminho do ícone (‘String’ path) e booleano para indicar se é um caminho de ícone.
     * Este construtor é útil quando você deseja criar um botão com um ícone
     *
     * @param iconPath   o caminho do ícone, que pode ser um arquivo local ou um recurso no classpath
     * @param isIconPath indica se o caminho fornecido é um caminho de ícone
     */
    public CustomButton(String iconPath, boolean isIconPath) {
        this("", createIconFromPath(iconPath, 16, 16));
    }

    /**
     * Cria um ícone a partir de um caminho de arquivo ou URL.
     *
     * @param path   o caminho do arquivo ou URL do ícone
     * @param width  a largura desejada do ícone
     * @param height a altura desejada do ícone
     * @return um ImageIcon redimensionado, ou null se o caminho for inválido
     */
    private static ImageIcon createIconFromPath(String path, int width, int height) {
        if (path == null || path.isEmpty()) {
            return null;
        }
        try {
            ImageIcon icon;
            URL imgUrl = CustomButton.class.getResource(path);
            if (imgUrl != null) {
                icon = new ImageIcon(imgUrl);
            } else {
                icon = new ImageIcon(path);
            }

            if (width > 0 || height > 0) {
                if (width <= 0) {
                    width = height * icon.getIconWidth() / icon.getIconHeight();
                } else if (height <= 0) {
                    height = width * icon.getIconHeight() / icon.getIconWidth();
                }
                Image image = icon.getImage().getScaledInstance(
                        width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(image);
            }
            return icon;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Configura as propriedades do botão, como cor de fundo, cor do texto,
     */
    private void setupButton() {
        setBackground(ButtonProperties.PRIMARY_BACKGROUND);
        setForeground(TEXT_COLOR);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setOpaque(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                setBackground(ButtonProperties.PRIMARY_HOVER);
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                setBackground(ButtonProperties.PRIMARY_BACKGROUND);
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent evt) {
                setBackground(ButtonProperties.PRIMARY_PRESSED);
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent evt) {
                setBackground(ButtonProperties.PRIMARY_HOVER);
                repaint();
            }
        });
    }

    /**
     * Paints the button with rounded corners and centers the icon or text.
     *
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (getModel().isPressed()) {
            g2.setColor(PRIMARY_PRESSED);
        } else if (getModel().isRollover()) {
            g2.setColor(PRIMARY_HOVER);
        } else {
            g2.setColor(getBackground());
        }
        int cornerRadius = 12;
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));

        if (getIcon() != null) {
            Icon icon = getIcon();
            int iconX = (getWidth() - icon.getIconWidth()) / 2;
            int iconY = (getHeight() - icon.getIconHeight()) / 2;
            icon.paintIcon(this, g2, iconX, iconY);
        } else if (getText() != null && !getText().isEmpty()) {
            FontMetrics fm = g2.getFontMetrics();
            Rectangle textBounds = fm.getStringBounds(getText(), g2).getBounds();
            int textX = (getWidth() - textBounds.width) / 2;
            int textY = (getHeight() - textBounds.height) / 2 + fm.getAscent();

            g2.setColor(getForeground());
            g2.drawString(getText(), textX, textY);
        }

        g2.dispose();
    }

    /**
     * Define o ícone do botão.
     *
     * @param icon the icon used as the default image
     */
    @Override
    public void setIcon(Icon icon) {
        super.setIcon(icon);
        if (icon != null) {
            setContentAreaFilled(false);
        }
    }

    /**
     * Define a altura do botão.
     *
     * @param height A altura desejada do botão.
     */
    public void setButtonHeight(int height) {
        int width = getWidth() > 0 ? getWidth() : getPreferredSize().width;
        setPreferredSize(new Dimension(width, height));
        setMinimumSize(new Dimension(width, height));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
    }
}
