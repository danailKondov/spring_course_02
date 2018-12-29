package ru.otus.spring02.dao;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.spring02.model.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by хитрый жук on 28.12.2018.
 */
@JdbcTest
@RunWith(SpringRunner.class)
@Import(GenreDaoImpl.class)
public class GenreDaoTest {

    private static final String TEST_NAME_1 = "testName";
    private static final String TEST_NAME_2 = "testName2";

    @Autowired
    private GenreDaoImpl dao;

    @Before
    public void init() {
        dao.deleteAll();
    }

    @Test
    public void addGenreTest() {
        addTestGenre(TEST_NAME_1);
        List<Genre> genres = dao.getAllGenres();
        assertThat(genres).isNotEmpty();
        assertThat(genres.get(0)).isNotNull().hasNoNullFieldsOrProperties();
    }

    @Test
    public void getAllGenresTest() {
        List<Genre> genres = dao.getAllGenres();
        assertThat(genres).isEmpty();

        Genre genre = addTestGenre(TEST_NAME_1);
        Genre genre2 = addTestGenre(TEST_NAME_2);

        genres = dao.getAllGenres();
        assertThat(genres)
                .hasSize(2)
                .contains(genre, genre2);
    }

    @Test
    public void getGenreByNameTest() {
        Genre genre1 = addTestGenre(TEST_NAME_1);
        Genre genre2 = dao.getGenreByName(TEST_NAME_1);
        assertThat(genre2).isEqualTo(genre1);
    }

    @Test
    public void deleteGenreTest() {
        Genre genre = addTestGenre(TEST_NAME_1);
        Genre genre2 = addTestGenre(TEST_NAME_2);
        List<Genre> genres = dao.getAllGenres();
        assertThat(genres)
                .hasSize(2)
                .contains(genre, genre2);

        dao.deleteGenre(genre);

        genres = dao.getAllGenres();
        assertThat(genres)
                .hasSize(1)
                .contains(genre2).
                doesNotContain(genre);
    }

    @Test
    public void deleteAllTest() {
        Genre genre = addTestGenre(TEST_NAME_1);
        Genre genre2 = addTestGenre(TEST_NAME_2);
        List<Genre> genres = dao.getAllGenres();
        assertThat(genres)
                .hasSize(2)
                .contains(genre, genre2);

        dao.deleteAll();

        genres = dao.getAllGenres();
        assertThat(genres).isEmpty();
    }

    private Genre addTestGenre(String testName) {
        Genre genre = new Genre();
        genre.setGenreName(testName);
        genre = dao.addGenre(genre);
        return genre;
    }

}