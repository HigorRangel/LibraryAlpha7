package swing.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import swing.model.Author;
import swing.model.Book;
import swing.model.Publisher;

/**
 * Classe utilitária para configurar o Hibernate e fornecer uma instância de SessionFactory.
 */
public class HibernateUtils {
    static final SessionFactory sessionFactory;

    static {
        try {
            Configuration configuration = new Configuration().configure();
            configuration.addAnnotatedClass(Book.class);
            configuration.addAnnotatedClass(Author.class);
            configuration.addAnnotatedClass(Publisher.class);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();

            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Exception ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    HibernateUtils() {

    }

    /**
     * Obtém a instância de SessionFactory configurada.
     *
     * @return Instância de SessionFactory.
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
