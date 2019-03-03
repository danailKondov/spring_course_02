package ru.otus.spring02.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ru.otus.spring02.model.Author;

@Repository
public interface AuthorRepository extends ReactiveMongoRepository<Author, String> {

    Mono<Author> findAuthorByName(String name);

    Mono<Author> save(Mono<Author> author);

    Mono<Long> deleteAuthorById(String id);

    Mono<Void> deleteAll();
}
