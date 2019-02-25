package ru.otus.spring02.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.spring02.model.Genre;

import java.util.List;

@Repository
public interface GenreRepository extends MongoRepository<Genre, String> {

    List<Genre> findAll();
    Genre findGenreByGenreName(String name);
    int deleteGenreByGenreName(String name);
}
