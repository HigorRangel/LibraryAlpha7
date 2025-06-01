package swing.util;

import org.hibernate.Session;

/**
 * ‘Interface’ funcional para ações que envolvem o Hibernate.
 *
 * @param <T> o tipo de retorno da ação executada.
 */
@FunctionalInterface
public interface HibernateAction<T> {

    /**
     * Executa uma ação numa sessão do Hibernate.
     *
     * @param session a sessão do Hibernate onde a ação será executada.
     * @return o resultado da ação executada, que pode ser de qualquer tipo especificado por <T>.
     */
    T execute(Session session);

}
