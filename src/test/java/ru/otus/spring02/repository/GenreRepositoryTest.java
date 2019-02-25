package ru.otus.spring02.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.spring02.model.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataMongoTest
@DirtiesContext // в т.ч. in-memory база пересоздается каждый тест
public class GenreRepositoryTest {

    private static final String TEST_NAME_1 = "testName";
    private static final String TEST_NAME_2 = "testName2";

    @Autowired
    private GenreRepository repository;

    @Before
    public void init() {
        repository.deleteAll();
    }

    @Test
    public void addGenreTest() {
        Genre genre = new Genre();
        genre.setGenreName(TEST_NAME_1);
        repository.save(genre);

        List<Genre> genres = repository.findAll();
        assertThat(genres).isNotEmpty();
        assertThat(genres.get(0)).isNotNull().hasNoNullFieldsOrProperties();
    }

    @Test
    public void getAllGenresTest() {
        List<Genre> genres = repository.findAll();
        assertThat(genres).isEmpty();

        Genre genre = addTestGenre(TEST_NAME_1);
        Genre genre2 = addTestGenre(TEST_NAME_2);

        genres = repository.findAll();
        assertThat(genres)
                .hasSize(2)
                .contains(genre, genre2);
    }

    @Test
    public void getGenreByNameTest() {
        Genre genre1 = addTestGenre(TEST_NAME_1);
        Genre genre2 = repository.findGenreByGenreName(TEST_NAME_1);
        assertThat(genre2).isEqualTo(genre1);
    }

    @Test
    public void deleteGenreTest() {
        Genre genre = addTestGenre(TEST_NAME_1);
        Genre genre2 = addTestGenre(TEST_NAME_2);
        List<Genre> genres = repository.findAll();
        assertThat(genres)
                .hasSize(2)
                .contains(genre, genre2);

        int result = repository.deleteGenreByGenreName(genre.getGenreName());

        genres = repository.findAll();
        assertThat(result > 0).isTrue();
        assertThat(genres)
                .hasSize(1)
                .contains(genre2).
                doesNotContain(genre);
    }

    @Test
    public void deleteAllTest() {
        Genre genre = addTestGenre(TEST_NAME_1);
        Genre genre2 = addTestGenre(TEST_NAME_2);
        List<Genre> genres = repository.findAll();
        assertThat(genres)
                .hasSize(2)
                .contains(genre, genre2);

        repository.deleteAll();

        genres = repository.findAll();
        assertThat(genres).isEmpty();
    }

    private Genre addTestGenre(String testName) {
        Genre genre = new Genre();
        genre.setGenreName(testName);
        genre = repository.save(genre);
        return genre;
    }

}