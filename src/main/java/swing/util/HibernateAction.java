package swing.util;

import org.hibernate.Session;

@FunctionalInterface
public interface HibernateAction<T> {

    T execute(Session session);

}
