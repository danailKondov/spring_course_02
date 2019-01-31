package ru.otus.spring02.service;

import ru.otus.spring02.model.Book;

import java.util.List;

/**
 * Created by хитрый жук on 26.12.2018.
 */
public interface LibraryService {

    List<Book> getAllBooks();
    List<String> getAllAuthorsNames();
    List<String> getAllGenres();
    List<Book> getBooksByAuthorsName(String name);
    List<String> getAllComments(String bookId);

    boolean addNewBook(Book book);
    boolean addComment(String bookId, String userName, String comment);

    boolean updateBookTitleById(String id, String newTitle);

    boolean deleteBookById(String bookId);
    void deleteAll();
}
