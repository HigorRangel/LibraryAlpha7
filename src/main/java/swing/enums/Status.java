package swing.enums;

/**
 * Enum representando os status de um objeto.
 */
public enum Status {
    /**
     * Status ativo.
     */
    ACTIVE('A'),
    /**
     * Status inativo.
     */
    INACTIVE('I');

    /**
     * Status representado por um caractere.
     */
    private final Character status;

    Status(Character status) {
        if (status == null) {
            throw new IllegalArgumentException("Status não pode ser nulo");
        }
        this.status = status;
    }

    /**
     * Converte uma ‘string’ representando o status num objeto Status.
     *
     * @param status a string contendo o status a ser convertido, que deve ser um único caractere.
     * @return o objeto Status correspondente ao caractere fornecido.
     */
    public static Status fromCharacter(String status) {
        if (status == null) {
            return null;
        }
        Character statusChar = status.charAt(0);
        for (Status s : Status.values()) {
            if (s.getStatus().equals(statusChar)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Status inválido: " + status);
    }

    /**
     * Obtém o caractere que representa o status.
     *
     * @return o caractere que representa o status, nunca nulo.
     */
    public Character getStatus() {
        return status;
    }

    /**
     * Converte o status para uma representação em string.
     *
     * @return a representação em string do status, que é o próprio caractere do status.
     */
    @Override
    public String toString() {
        return status.toString();
    }
}
