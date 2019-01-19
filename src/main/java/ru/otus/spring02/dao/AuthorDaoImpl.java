package ru.otus.spring02.dao;

import org.springframework.stereotype.Repository;
import ru.otus.spring02.model.Author;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by хитрый жук on 13.01.2019.
 */
@Repository
public class AuthorDaoImpl implements AuthorDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<String> getAllAuthorsNames() {
        TypedQuery<String> query = em.createQuery("SELECT a.name FROM Author a", String.class);
        return query.getResultList();
    }

    @Override
    public Author getAuthorByName(String name) {
        TypedQuery<Author> query = em.createQuery("SELECT a FROM Author a WHERE a.name = :name", Author.class);
        query.setParameter("name", name);
        try {
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Author addNewAuthor(Author author) {
        em.persist(author);
        return author;
    }

    @Override
    public void deleteAuthor(Author author) {
        em.remove(author);
    }

    @Override
    public int deleteAll() {
        return em.createQuery("DELETE FROM Author").executeUpdate();
    }

    @Override
    public Author getAuthorById(Long id) {
        return em.find(Author.class, id);
    }

    @Override
    public boolean deleteAuthorById(Long id) {
        Author author = em.find(Author.class, id);
        if (author != null) {
            em.remove(author);
            return true;
        }
        return false;
    }
}
