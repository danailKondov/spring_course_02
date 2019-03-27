package ru.otus.spring02.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring02.model.MongoBook;

public interface MongoBookRepository extends MongoRepository<MongoBook, String> {
}
