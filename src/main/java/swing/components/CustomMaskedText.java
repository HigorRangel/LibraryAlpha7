package swing.components;

import swing.enums.InputType;
import swing.interfaces.TextFieldProperties;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DateFormatter;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.util.Date;

/**
 * Classe que representa um campo de texto com máscara personalizada.
 * Esta classe estende JFormattedTextField e implementa a interface FieldProperties que contém
 * métodos para definir propriedades de campo.
 * A máscara é definida no construtor e o campo de texto é inicializado com propriedades
 * visuais personalizadas.
 * A classe também implementa o método paintComponent para desenhar uma borda arredondada
 * e o método paintBorder para desenhar a borda do campo de texto.
 */
public class CustomMaskedText extends JFormattedTextField implements TextFieldProperties {
    Color defaultBorderColor = DEFAULT_BORDER_NORMAL_COLOR;
    boolean isRequired = false;
    private boolean isTouched = false;
    private int maxLength = -1;
    private int minLength = -1;
    private InputType inputType = InputType.ANY;

    public CustomMaskedText(String mask) {
        this(createFormat(mask), null);
    }

    public CustomMaskedText(String mask, String placeholder) {
        this(createFormat(mask), placeholder);
    }

    public CustomMaskedText(MaskFormatter mask, String placeholder) {
        this((AbstractFormatter) mask, placeholder);

    }

    public CustomMaskedText(MaskFormatter mask) {
        this(mask, null);
    }

