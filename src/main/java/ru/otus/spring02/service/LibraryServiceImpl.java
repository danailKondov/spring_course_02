package ru.otus.spring02.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring02.dao.AuthorDao;
import ru.otus.spring02.dao.BookDao;
import ru.otus.spring02.dao.GenreDaoImpl;
import ru.otus.spring02.model.Author;
import ru.otus.spring02.model.Book;
import ru.otus.spring02.model.Genre;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by хитрый жук on 26.12.2018.
 */
@Service
@Transactional
public class LibraryServiceImpl implements LibraryService {

    private BookDao bookDao;
    private AuthorDao authorDao;
    private GenreDaoImpl genreDao;

    @Autowired
    public LibraryServiceImpl(BookDao bookDao, AuthorDao authorDao, GenreDaoImpl genreDao) {
        this.bookDao = bookDao;
        this.authorDao = authorDao;
        this.genreDao = genreDao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        return bookDao.getAllBooks();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllAuthorsNames() {
        return authorDao.getAllAuthorsNames();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllGenres() {
        return genreDao.getAllGenres().stream()
                .map(Genre::getGenreName)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getBooksByAuthorsName(String name) {
        Author author = authorDao.getAuthorByName(name);
        if (author == null) {
            return Collections.emptyList();
        }
        return bookDao.getBooksByAuthor(author);
    }

    @Override
    public boolean addNewGenre(Genre genre) {
        Genre checkGenre = genreDao.getGenreByName(genre.getGenreName());
        if (checkGenre != null) {
            return false;
        }
        genre = genreDao.addGenre(genre);
        return genre.getId() != null;
    }

    @Override
    public boolean addNewBook(Book book) {

        Genre genre = genreDao.getGenreByName(book.getGenre().getGenreName());
        if (genre == null) {
            genre = genreDao.addGenre(book.getGenre());
        }
        book.setGenre(genre);

        List<Author> authorList = new ArrayList<>();
        for (Author author : book.getAuthors()) {
            Author checkAuthor = authorDao.getAuthorByName(author.getName());
            if (checkAuthor == null) {
                author = authorDao.addNewAuthor(author);
                authorList.add(author);
            } else {
                authorList.add(checkAuthor);
            }
        }
        book.setAuthors(authorList);

        if(isBookDuplicate(book)) {
            return false;
        }

        book = bookDao.addNewBook(book);
        return book.getId() != null;
    }

    private boolean isBookDuplicate(Book book) {
        List<Book> booksWithSameTitle = bookDao.getBooksByTitle(book.getTitle());
        if (!booksWithSameTitle.isEmpty()) {
            for (Book bookFromDB : booksWithSameTitle) {
                if (book.getGenre().equals(bookFromDB.getGenre())
                        && book.getAuthors().containsAll(bookFromDB.getAuthors())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean addNewAuthor(Author author) {
        Author result = authorDao.addNewAuthor(author);
        return result.getId() != null;
    }

    @Override
    public boolean updateBookTitleById(Long id, String newTitle) {
        int result = bookDao.updateBookTitleById(id, newTitle);
        return result > 0;
    }

    @Override
    public boolean deleteBookById(Long id) {
        int result = bookDao.deleteBookById(id);
        return result > 0;
    }

    @Override
    public boolean deleteAuthorById(Long id) {
        int result = authorDao.deleteAuthorById(id);
        return result > 0;
    }

    @Override
    public boolean deleteGenre(String genreName) {
        Genre genre = genreDao.getGenreByName(genreName);
        if (genre == null) {
            return false;
        }
        int result = genreDao.deleteGenre(genre);
        return result > 0;
    }

    @Override
    public void deleteAll() {
        genreDao.deleteAll();
        authorDao.deleteAll();
        bookDao.deleteAll();
    }
}
