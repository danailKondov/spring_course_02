package ru.otus.spring02.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring02.model.Genre;

@Repository
public interface GenreRepository extends ReactiveMongoRepository<Genre, String> {

    Flux<Genre> findAll();
    Mono<Genre> findGenreByGenreName(String name);
    Mono<Long> deleteGenreByGenreName(String name);
}
