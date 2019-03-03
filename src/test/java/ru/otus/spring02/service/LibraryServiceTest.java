package ru.otus.spring02.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import ru.otus.spring02.repository.AuthorRepository;
import ru.otus.spring02.repository.BookRepository;
import ru.otus.spring02.repository.GenreRepository;
import ru.otus.spring02.model.Genre;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest
public class LibraryServiceTest {

    private static final String TEST_GENRE_1 = "testGenre";
    private static final String TEST_GENRE_2 = "testGenre2";

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @Autowired
    private LibraryServiceImpl libraryService;

    @Before
    public void init() {
        reset(bookRepository);
        reset(authorRepository);
        reset(genreRepository);
    }

    @Test
    public void getAllBooksTest() throws Exception {
        libraryService.getAllBooks();
        verify(bookRepository).findAll();
    }

    @Test
    public void getAllGenresTest() throws Exception {
        Genre genre = new Genre();
        genre.setId("id1");
        genre.setGenreName(TEST_GENRE_1);

        Genre genre2 = new Genre();
        genre2.setId("id2");
        genre2.setGenreName(TEST_GENRE_2);

        Flux<Genre> genres = Flux.just(genre, genre2);

        when(genreRepository.findAll()).thenReturn(genres);

        Flux<String> resultList = libraryService.getAllGenres();

        verify(genreRepository).findAll();
        StepVerifier
                .create(resultList)
                .expectNextCount(2)
                .expectComplete()
                .verify();
    }
}