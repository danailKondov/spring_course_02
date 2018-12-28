package ru.otus.spring02.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.spring02.dao.AuthorDaoImpl;
import ru.otus.spring02.dao.BookDaoImpl;
import ru.otus.spring02.dao.GenreDaoImpl;
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


/**
 * Created by хитрый жук on 28.12.2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LibraryServiceTest {

    private static final String TEST_AUTHOR_1 = "testAuthor";
    private static final String TEST_GENRE_1 = "testGenre";
    private static final String TEST_GENRE_2 = "testGenre2";

    @MockBean
    private BookDaoImpl bookDao;

    @MockBean
    private AuthorDaoImpl authorDao;

    @MockBean
    private GenreDaoImpl genreDao;

    @Autowired
    private LibraryServiceImpl libraryService;

    @Before
    public void init() {
        reset(bookDao);
        reset(authorDao);
        reset(genreDao);
    }

    @Test
    public void getAllBooksTest() throws Exception {
        libraryService.getAllBooks();
        verify(bookDao).getAllBooks();
    }

    @Test
    public void getAllAuthorsNamesTest() throws Exception {
        libraryService.getAllAuthorsNames();
        verify(authorDao).getAllAuthorsNames();
    }

    @Test
    public void getAllGenresTest() throws Exception {
        Genre genre = new Genre();
        genre.setId(1L);
        genre.setGenreName(TEST_GENRE_1);

        Genre genre2 = new Genre();
        genre2.setId(2L);
        genre2.setGenreName(TEST_GENRE_2);

        List<Genre> genres = new ArrayList<>(Arrays.asList(genre, genre2));

        when(genreDao.getAllGenres()).thenReturn(genres);

        List<String> resultList = libraryService.getAllGenres();

        verify(genreDao).getAllGenres();
        assertThat(resultList)
                .isNotEmpty()
                .hasSize(2)
                .contains(TEST_GENRE_1, TEST_GENRE_2);
    }

    @Test
    public void getBooksByAuthorsNameWhenSuccessfulTest() throws Exception {
        Author author = new Author();
        when(authorDao.getAuthorByName(TEST_AUTHOR_1)).thenReturn(author);

        libraryService.getBooksByAuthorsName(TEST_AUTHOR_1);

        verify(bookDao).getBooksByAuthor(author);
    }

    @Test
    public void getBooksByAuthorsNameWhenNoAuthorTest() throws Exception {
        when(authorDao.getAuthorByName(TEST_AUTHOR_1)).thenReturn(null);

        List<Book> books = libraryService.getBooksByAuthorsName(TEST_AUTHOR_1);

        assertThat(books).isEmpty();
    }

    @Test
    public void addNewGenreWhenSuccessfulTest() throws Exception {
        when(genreDao.getGenreByName(TEST_GENRE_1)).thenReturn(null);
        Genre genre = new Genre(TEST_GENRE_1);
        genre.setId(1L);
        when(genreDao.addGenre(any())).thenReturn(genre);

        boolean result = libraryService.addNewGenre(genre);

        assertThat(result).isTrue();
    }

    @Test
    public void addNewGenreWhenGenreExistsTest() throws Exception {
        Genre genre = new Genre(TEST_GENRE_1);
        when(genreDao.getGenreByName(TEST_GENRE_1)).thenReturn(genre);

        boolean result = libraryService.addNewGenre(genre);

        assertThat(result).isFalse();
    }

    @Test
    public void addNewBookTestWhenSuccessful() throws Exception {
        List<String> titles = new ArrayList<>();
        when(bookDao.getAllTitles()).thenReturn(titles);

        Genre genre = new Genre();
        when(genreDao.getGenreByName(TEST_GENRE_1)).thenReturn(genre);

        Author author = new Author();
        when(authorDao.getAuthorByName(TEST_AUTHOR_1)).thenReturn(author);

        Book book = new Book();
        book.setId(1L);
        when(bookDao.addNewBook(any())).thenReturn(book);
    }
}