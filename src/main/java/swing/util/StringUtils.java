package swing.util;

import org.apache.commons.text.WordUtils;

import java.text.Normalizer;

public class StringUtils extends WordUtils {


    /**
     * Remove os espaços excessivos de uma ‘string’, deixando apenas um espaço entre as palavras.
     *
     * @param value a ‘string’ a ser processada
     * @return uma nova ‘string’ com os espaços excessivos removidos
     */
    public static String removeExcessiveSpaces(String value) {
        if (value == null) {
            return null;
        }
        return value.replaceAll("\\s+", " ").trim();
    }

    /**
     * Remove acentos de uma ‘string’, convertendo caracteres acentuados nas suas versões sem acento.
     *
     * @param value a ‘string’ da qual os acentos serão removidos
     * @return uma nova ‘string’ com os acentos removidos
     */
    public static String removeAccents(String value) {
        if (value == null) {
            return null;
        }
        return Normalizer.normalize(value, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    /**
     * Verifica se uma string é nula ou vazia e retorna uma string vazia se for o caso.
     *
     * @param str a string a ser verificada
     * @return uma string vazia se a entrada for nula ou vazia, caso contrário retorna a própria string
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

}
