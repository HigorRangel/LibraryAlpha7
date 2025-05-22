package swing.util;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class HibernateExecutor {


    public static <T> T executeTransaction(HibernateAction<T> function) {
        Transaction tx = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            T result = function.execute(session);
            tx.commit();
            return result;
        }
    }
}
