package ru.otus.spring02.service;

import ru.otus.spring02.model.Author;
import ru.otus.spring02.model.Book;
import ru.otus.spring02.model.Genre;

import java.util.List;

/**
 * Created by хитрый жук on 26.12.2018.
 */
public interface LibraryService {

    List<Book> getAllBooks();
    List<String> getAllAuthorsNames();
    List<String> getAllGenres();
    List<Book> getBooksByAuthorsName(String name);

    boolean addNewGenre(Genre genre);
    boolean addNewBook(Book book);
    boolean addNewAuthor(Author author);

    boolean updateBookTitleById(Long id, String newTitle);

    boolean deleteBookById(Long id);
    boolean deleteAuthorById(Long id);
    boolean deleteGenre(String genreName);
    void deleteAll();
}
