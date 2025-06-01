package swing.repository;

import org.hibernate.Session;
import swing.model.Publisher;
import swing.util.HibernateUtils;

import java.util.List;

/**
 * Classe responsável por realizar operações de persistência relacionadas a editoras.
 */
public class PublisherRepository {
    /**
     * Busca uma editora pelo name.
     *
     * @param name name da editora a ser buscada
     * @return lista de editoras que correspondem ao name fornecido
     */
    public List<Publisher> findPublisherByName(String name) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "FROM Publisher e WHERE e.name = :name";
            return session.createQuery(hql, Publisher.class)
                    .setParameter("name", name)
                    .getResultList();
        }
    }
}
