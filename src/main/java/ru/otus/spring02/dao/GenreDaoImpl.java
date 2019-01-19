package ru.otus.spring02.dao;

import org.springframework.stereotype.Repository;
import ru.otus.spring02.model.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by хитрый жук on 26.12.2018.
 */
@Repository
public class GenreDaoImpl implements GenreDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Genre> getAllGenres() {
        return em.createQuery("SELECT g FROM Genre g", Genre.class).getResultList();
    }

    @Override
    public Genre addGenre(Genre genre) {
        em.persist(genre);
        return genre;
    }

    @Override
    public Genre getGenreByName(String genreName) {
        TypedQuery<Genre> query = em.createQuery("SELECT g FROM Genre g WHERE g.genreName = :name", Genre.class);
        query.setParameter("name", genreName);
        try {
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void deleteGenre(Genre genre) {
        em.remove(genre);
    }

    @Override
    public int deleteAll() {
        return em.createQuery("DELETE FROM Genre").executeUpdate();
    }
}
