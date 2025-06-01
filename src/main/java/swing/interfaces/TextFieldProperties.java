package swing.interfaces;

import swing.components.FieldGroup;
import swing.enums.InputType;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;

public interface TextFieldProperties {

    /**
     * Forma da borda padrão para os campos de texto com as seguintes características: <br>
     * <strong>Cor:</strong> Preto <br>
     * <strong>Espessura:</strong> 1 <br>
     * <strong>Espaçamento:</strong> 3 (superior, inferior, esquerdo e direito)
     */
    Border DEFAULT_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 0, 0), 1),
            BorderFactory.createEmptyBorder(3, 3, 3, 3));

    /**
     * Cor padrão de fundo para os campos de texto. Padrão: <br>
     * <strong>Cor:</strong> Branco <br>
     */
    Color DEFAULT_BACKGROUND_COLOR = new Color(255, 255, 255);

    /**
     * Cor padrão de fundo para os campos de texto quando desabilitados. Padrão: <br>
     * <strong>Cor:</strong> Cinza-claro <br>
     */
    Color DEFAULT_BACKGROUND_COLOR_DISABLED = new Color(230, 230, 230);

    /**
     * Cor padrão da borda de error. Padrão: <br>
     * <strong>Cor:</strong> Vermelho <br>
     */
    Color DEFAULT_BORDER_ERROR_COLOR = new Color(255, 0, 0);

    /**
     * Cor padrão da borda de error. Padrão: <br>
     * <strong>Cor:</strong> Vermelho-escuro <br>
     */
    Color DEFAULT_BORDER_FOCUS_ERROR_COLOR = new Color(219, 16, 2);

    /**
     * Cor padrão da fonte do texto nos campos de texto. Padrão: <br>
     * <strong>Cor:</strong> Preto <br>
     */
    Color DEFAULT_TEXT_COLOR = new Color(0, 0, 0);

    /**
     * Tamanho padrão preferido para os campos de texto. Padrão: <br>
     * <strong>Largura:</strong> 0 <br>
     * <strong>Altura:</strong> 28 <br>
     */
    Dimension DEFAULT_PREFERRED_SIZE = new Dimension(0, 28);


    /**
     * Altura padrão para os campos de texto. Padrão: 30 <br>
     */
    int DEFAULT_HEIGHT = 30;

    /**
     * Cor padrão da borda normal. Padrão: <br>
     * <strong>Cor:</strong> Cinza <br>
     */
    Color DEFAULT_BORDER_NORMAL_COLOR = Color.GRAY;

    /**
     * Cor padrão da borda ao passar o mouse. Padrão: <br>
     * <strong>Cor:</strong> Azul claro <br>
     */
    Color DEFAULT_BORDER_HOVER_COLOR = new Color(100, 149, 237);

    /**
     * Cor padrão da borda ao focar. Padrão: <br>
     * <strong>Cor:</strong> Vermelho <br>
     */
    Color DEFAULT_BORDER_FOCUS_COLOR = new Color(163, 109, 214);

    /**
     * Cor padrão da borda de error. Padrão: <br>
     * <strong>Cor:</strong> Vermelho-escuro <br>
     */
    Color DEFAULT_BORDER_HOVER_ERROR_COLOR = new Color(219, 16, 2);

    /**
     * Raio padrão da borda arredondada. Padrão: <br>
     * <strong>Raio:</strong> 10 <br>
     */
    int DEFAULT_BORDER_RADIUS = 10;

    /**
     * Espessura padrão da borda. Padrão: <br>
     * <strong>Espessura:</strong> 1 <br>
     */
    int DEFAULT_BORDER_THICKNESS = 1;


    /**
     * Método para definir o campo de texto com as propriedades padrão. Defines as seguintes propriedades: <br>
     * <strong>Opaco (opaque):</strong> <code>false</code> <br>
     * <strong>Borda (border):</strong> <code>DEFAULT_BORDER</code> <br>
     * <strong>Cor de fundo (background):</strong> <code>DEFAULT_BACKGROUND_COLOR</code> <br>
     * <strong>Cor do texto (foreground):</strong> <code>DEFAULT_TEXT_COLOR</code> <br>
     * <strong>Tamanho preferido (preferredSize):</strong> <code>DEFAULT_PREFERRED_SIZE</code> <br>
     * <strong>Cor do hover (hover):</strong> <code>DEFAULT_BORDER_HOVER_COLOR</code> <br>
     * <strong>Cor do foco (focus):</strong> <code>DEFAULT_BORDER_FOCUS_COLOR</code> <br>
     *
     * @param field Componente a ser inicializado
     */
    default void initialize(JTextField field) {
        field.setOpaque(false);
        field.setBorder(DEFAULT_BORDER);
        field.setBackground(DEFAULT_BACKGROUND_COLOR);
        field.setForeground(DEFAULT_TEXT_COLOR);
        field.setPreferredSize(DEFAULT_PREFERRED_SIZE);
        setButtonHeight(DEFAULT_HEIGHT);

        // Define cor do Hover
        field.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boolean isCampoValido = validacoesIniciais();
                boolean isTouched = isTouched();
                if (!isCampoValido && isTouched) {
                    setDefaultBorderColor(DEFAULT_BORDER_HOVER_ERROR_COLOR);
                } else {
                    setDefaultBorderColor(DEFAULT_BORDER_HOVER_COLOR);
                }


                field.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boolean isCampoValido = validacoesIniciais();
                boolean isTouched = isTouched();
                if (!isCampoValido && isTouched) {
                    setDefaultBorderColor(hasFocus() ? DEFAULT_BORDER_FOCUS_ERROR_COLOR : DEFAULT_BORDER_ERROR_COLOR);
                } else {
                    setDefaultBorderColor(hasFocus() ? DEFAULT_BORDER_FOCUS_COLOR : DEFAULT_BORDER_NORMAL_COLOR);
                }
                field.repaint();
            }
        });

        field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                boolean isCampoValido = validacoesIniciais();
                if (!isCampoValido && isTouched()) {
                    setDefaultBorderColor(DEFAULT_BORDER_FOCUS_ERROR_COLOR);
                } else {
                    setDefaultBorderColor(DEFAULT_BORDER_FOCUS_COLOR);
                }
                field.repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                setIsTouched(true);

                boolean isCampoValido = validacoesIniciais();
                if (!isCampoValido) {
                    setDefaultBorderColor(DEFAULT_BORDER_ERROR_COLOR);
                } else {
                    setDefaultBorderColor(DEFAULT_BORDER_NORMAL_COLOR);
                }
                field.repaint();
            }
        });

        this.comportamentoTamanhoMaximo(field);

    }

    /**
     * Define o comportamento de tamanho máximo do campo de texto.
     *
     * @param field O campo de texto a ser configurado.
     */
    default void comportamentoTamanhoMaximo(JTextField field) {
        comportamentoTamanhoMaximo(field, null);
    }

    /**
     * Define o comportamento de tamanho máximo do campo de texto.
     *
     * @param field O campo de texto a ser configurado.
     */
    default void comportamentoTamanhoMaximo(JTextField field, String customRegex) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {

            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                int max = getMaxLength();
                InputType inputType = getInputType();

                if (string == null) return;

                String clean = aplicarFiltro(string, inputType, customRegex);

                StringBuilder current = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
                current.insert(offset, clean);

                if (max > 0 && current.length() > max) {
                    Toolkit.getDefaultToolkit().beep();
                    return;
                }

                super.insertString(fb, offset, clean, attr);
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                int max = getMaxLength();
                if (text == null) return;

                InputType inputType = getInputType();

                String clean = aplicarFiltro(text, inputType, customRegex);

                StringBuilder current = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
                current.replace(offset, offset + length, clean);

                if (max > 0 && current.length() > max) {
                    Toolkit.getDefaultToolkit().beep();
                    return;
                }

                super.replace(fb, offset, length, clean, attrs);
            }
        });
    }

    /**
     * Aplica um filtro ao texto de entrada com base no tipo especificado.
     *
     * @param input       o texto de entrada a ser filtrado.
     * @param tipo        o tipo de filtro a ser aplicado, que pode ser NUMERIC, ALPHA, ALPHANUMERIC, CUSTOM ou ANY.
     * @param customRegex uma expressão regular personalizada para o filtro, se necessário.
     * @return o texto filtrado de acordo com o tipo especificado.
     */
    default String aplicarFiltro(String input, InputType tipo, String customRegex) {
        switch (tipo) {
            case NUMERIC:
                return input.replaceAll("[^\\d]", "");
            case ALPHA:
                return input.replaceAll("[^a-zA-Z]", "");
            case ALPHANUMERIC:
                return input.replaceAll("[^a-zA-Z0-9]", "");
            case CUSTOM:
                if (customRegex != null) return input.replaceAll(customRegex, "");
                return input;
            case ANY:
            default:
                return input;
        }
    }

    /**
     * Método para realizar as validações iniciais do campo de texto.
     * As validações podem incluir verificar se o campo é obrigatório,
     * verificar o tamanho máximo e mínimo do texto, entre outras.
     *
     * @return true se as validações iniciais forem bem-sucedidas, false caso contrário.
     */
    default boolean validacoesIniciais() {
        Object value;
        try {
            value = getFieldValue();
        } catch (ParseException e) {
            exibirErro("Valor inválido");
            return false;
        }
        if (isRequired() && value == null) {
            exibirErro("Campo obrigatório");
            return false;
        }

        if (value instanceof String) {
            String strValue = (String) value;
            if (getMaxLength() > 0 && strValue.length() > getMaxLength()) {
                exibirErro("Campo com mais de " + getMaxLength() + " caracteres");
                return false;
            } else if (getMinLength() > 0 && strValue.length() < getMinLength()) {
                exibirErro("Campo com menos de " + getMinLength() + " caracteres");
                return false;
            } else if (strValue.isEmpty() && isRequired()) {
                exibirErro("Campo obrigatório");
                return false;
            } else if (strValue.isEmpty() && !isRequired()) {
                limparErro();
                setDefaultBorderColor(DEFAULT_BORDER_NORMAL_COLOR);
                return true;
            } else {
                setDefaultBorderColor(DEFAULT_BORDER_NORMAL_COLOR);
            }
        }
        limparErro();
        return true;
    }

    /**
     * Exibe uma mensagem de error abaixo do campo de entrada.
     *
     * @param mensagem A mensagem de error a ser exibida, geralmente indicando que o campo é obrigatório.
     */
    default void exibirErro(String mensagem) {
        if (mensagem == null || mensagem.isEmpty()) {
            mensagem = "Campo inválido";
        }
        if (isTouched()) {
            setErrorMessage(mensagem);
            this.setDefaultBorderColor(DEFAULT_BORDER_ERROR_COLOR);
            exibirErroComponentePai(mensagem);
        }
    }

    /**
     * Define o valor da mensagem de error a ser exibida abaixo do campo de entrada.
     *
     * @param mensagem A mensagem de error a ser exibida.
     */
    void setErrorMessage(String mensagem);


    /**
     * Obtem o valor do campo de texto.
     *
     * @return o valor do campo de texto.
     */
    Object getFieldValue() throws ParseException;


    /**
     * Verifica se o componente tem o foco.
     *
     * @return true se o componente tem o foco, false caso contrário.
     */
    default boolean hasFocus() {
        return KeyboardFocusManager.getCurrentKeyboardFocusManager().
                getFocusOwner() == this;
    }

    /**
     * Define a cor padrão da borda.
     *
     * @param defaultBorderHoverColor a cor padrão da borda.
     */
    void setDefaultBorderColor(Color defaultBorderHoverColor);

    /**
     * Verifica se o campo é obrigatório.
     *
     * @return true se o campo for obrigatório, false caso contrário.
     */
    boolean isRequired();

    /**
     * Define se o campo é obrigatório ou não.
     *
     * @param isRequired true se o campo for obrigatório, false caso contrário.
     */
    void setRequired(boolean isRequired);

    /**
     * Obtém o tamanho máximo do campo de texto.
     *
     * @return o tamanho máximo do campo de texto.
     */
    int getMaxLength();

    /**
     * Define o tamanho máximo do campo de texto.
     *
     * @param maxLength o tamanho máximo do campo de texto.
     */
    void setMaxLength(int maxLength);

    /**
     * Obtém o tamanho mínimo do campo de texto.
     *
     * @return o tamanho mínimo do campo de texto.
     */
    int getMinLength();

    /**
     * Define o tamanho mínimo do campo de texto.
     *
     * @param minLength o tamanho mínimo do campo de texto.
     */
    void setMinLength(int minLength);

    /**
     * Verifica se o campo foi tocado (ou seja, se o usuário interagiu com ele).
     *
     * @return <code>true</code> se o campo foi tocado, <code>false</code> caso contrário.
     */
    boolean isTouched();

    /**
     * Define se o campo foi tocado (ou seja, se o usuário interagiu com ele).
     *
     * @param isTouched <code>true</code> se o campo foi tocado, <code>false</code> caso contrário.
     */
    void setIsTouched(boolean isTouched);


    /**
     * Exibe uma mensagem de error no componente pai, se houver.
     *
     * @param mensagem mensagem a ser exibida no componente pai.
     */
    default void exibirErroComponentePai(String mensagem) {
        FieldGroup<?> pai = getParentContainer();

        if (pai != null) {
            pai.showError(mensagem);
        }
    }

    /**
     * Limpa a mensagem de error no componente pai, se houver.
     */
    default void limparErroComponentePai() {
        FieldGroup<?> pai = getParentContainer();

        if (pai != null) {
            pai.cleanError();
        }
    }


    /**
     * Limpa a mensagem de error exibida.
     */
    default void limparErro() {
        setErrorMessage(" ");
        this.setDefaultBorderColor(DEFAULT_BORDER_NORMAL_COLOR);
        limparErroComponentePai();
    }


    /**
     * Obtém o contêiner pai do campo de texto.
     *
     * @return o contêiner pai do campo de texto, ou <code>null</code> se não houver um contêiner pai válido.
     */
    default FieldGroup<?> getParentContainer() {
        Container pai = getParent();
        while (pai != null && !(pai instanceof FieldGroup)) {
            pai = pai.getParent();
        }
        return (FieldGroup<?>) pai;
    }

    /**
     * Obtém o contêiner pai do campo de texto.
     *
     * @return o contêiner pai do campo de texto, ou <code>null</code> se não houver um contêiner pai válido.
     */
    Container getParent();

    /**
     * Obtém o tipo de entrada do campo de texto.
     *
     * @return o tipo de entrada do campo de texto, que pode ser NUMERIC, ALPHA, ALPHANUMERIC, CUSTOM ou ANY.
     */
    InputType getInputType();

    /**
     * Define o tipo de entrada do campo de texto.
     *
     * @param inputType o tipo de entrada a ser definido, que pode ser NUMERIC, ALPHA, ALPHANUMERIC, CUSTOM ou ANY.
     */
    void setInputType(InputType inputType);


    /**
     * Define a altura do botão.
     *
     * @param height A altura desejada do botão.
     */
    default void setButtonHeight(int height) {
        int width = getWidth() > 0 ? getWidth() : getPreferredSize().width;
        setPreferredSize(new Dimension(width, height));
        setMinimumSize(new Dimension(width, height));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
    }

    /**
     * Define o tamanho máximo do campo de texto.
     *
     * @param dimension o tamanho máximo a ser definido, que deve incluir largura e altura.
     */
    void setMaximumSize(Dimension dimension);

    /**
     * Define o tamanho mínimo do campo de texto.
     *
     * @param dimension o tamanho mínimo a ser definido, que deve incluir largura e altura.
     */
    void setMinimumSize(Dimension dimension);

    /**
     * Obtém o tamanho preferido do campo de texto.
     *
     * @return o tamanho preferido do campo de texto, que pode ser 0 se não estiver definido.
     */
    Dimension getPreferredSize();

    /**
     * Define o tamanho preferido do campo de texto.
     *
     * @param dimension o tamanho preferido a ser definido, que deve incluir largura e altura.
     */
    void setPreferredSize(Dimension dimension);

    /**
     * Obtém a largura do campo de texto.
     *
     * @return a largura do campo de texto, que pode ser 0 se não estiver definido.
     */
    int getWidth();
}
