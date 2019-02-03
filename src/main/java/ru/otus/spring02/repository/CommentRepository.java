package ru.otus.spring02.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.otus.spring02.model.Comment;

import java.util.List;

/**
 * Created by хитрый жук on 19.01.2019.
 */
@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {

    @Query("SELECT c.commentText FROM Comment c WHERE c.book.id = :bookId")
    List<String> findCommentsByBookId(@Param(value = "bookId") Long bookId);

    List<Comment> findAllById(Long id);

    int deleteCommentById(Long id);
}
