package ru.otus.spring02.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring02.model.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by хитрый жук on 26.12.2018.
 */
@Repository
public class AuthorDaoImpl implements AuthorDao {

    private final NamedParameterJdbcOperations namedJdbc;

    @Autowired
    public AuthorDaoImpl(NamedParameterJdbcOperations namedJdbc) {
        this.namedJdbc = namedJdbc;
    }

    @Override
    public List<String> getAllAuthorsNames() {
        return namedJdbc.getJdbcOperations().queryForList("SELECT author_name FROM authors", String.class);
    }

    @Override
    public Author getAuthorByName(String name) {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("a_name", name);
        try {
            return namedJdbc.queryForObject("SELECT * FROM authors WHERE author_name = :a_name", params, new AuthorsMapper());
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public Author addNewAuthor(Author author) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("a_name", author.getName());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedJdbc.update("INSERT INTO authors(author_name) VALUES (:a_name)", param, keyHolder, new String[] { "id" });

        author.setId(keyHolder.getKey().longValue());
        author.setBooks(Collections.emptyList());
        return author;
    }

    @Override
    public int deleteAuthor(Author author) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("id", author.getId());
        return namedJdbc.update("DELETE FROM authors WHERE id = :id", param);
    }

    @Override
    public int deleteAll() {
        return namedJdbc.getJdbcOperations().update("DELETE FROM authors");
    }

    @Override
    public Author getAuthorById(Long id) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("id", id);
        return namedJdbc.queryForObject("SELECT * FROM authors WHERE id = :id", param, new AuthorsMapper());
    }

    @Override
    public int deleteAuthorById(Long id) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("id", id);
        return namedJdbc.update("DELETE FROM authors WHERE id = :id", param);
    }

    private static class AuthorsMapper implements RowMapper<Author> {
        @Override
        public Author mapRow(ResultSet resultSet, int i) throws SQLException {
            Author author = new Author();
            author.setId(resultSet.getLong("id"));
            author.setName(resultSet.getString("author_name"));
            return author;
        }
    }
}
