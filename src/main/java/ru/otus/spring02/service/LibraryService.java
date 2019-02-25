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
    List<String> getAllComments(String bookId);
    List<Comment> getAllFullComments(String id);
    Book getBookById(String id);

    boolean addNewGenre(Genre genre);
    Book addNewBook(Book book);
    boolean addNewAuthor(Author author);
    boolean addComment(String bookId, String userName, String comment);

    boolean updateBookTitleById(String id, String newTitle);
    boolean updateComment(Comment comment);

    boolean deleteBookById(String id);
    boolean deleteAuthorById(String id);
    boolean deleteGenre(String genreName);
    boolean deleteCommentById(String id);
    void deleteAll();
}
