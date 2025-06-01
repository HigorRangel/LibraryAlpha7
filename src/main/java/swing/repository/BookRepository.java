package swing.repository;

import org.hibernate.Session;
import swing.model.Book;
import swing.util.HibernateUtils;

import java.util.List;

/**
 * Repositório para operações relacionadas a livros.
 */
public class BookRepository {
    /**
     * Busca livros pelo ISBN no banco de dados.
     *
     * @param isbn o ISBN do livro a ser buscado.
     * @return uma lista de livros que correspondem ao ISBN fornecido.
     */
    public List<Book> findBookByIsbn(String isbn) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "FROM Book l WHERE l.isbn = :isbn";
            return session.createQuery(hql, Book.class)
                    .setParameter("isbn", isbn)
                    .getResultList();
        }
    }

    /**
     * Busca livros pelo título no banco de dados.
     *
     * @param title o título do livro a ser buscado.
     * @return uma lista de livros que correspondem ao título fornecido.
     */
    public List<Book> findBookByTitle(String title) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "FROM Book l WHERE l.title = :title";
            return session.createQuery(hql, Book.class)
                    .setParameter("title", title)
                    .getResultList();
        }
    }
}
