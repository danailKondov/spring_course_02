package ru.otus.spring02.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import ru.otus.spring02.model.Author;

import java.util.List;

/**
 * Created by хитрый жук on 19.01.2019.
 */
@Repository
public interface AuthorRepository extends CrudRepository<Author, Long> {

    @Query("SELECT a.name FROM Author a")
    List<String> findAllAuthorsNames();

    @Nullable
    Author findAuthorByName(String name);

    @Nullable
    Author findAuthorById(Long id);

    int deleteAuthorById(Long id);
}
