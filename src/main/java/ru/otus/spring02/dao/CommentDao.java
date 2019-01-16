package ru.otus.spring02.dao;

import ru.otus.spring02.model.Comment;

import java.util.List;

/**
 * Created by хитрый жук on 15.01.2019.
 */
public interface CommentDao {
    Comment addComment(Comment comment);

    List<String> getCommentsByBookId(Long bookId);

    Comment getCommentById(Long id);

    boolean deleteCommentById(Long id);

    void deleteAll();
}
