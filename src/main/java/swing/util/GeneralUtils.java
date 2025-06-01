package swing.util;

/**
 * Classe utilitária para fornecer métodos gerais que podem ser usados em diferentes partes do aplicativo.
 */
public class GeneralUtils {

    private GeneralUtils() {
    }

    /**
     * Pausa a execução do programa por um determinado número de milissegundos.
     *
     * @param milissegundos Número de milissegundos para pause a execução.
     */
    public static void pause(int milissegundos) {
        try {
            Thread.sleep(milissegundos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
