package ru.otus.spring02.integration;

import ru.otus.spring02.model.Book;

public interface RandomCommentService {
    Book addRandomComment(Book book);
}
