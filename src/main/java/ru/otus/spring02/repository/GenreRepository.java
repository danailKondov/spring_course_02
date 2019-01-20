package ru.otus.spring02.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.spring02.model.Genre;

import java.util.List;

/**
 * Created by хитрый жук on 19.01.2019.
 */
@Repository
public interface GenreRepository extends CrudRepository<Genre, Long> {

    List<Genre> findAll();
    Genre findGenreByGenreName(String name);
    int deleteGenreByGenreName(String name);
}
