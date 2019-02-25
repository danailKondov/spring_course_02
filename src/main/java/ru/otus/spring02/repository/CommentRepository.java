package ru.otus.spring02.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.spring02.model.Comment;

import java.util.List;


@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {

    List<Comment> findAllById(String id);

    List<Comment> findCommentsByBook_Id(String id);

    int deleteCommentById(String id);
}
