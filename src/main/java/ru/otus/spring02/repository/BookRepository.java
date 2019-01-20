package ru.otus.spring02.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import ru.otus.spring02.model.Book;

import java.util.List;

/**
 * Created by хитрый жук on 19.01.2019.
 */
@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    @Query("SELECT b.title FROM Book b")
    List<String> findAllTitles();

    List<Book> findAll();

    @Query("SELECT b FROM Book b JOIN b.authors a WHERE a.id = :id")
    List<Book> findBooksByAuthorId(@Param(value = "id") Long authorId);

    List<Book> findBooksByTitle(String title);

    @Nullable
    Book findBookById(Long id);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Book b SET b.title = :title WHERE b.id = :id")
    int updateBookTitleById(@Param(value = "id") Long id, @Param(value = "title") String newTitle);

    int deleteBookById(Long id);
}
