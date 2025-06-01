package swing.view.components;

import org.jdesktop.swingx.prompt.PromptSupport;
import swing.enums.InputType;
import swing.interfaces.TextFieldProperties;
import swing.interfaces.ValidatableField;

import javax.swing.*;
import java.awt.*;

/**
 * CustomTextField é uma classe que estende JTextField e implementa
 * TextFieldProperties e ValidatableField.
 */
public class CustomTextField extends JTextField implements TextFieldProperties, ValidatableField {

    Color defaultBorderColor = DEFAULT_BORDER_NORMAL_COLOR;

    boolean isRequired = false;
    int maxLength = -1;
    int minLength = -1;
    String errorMessage = "";
    boolean isTouched = false;
    InputType inputType = InputType.ANY;

    public CustomTextField() {
        super();
        initialize(this);
        setDisabledTextColor(Color.BLACK);
    }

    /**
     * Define borda arredondada para o campo de texto.
     *
     * @param g o contexto <code>Graphics</code> no qual pintar
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Color color = getBackground() != null ? getBackground() : DEFAULT_BACKGROUND_COLOR;
        Color bgColor = isEnabled()
                ? color
                : DEFAULT_BACKGROUND_COLOR_DISABLED;
        g2.setColor(bgColor);
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
     * Método para definir o campo de texto com as propriedades padrão.
     *
     * @param color Cor padrão da borda
     */
    @Override
    public void setDefaultBorderColor(Color color) {
        this.defaultBorderColor = color;
    }

    /**
     * Verifica se o campo é obrigatório.
     *
     * @return <code>true</code> se o campo for obrigatório, <code>false</code> caso contrário.
     */
    @Override
    public boolean isRequired() {
        return this.isRequired;
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
     * @return o tamanho máximo do campo de texto.
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
     * @return o tamanho mínimo do campo de texto.
     */
    @Override
    public int getMinLength() {
        return this.minLength;
    }

    /**
     * Define o tamanho mínimo do campo de texto.
     *
     * @param minLength o tamanho mínimo do campo de texto.
     */
    @Override
    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    /**
     * Verifica se o campo é válido.
     *
     * @return <code>true</code> se o campo for válido, <code>false</code> caso contrário.
     */
    @Override
    public boolean isValido() {
        return this.validacoesIniciais();
    }

    /**
     * Retorna a mensagem de error se o campo for inválido.
     *
     * @return mensagem de error ou uma ‘string’ vazia se o campo for válido.
     */
    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }

    /**
     * Define a mensagem de error a ser exibida abaixo do campo de entrada.
     *
     * @param mensagemErro A mensagem de error a ser exibida.
     */
    public void setErrorMessage(String mensagemErro) {
        this.errorMessage = mensagemErro;
    }

    /**
     * Obtem o valor do campo de texto.
     *
     * @return o valor do campo de texto.
     */
    @Override
    public String getFieldValue() {
        String value = this.getText();
        return value != null ? value.trim() : "";
    }

    /**
     * Limpa a mensagem de error exibida.
     */
    @Override
    public void limparErro() {
        TextFieldProperties.super.limparErro();
    }

    /**
     * Exibe uma mensagem de error abaixo do campo de entrada.
     *
     * @param mensagem a mensagem de error a ser exibida.
     */
    @Override
    public void exibirErro(String mensagem) {
        TextFieldProperties.super.exibirErro(mensagem);
    }

    /**
     * Retorna o valor do campo.
     *
     * @return valor do campo, que pode ser de qualquer tipo, dependendo da implementação.
     */
    @Override
    public Object getValor() {
        return this.getText();
    }

    /**
     * Verifica se o campo foi tocado (ou seja, se o usuário interagiu com ele).
     *
     * @return <code>true</code> se o campo foi tocado, <code>false</code> caso contrário.
     */
    public boolean isTouched() {
        return isTouched;
    }

    /**
     * Define se o campo foi tocado (ou seja, se o usuário interagiu com ele).
     *
     * @param isTouched <code>true</code> se o campo foi tocado, <code>false</code> caso contrário.
     */
    public void setIsTouched(boolean isTouched) {
        this.isTouched = isTouched;
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

    /**
     * Define o placeholder do campo de texto.
     *
     * @param placeholder o texto que será exibido como placeholder.
     */
    public void setPlaceholder(String placeholder) {
        PromptSupport.setPrompt(placeholder, this);
    }
}
