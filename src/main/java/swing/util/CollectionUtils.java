package swing.util;

import java.util.Collection;

/**
 * Classe utilitária para operações com coleções. <br>
 * Fornece métodos para manipulação de listas, mapas e outras coleções.
 */
public class CollectionUtils {

    private CollectionUtils() {
    }

    /**
     * Verifica se uma lista é nula ou vazia.
     *
     * @param collection a lista a ser verificada
     * @return true se a lista for nula ou vazia, false caso contrário
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
}
