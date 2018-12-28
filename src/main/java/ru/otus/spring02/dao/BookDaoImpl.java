package ru.otus.spring02.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring02.model.Author;
import ru.otus.spring02.model.Book;
import ru.otus.spring02.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by хитрый жук on 23.12.2018.
 */
@Repository
public class BookDaoImpl implements BookDao {

    private final NamedParameterJdbcOperations namedJdbc;

    @Autowired
    public BookDaoImpl(NamedParameterJdbcOperations namedJdbc) {
        this.namedJdbc = namedJdbc;
    }

    @Override
    public List<String> getAllTitles() {
        return namedJdbc.getJdbcOperations().queryForList("SELECT title FROM books", String.class);
    }

    @Override
    public List<Book> getAllBooks() {
        return namedJdbc.getJdbcOperations().query(
             "SELECT b.id, b.title, g.id as genre_id, g.genre_name, a.id as author_id, a.author_name " +
                "FROM books AS b " +
                "LEFT OUTER JOIN genres AS g ON b.genre_id = g.id " +
                "LEFT OUTER JOIN books_authors AS ba ON b.id = ba.books_id " +
                "LEFT OUTER JOIN authors AS a ON ba.authors_id = a.id", new BookExtractor());
    }

    @Override
    public List<Book> getBooksByAuthor(Author author) {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("id", author.getId());
        return namedJdbc.query(
             "SELECT b.id, b.title, g.id as genre_id, g.genre_name, a.id as author_id, a.author_name " +
                "FROM books AS b " +
                "LEFT OUTER JOIN genres AS g ON b.genre_id = g.id " +
                "LEFT OUTER JOIN books_authors AS ba ON b.id = ba.books_id " +
                "LEFT OUTER JOIN authors AS a ON ba.authors_id = a.id " +
                "WHERE a.id = :id", params, new BookExtractor());
    }

    @Override
    public Book getBookById(Long id) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("id", id);
        List<Book> books = namedJdbc.query(
             "SELECT b.id, b.title, g.id as genre_id, g.genre_name, a.id as author_id, a.author_name " +
                "FROM books AS b " +
                "LEFT OUTER JOIN genres AS g ON b.genre_id = g.id " +
                "LEFT OUTER JOIN books_authors AS ba ON b.id = ba.books_id " +
                "LEFT OUTER JOIN authors AS a ON ba.authors_id = a.id " +
                "WHERE b.id = :id", param, new BookExtractor());
        Book result = null;
        if (!books.isEmpty()) {
            result = books.get(0);
        }
        return result;
    }

    @Override
    public List<Book> getBooksByTitle(String title) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("title", title);
        return namedJdbc.query(
                "SELECT b.id, b.title, g.id as genre_id, g.genre_name, a.id as author_id, a.author_name " +
                        "FROM books AS b " +
                        "LEFT OUTER JOIN genres AS g ON b.genre_id = g.id " +
                        "LEFT OUTER JOIN books_authors AS ba ON b.id = ba.books_id " +
                        "LEFT OUTER JOIN authors AS a ON ba.authors_id = a.id " +
                        "WHERE b.title = :title", param, new BookExtractor());
    }

    @Override
    public Book addNewBook(Book book) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("t_name", book.getTitle());
        param.addValue("g_id", book.getGenre().getId());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedJdbc.update(
                "INSERT INTO books (title, genre_id) " +
                        "VALUES (:t_name, :g_id)", param, keyHolder, new String[] { "id" });
        Long bookId = keyHolder.getKey().longValue();

        for (Author author : book.getAuthors()) {
            MapSqlParameterSource param2 = new MapSqlParameterSource();
            param2.addValue("aut_id", author.getId());
            param2.addValue("b_id", bookId);
            namedJdbc.update(
                    "INSERT INTO books_authors (books_id, authors_id) " +
                            "VALUES (:b_id, :aut_id)", param2);
        }

        book.setId(bookId);
        return book;
    }

    @Override
    public int updateBookTitleById(Long id, String newTitle) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("id", id);
        param.addValue("new_title", newTitle);
        return namedJdbc.update(
                "UPDATE books SET title = :new_title " +
                        "WHERE id = :id", param);
    }

    @Override
    public int deleteBookById(Long id) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("id", id);
        return namedJdbc.update("DELETE FROM books WHERE id = :id", param);
    }

    @Override
    public int deleteAll() {
        namedJdbc.getJdbcOperations().update("DELETE FROM books_authors");
        return namedJdbc.getJdbcOperations().update("DELETE FROM books");
    }

    private static class BookExtractor implements ResultSetExtractor<List<Book>> {

        Map<Long, Book> bookMap = new HashMap<>();

        @Override
        public List<Book> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Book book = bookMap.get(id);
                if (book == null) {
                    book = new Book();
                    book.setId(id);
                    book.setTitle(resultSet.getString("title"));
                    Genre genre = new Genre();
                    genre.setId(resultSet.getLong("genre_id"));
                    genre.setGenreName(resultSet.getString("genre_name"));
                    book.setGenre(genre);
                    List<Author> authors = new ArrayList<>();
                    extractAuthor(resultSet, book, authors);
                    bookMap.put(id, book);
                } else {
                    List<Author> authors = book.getAuthors();
                    extractAuthor(resultSet, book, authors);
                }
            }
            return new ArrayList<>(bookMap.values());
        }

        private void extractAuthor(ResultSet resultSet, Book book, List<Author> authors) throws SQLException {
            String name = resultSet.getString("author_name");
            if (name != null) {
                Author author = new Author();
                author.setId(resultSet.getLong("author_id"));
                author.setName(name);
                authors.add(author);
            }
            book.setAuthors(authors);
        }
    }
}
