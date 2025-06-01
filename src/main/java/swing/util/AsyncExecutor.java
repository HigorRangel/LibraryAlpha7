package swing.util;

import javax.swing.*;

/**
 * Classe utilitária para executar tarefas assíncronas no EDT (Event Dispatch Thread).
 * Permite que tarefas de longa duração sejam executadas em segundo plano, evitando
 * o bloqueio da interface do usuário.
 */
public class AsyncExecutor {

    private AsyncExecutor() {
    }

    /**
     * Executa uma tarefa em segundo plano com suporte à finalização no EDT.
     *
     * @param backgroundTask código a ser executado em background
     * @param onDone         código a ser executado no final (opcional)
     */
    public static void runAsync(Runnable backgroundTask, Runnable onDone) {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                backgroundTask.run();
                return null;
            }

            @Override
            protected void done() {
                if (onDone != null) {
                    SwingUtilities.invokeLater(onDone);
                }
            }
        }.execute();
    }
}
