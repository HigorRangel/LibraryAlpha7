package swing.repository;

import org.hibernate.Session;
import swing.model.Author;
import swing.util.HibernateUtils;

import java.util.List;

/**
 * Repositório para operações relacionadas a autores.
 * Este repositório permite buscar autores pelo nome comercial ou nome completo.
 */
public class AuthorRepository {

    /**
     * Busca autores pelo name comercial ou name completo.
     *
     * @param name name do autor a ser buscado
     * @return lista de autores que correspondem ao name fornecido
     */
    public List<Author> findAuthorByName(String name) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "FROM Author l WHERE l.commercialName = :name OR l.fullName = :name";
            return session.createQuery(hql, Author.class)
                    .setParameter("name", name)
                    .getResultList();
        }
    }
}
