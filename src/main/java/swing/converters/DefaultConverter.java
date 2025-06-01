package swing.converters;

import swing.interfaces.CSVConverter;

/**
 * Conversor padrão que simplesmente retorna o valor da ‘string’ sem modificações.
 */
public class DefaultConverter implements CSVConverter<Object> {

    /**
     * Converte uma ‘string’ num objeto, retornando o próprio valor da ‘string’.
     *
     * @param valor a ‘string’ a ser convertida, que pode ser qualquer valor textual.
     * @return o próprio valor da ‘string’, sem alterações.
     */
    @Override
    public Object converter(String valor) {
        return valor;
    }
}
