package swing.interfaces;

import java.util.Set;

/**
 * ‘Interface’ para objetos que podem ser importados de um arquivo CSV.
 */
public interface CSVImportable extends Identifiable {


    /**
     * Retorna possíveis erros de importação
     *
     * @return String com os erros de importação
     */
    Set<String> getImportErrors();


    /**
     * Adiciona um error de importação à lista de erros
     *
     * @param erro ‘String’ com o error de importação a ser adicionado
     */
    void addErroImportacao(String erro);

}
