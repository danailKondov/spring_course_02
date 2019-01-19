package ru.otus.spring02.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.spring02.model.Author;
import ru.otus.spring02.model.Book;
import ru.otus.spring02.model.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by хитрый жук on 23.12.2018.
 */
@Repository
public class BookDaoImpl implements BookDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<String> getAllTitles() {
        return em.createQuery("SELECT b.title FROM Book b", String.class).getResultList();
    }

    @Override
    public List<Book> getAllBooks() {
        return em.createQuery("SELECT b FROM Book b", Book.class).getResultList();
    }

    @Override
    public List<Book> getBooksByAuthor(Author author) {
        TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b JOIN b.authors a WHERE a.id = :id", Book.class);
        query.setParameter("id", author.getId());
        return query.getResultList();
    }

    @Override
    public Book getBookById(Long id) {
        return em.find(Book.class, id);
    }

    @Override
    public List<Book> getBooksByTitle(String title) {
        TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b WHERE b.title = :title", Book.class);
        query.setParameter("title", title);
        return query.getResultList();
    }

    @Override
    public Book addNewBook(Book book) {
        em.persist(book);
        return book;
    }

    @Override
    public boolean updateBookTitleById(Long id, String newTitle) {
        Book book = em.find(Book.class, id);
        if (book != null) {
            book.setTitle(newTitle);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteBookById(Long id) {
        Book book = em.find(Book.class, id);
        if (book != null) {
            em.remove(book);
            return true;
        }
        return false;
    }

    @Override
    public int deleteAll() {
        return em.createQuery("DELETE FROM Book").executeUpdate();
    }
}
