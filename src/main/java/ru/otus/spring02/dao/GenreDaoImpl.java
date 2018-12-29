package ru.otus.spring02.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring02.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by хитрый жук on 26.12.2018.
 */
@Repository
public class GenreDaoImpl implements GenreDao {

    private final NamedParameterJdbcOperations namedJdbc;

    @Autowired
    public GenreDaoImpl(NamedParameterJdbcOperations namedJdbc) {
        this.namedJdbc = namedJdbc;
    }

    @Override
    public List<Genre> getAllGenres() {
        return namedJdbc.getJdbcOperations().query("SELECT * FROM genres", new GenreMapper());
    }

    @Override
    public Genre addGenre(Genre genre) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("g_name", genre.getGenreName());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedJdbc.update("INSERT INTO genres (genre_name) VALUES (:g_name)", param, keyHolder, new String[] { "id" });
        genre.setId(keyHolder.getKey().longValue());
        return genre;
    }

    @Override
    public Genre getGenreByName(String genreName) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("g_name", genreName);
        try {
            return namedJdbc.queryForObject("SELECT * FROM genres WHERE genre_name = :g_name", param, new GenreMapper());
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public int deleteGenre(Genre genre) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("id", genre.getId());
        namedJdbc.update("UPDATE books SET genre_id = null WHERE genre_id = :id", param);
        return namedJdbc.update("DELETE FROM genres WHERE id = :id", param);
    }

    @Override
    public int deleteAll() {
        namedJdbc.getJdbcOperations().update("UPDATE books SET genre_id = null");
        return namedJdbc.getJdbcOperations().update("DELETE FROM genres");
    }

    private static class GenreMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
            Genre genre = new Genre();
            genre.setId(resultSet.getLong("id"));
            genre.setGenreName(resultSet.getString("genre_name"));
            return genre;
        }
    }
}
