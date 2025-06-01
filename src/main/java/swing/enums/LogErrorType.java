package swing.enums;

/**
 * Enum representando os tipos de erro de log.
 */
public enum LogErrorType {
    /**
     * Tipo de log para erros.
     */
    ERROR("ERRO"),

    /**
     * Tipo de log para avisos.
     */
    WARNING("AVISO"),

    /**
     * Tipo de log para informações.
     */
    INFO("INFO");

    private final String description;

    LogErrorType(String description) {
        this.description = description;
    }

    /**
     * Obtém a descrição do tipo de log.
     *
     * @return Descrição do tipo de log.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Converte uma string para o tipo de log correspondente.
     *
     * @return O tipo de log correspondente à string fornecida.
     */
    @Override
    public String toString() {
        return description;
    }
}
