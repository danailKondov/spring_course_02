package ru.otus.spring02.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.otus.spring02.model.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {

    @Query("SELECT c.commentText FROM Comment c WHERE c.book.id = :bookId")
    List<String> findCommentsTextByBookId(@Param(value = "bookId") Long bookId);

    List<Comment> findCommentsByBook_Id(Long id);

    int deleteCommentById(Long id);
}
