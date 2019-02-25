package ru.otus.spring02.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import ru.otus.spring02.model.Author;

@Repository
public interface AuthorRepository extends MongoRepository<Author, String> {

    @Nullable
    Author findAuthorByName(String name);

    @Nullable
    Author findAuthorById(String id);

    int deleteAuthorById(String id);
}