    public CustomMaskedText(AbstractFormatter mask, String placeholder) {
        super(mask);
        initialize(this);
        setFocusLostBehavior(JFormattedTextField.PERSIST);
        if (placeholder != null && !placeholder.isEmpty() && mask instanceof MaskFormatter) {
            ((MaskFormatter) mask).setPlaceholderCharacter('_');
            ((MaskFormatter) mask).setValueContainsLiteralCharacters(false);
            ((MaskFormatter) mask).setPlaceholder(placeholder);
        }
        setFormatter(mask);


        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                SwingUtilities.invokeLater(() -> {
                    int pos = getNextEditablePosition(0);
                    if (pos >= 0) {
                        setCaretPosition(pos);
                    }
                });
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                SwingUtilities.invokeLater(() -> {
                    int pos = getNextEditablePosition(0);
                    if (pos >= 0) {
                        setCaretPosition(pos);
                    }
                });
                e.consume();
            }
        });
        setColumns(10);
    }

    public CustomMaskedText(AbstractFormatter mask) {
        this(mask, null);
    }

    /**
     * Cria uma máscara para o campo de texto.
     *
     * @param mask máscara a ser aplicada
     * @return o campo de texto formatado
     */
    private static MaskFormatter createFormat(String mask) {
        try {
            MaskFormatter mascara = new MaskFormatter(mask);
            mascara.setPlaceholderCharacter('_');
            mascara.setValueContainsLiteralCharacters(false);
            return mascara;
        } catch (ParseException e) {
            throw new RuntimeException("Máscara inválida!", e);
        }
    }

    /**
     * Método que inicializa o campo de texto.
     *
     * @param start posição inicial
     * @return a próxima posição editável
     */
    private int getNextEditablePosition(int start) {
        AbstractDocument doc = (AbstractDocument) getDocument();
        for (int i = start; i < getText().length(); i++) {
            if (doc.getDocumentFilter() == null || isEditablePosition(i)) {
                return i;
            }
        }
        return start;
    }

    /**
     * Verifica se a posição é editável.
     *
     * @param pos posição a ser verificada
     * @return true se a posição for editável, false caso contrário
     */
    private boolean isEditablePosition(int pos) {
        try {
            return getText().charAt(pos) == '_';
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Define o valor da message de error a ser exibida abaixo do campo de entrada.
     *
     * @param message A message de error a ser exibida.
     */
    @Override
    public void setErrorMessage(String message) {
    }

    /**
     * Obtem o valor do campo de texto.
     *
     * @return o valor do campo de texto.
     */
    @Override
    public Object getFieldValue() throws ParseException {
        this.commitEdit();

        Object value = this.getValue();
        if (value == null) {
            return null;
        } else if (value instanceof Date) {
            DateFormatter dateFormatter = (DateFormatter) this.getFormatter();
            return dateFormatter.valueToString(value);
        } else if (value instanceof String) {
            String strObject = (String) this.getValue();

            if (strObject == null || strObject.isEmpty()) {
                return null;
            }
            return strObject.trim();
        }
        return value;
    }

    /**
     * Define a cor padrão da borda do campo de texto.
     *
     * @param defaultBorderHoverColor a cor padrão da borda.
     */
    @Override
    public void setDefaultBorderColor(Color defaultBorderHoverColor) {
        this.defaultBorderColor = defaultBorderHoverColor;
    }

    /**
     * Verifica se o campo é obrigatório.
     *
     * @return true se o campo for obrigatório, false caso contrário.
     */
    @Override
    public boolean isRequired() {
        return isRequired;
    }

    /**
     * Define se o campo é obrigatório ou não.
     *
     * @param isRequired true se o campo for obrigatório, false caso contrário.
     */
    @Override
    public void setRequired(boolean isRequired) {
        this.isRequired = isRequired;
    }

    /**
     * Retorna o tamanho máximo do campo de texto.
     *
     * @return tamanho máximo do campo de texto
     */
    @Override
    public int getMaxLength() {
        return this.maxLength;
    }

    /**
     * Define o tamanho máximo do campo de texto.
     *
     * @param maxLength o tamanho máximo do campo de texto.
     */
    @Override
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    /**
     * Retorna o tamanho mínimo do campo de texto.
     *
     * @return tamanho mínimo do campo de texto
     */
    @Override
    public int getMinLength() {
        return this.minLength;
    }

    /**
     * Retorna o tamanho mínimo do campo de texto
     *
     * @param minLength o tamanho mínimo do campo de texto.
     */
    @Override
    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    /**
     * Verifica se o campo foi tocado (ou seja, se o usuário interagiu com ele).
     *
     * @return <code>true</code> se o campo foi tocado, <code>false</code> caso contrário.
     */
    @Override
    public boolean isTouched() {
        return isTouched;
    }

    /**
     * Define se o campo foi tocado (ou seja, se o usuário interagiu com ele).
     *
     * @param isTouched <code>true</code> se o campo foi tocado, <code>false</code> caso contrário.
     */
    @Override
    public void setIsTouched(boolean isTouched) {
        this.isTouched = isTouched;
    }


    /**
     * Define borda arredondada para o campo de texto.
     *
     * @param g o contexto <code>Graphics</code> no qual pintar
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(getBackground() != null ? getBackground() : DEFAULT_BACKGROUND_COLOR);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), DEFAULT_BORDER_RADIUS, DEFAULT_BORDER_RADIUS);
        super.paintComponent(g);
        g2.dispose();
    }

    /**
     * Define borda arredondada para o campo de texto.
     *
     * @param g o contexto <code>Graphics</code> no qual pintar
     */
    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(defaultBorderColor);
        g2.setStroke(new BasicStroke(DEFAULT_BORDER_THICKNESS));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, DEFAULT_BORDER_RADIUS, DEFAULT_BORDER_RADIUS);
        g2.dispose();
    }

    /**
     * Obtém o tipo de entrada do campo de texto.
     *
     * @return o tipo de entrada do campo de texto, que pode ser NUMERIC, ALPHA, ALPHANUMERIC, CUSTOM ou ANY.
     */
    @Override
    public InputType getInputType() {
        return this.inputType;
    }

    /**
     * Define o tipo de entrada do campo de texto.
     *
     * @param inputType o tipo de entrada a ser definido, que pode ser NUMERIC, ALPHA, ALPHANUMERIC, CUSTOM ou ANY.
     */
    @Override
    public void setInputType(InputType inputType) {
        this.inputType = inputType;
    }
}
