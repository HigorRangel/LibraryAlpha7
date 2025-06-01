package swing.util;

import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Classe utilitária para executar ações no Hibernate numa transação.
 */
public class HibernateExecutor {

    private HibernateExecutor() {
    }

    /**
     * Executa uma ação no Hibernate numa transação.
     *
     * @param function a ação a ser executada, encapsulada numa ‘interface’ funcional.
     * @param <T>      o tipo do resultado da ação.
     * @return o resultado da ação executada.
     */
    public static <T> T executeTransaction(HibernateAction<T> function) {
        Transaction tx;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            T result = function.execute(session);
            tx.commit();
            return result;
        }
    }
}
