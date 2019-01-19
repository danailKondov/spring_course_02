package ru.otus.spring02.dao;

import org.springframework.stereotype.Repository;
import ru.otus.spring02.model.Comment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by хитрый жук on 15.01.2019.
 */
@Repository
public class CommentDaoImpl implements CommentDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Comment addComment(Comment comment) {
        em.persist(comment);
        return comment;
    }

    @Override
    public List<String> getCommentsByBookId(Long bookId) {
        TypedQuery<String> query = em.createQuery("SELECT c.commentText FROM Comment c WHERE c.book.id = :bookId", String.class);
        query.setParameter("bookId", bookId);
        return query.getResultList();
    }

    @Override
    public Comment getCommentById(Long id) {
        return em.find(Comment.class, id);
    }

    @Override
    public boolean deleteCommentById(Long id) {
        Comment comment = em.find(Comment.class, id);
        if (comment != null) {
            em.remove(comment);
            return true;
        }
        return false;
    }

    @Override
    public void deleteAll() {
        em.createQuery("DELETE FROM Comment").executeUpdate();
    }
}
