package ru.otus.spring02.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import ru.otus.spring02.model.Book;

import java.util.List;

@Repository
public interface BookRepository extends MongoRepository<Book, String>, BookRepositoryCustom {

    List<Book> findAll();

    List<Book> findAllByAuthorsId(String authorId);

    List<Book> findBooksByTitle(String title);

    @Nullable
    Book findBookById(String id);

    int deleteBookById(String id);
}
