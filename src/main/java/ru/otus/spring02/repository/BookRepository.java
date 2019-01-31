package ru.otus.spring02.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.lang.Nullable;
import ru.otus.spring02.model.Book;

import java.util.List;

public interface BookRepository extends MongoRepository<Book, String> {

    List<Book> findAll();

    @Query(value = "{'title':'?0'}")
    List<Book> findBooksByTitle(String title);

    @Nullable
    Book findBookById(String id);

    List<Book> findBooksByAuthors(String name);

    @Query(value = "{'id':'?0'}", fields = "{'comments': 1}")
    Book findBookWithCommentsById(String id);

    int deleteBookById(String id);
}
