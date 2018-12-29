package ru.otus.spring02.dao;

import ru.otus.spring02.model.Author;
import ru.otus.spring02.model.Book;

import java.util.List;

/**
 * Created by хитрый жук on 26.12.2018.
 */
public interface BookDao {

    List<String> getAllTitles();

    List<Book> getAllBooks();
    List<Book> getBooksByAuthor(Author author);
    List<Book> getBooksByTitle(String title);
    Book getBookById(Long id);

    Book addNewBook(Book book);

    int updateBookTitleById(Long id, String newTitle);

    int deleteBookById(Long id);
    int deleteAll();
}
