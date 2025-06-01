package swing.enums;

/**
 * Enum representando os tipos de entrada permitidos em campos de texto.
 */
public enum InputType {
    /**
     * Permite qualquer tipo de entrada, sem restrições.
     */
    ANY,

    /**
     * Permite apenas números, sem letras ou caracteres especiais.
     */
    NUMERIC,
    /**
     * Permite apenas letras, sem números ou caracteres especiais.
     */
    ALPHA,

    /**
     * Permite apenas letras e números, sem caracteres especiais.
     */
    ALPHANUMERIC,
    /**
     * Permite apenas letras, números e espaços, sem caracteres especiais.
     */
    CUSTOM
}
