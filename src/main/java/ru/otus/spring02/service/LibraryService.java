package ru.otus.spring02.service;

import ru.otus.spring02.model.Author;
import ru.otus.spring02.model.Book;
import ru.otus.spring02.model.Comment;
import ru.otus.spring02.model.Genre;

import java.util.List;

public interface LibraryService {

    List<Book> getAllBooks();
    List<String> getAllAuthorsNames();
    List<String> getAllGenres();
    List<Book> getBooksByAuthorsName(String name);
    List<String> getAllComments(Long bookId);
    List<Comment> getAllFullComments(Long id);
    Book getBookById(Long id);

    boolean addNewGenre(Genre genre);
    Book addNewBook(Book book);
    boolean addNewAuthor(Author author);
    boolean addComment(Long bookId, String userName, String comment);

    boolean updateBookTitleById(Long id, String newTitle);
    boolean updateComment(Comment comment);

    boolean deleteBookById(Long id);
    boolean deleteAuthorById(Long id);
    boolean deleteGenre(String genreName);
    boolean deleteCommentById(Long id);
    void deleteAll();
}
