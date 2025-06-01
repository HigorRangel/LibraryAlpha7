package swing.interfaces;

/**
 * ‘Interface’ para campos que precisam de validação.
 */
public interface ValidatableField {

    /**
     * Verifica se o campo é válido.
     *
     * @return true se o campo for válido, false caso contrário.
     */
    boolean isValido();

    /**
     * Retorna a mensagem de error se o campo for inválido.
     *
     * @return mensagem de error ou uma ‘string’ vazia se o campo for válido.
     */
    String getErrorMessage();

    /**
     * Exibe uma mensagem de error.
     *
     * @param mensagem mensagem a ser exibida.
     */
    void exibirErro(String mensagem);

    /**
     * Limpa a mensagem de error exibida.
     */
    void limparErro();

    /**
     * Retorna o valor do campo.
     *
     * @return valor do campo, que pode ser de qualquer tipo, dependendo da implementação.
     */
    Object getValor();
}
