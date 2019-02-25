package ru.otus.spring02.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.spring02.repository.AuthorRepository;
import ru.otus.spring02.repository.BookRepository;
import ru.otus.spring02.repository.GenreRepository;
import ru.otus.spring02.model.Author;
import ru.otus.spring02.model.Book;
import ru.otus.spring02.model.Genre;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest
public class LibraryServiceTest {

    private static final String TEST_AUTHOR_1 = "testAuthor";
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
    public void getAllAuthorsNamesTest() throws Exception {
        libraryService.getAllAuthorsNames();
        verify(authorRepository).findAll();
    }

    @Test
    public void getAllGenresTest() throws Exception {
        Genre genre = new Genre();
        genre.setId("id1");
        genre.setGenreName(TEST_GENRE_1);

        Genre genre2 = new Genre();
        genre2.setId("id2");
        genre2.setGenreName(TEST_GENRE_2);

        List<Genre> genres = new ArrayList<>(Arrays.asList(genre, genre2));

        when(genreRepository.findAll()).thenReturn(genres);

        List<String> resultList = libraryService.getAllGenres();

        verify(genreRepository).findAll();
        assertThat(resultList)
                .isNotEmpty()
                .hasSize(2)
                .contains(TEST_GENRE_1, TEST_GENRE_2);
    }

    @Test
    public void getBooksByAuthorsNameWhenSuccessfulTest() throws Exception {
        Author author = new Author();
        when(authorRepository.findAuthorByName(TEST_AUTHOR_1)).thenReturn(author);

        libraryService.getBooksByAuthorsName(TEST_AUTHOR_1);

        verify(bookRepository).findAllByAuthorsId(author.getId());
    }

    @Test
    public void getBooksByAuthorsNameWhenNoAuthorTest() throws Exception {
        when(authorRepository.findAuthorByName(TEST_AUTHOR_1)).thenReturn(null);

        List<Book> books = libraryService.getBooksByAuthorsName(TEST_AUTHOR_1);

        assertThat(books).isEmpty();
    }

    @Test
    public void addNewGenreWhenSuccessfulTest() throws Exception {
        when(genreRepository.findGenreByGenreName(TEST_GENRE_1)).thenReturn(null);
        Genre genre = new Genre(TEST_GENRE_1);
        genre.setId("id");
        when(genreRepository.save(any())).thenReturn(genre);

        boolean result = libraryService.addNewGenre(genre);

        assertThat(result).isTrue();
    }

    @Test
    public void addNewGenreWhenGenreExistsTest() throws Exception {
        Genre genre = new Genre(TEST_GENRE_1);
        when(genreRepository.findGenreByGenreName(TEST_GENRE_1)).thenReturn(genre);

        boolean result = libraryService.addNewGenre(genre);

        assertThat(result).isFalse();
    }

    @Test
    public void addNewBookTestWhenSuccessful() throws Exception {
        List<Book> books = new ArrayList<>();
        when(bookRepository.findAll()).thenReturn(books);

        Genre genre = new Genre();
        when(genreRepository.findGenreByGenreName(TEST_GENRE_1)).thenReturn(genre);

        Author author = new Author();
        when(authorRepository.findAuthorByName(TEST_AUTHOR_1)).thenReturn(author);

        Book book = new Book();
        book.setId("id");
        when(bookRepository.save(any())).thenReturn(book);
    }
}