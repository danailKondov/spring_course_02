package ru.otus.spring02.dao;

import ru.otus.spring02.model.Author;

import java.util.List;

/**
 * Created by хитрый жук on 26.12.2018.
 */
public interface AuthorDao {
    List<String> getAllAuthorsNames();
    Author getAuthorByName(String name);
    Author getAuthorById(Long id);

    Author addNewAuthor(Author author);

    int deleteAuthor(Author author);
    int deleteAuthorById(Long id);
    int deleteAll();
}
