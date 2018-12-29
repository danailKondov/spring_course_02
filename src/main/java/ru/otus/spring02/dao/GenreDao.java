package ru.otus.spring02.dao;

import ru.otus.spring02.model.Genre;

import java.util.List;

/**
 * Created by хитрый жук on 26.12.2018.
 */
public interface GenreDao {
    List<Genre> getAllGenres();
    Genre getGenreByName(String genreName);

    Genre addGenre(Genre genre);

    int deleteGenre(Genre genre);
    int deleteAll();
}
