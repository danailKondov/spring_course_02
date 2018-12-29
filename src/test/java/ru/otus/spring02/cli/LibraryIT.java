package ru.otus.spring02.cli;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Shell;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.spring02.dao.AuthorDaoImpl;
import ru.otus.spring02.dao.BookDaoImpl;
import ru.otus.spring02.dao.GenreDaoImpl;
import ru.otus.spring02.model.Author;
import ru.otus.spring02.model.Book;
import ru.otus.spring02.model.Genre;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by хитрый жук on 28.12.2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LibraryIT {

    private static final String TEST_TITLE_1 = "testName";
    private static final String TEST_TITLE_2 = "testName2";
    private static final String TEST_AUTHOR_1 = "testAuthor";
    private static final String TEST_AUTHOR_2 = "testAuthor2";
    private static final String TEST_GENRE_1 = "testGenre";
    private static final String TEST_GENRE_2 = "testGenre2";

    @Autowired
    private Shell shell;

    @Autowired
    private BookDaoImpl bookDao;

    @Autowired
    private AuthorDaoImpl authorDao;

    @Autowired
    private GenreDaoImpl genreDao;

    @Before
    public void init() {
        genreDao.deleteAll();
        authorDao.deleteAll();
        bookDao.deleteAll();
    }

    @Test
    public void getAllAuthorsNamesTest() throws Exception {
        addTestAuthor(TEST_AUTHOR_1);
        addTestAuthor(TEST_AUTHOR_2);

        Object res = shell.evaluate(() -> "all-names");

        String result = res.toString();
        String expected = "[" + TEST_AUTHOR_1 + ", " + TEST_AUTHOR_2 + "]";

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void getAllBooksTest() throws Exception {
        Book book1 = addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        Book book2 = addTestBookToDb(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);

        Object res = shell.evaluate(() -> "all-books");

        String result = res.toString();
        String expected = "Book idAuthor(s)  Title    Genre     \n";

        assertThat(result).contains(expected,
                String.valueOf(book1.getId()), TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1,
                String.valueOf(book2.getId()), TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);
    }

    @Test
    public void getAllGenresTest() throws Exception {
        addTestGenre(TEST_GENRE_1);
        addTestGenre(TEST_GENRE_2);

        Object res = shell.evaluate(() -> "all-genres");

        String result = res.toString();
        String expected = "[" + TEST_GENRE_1 + ", " + TEST_GENRE_2 + "]";

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void getBooksByAuthorsNameTest() throws Exception {
        addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        Book book2 = addTestBookToDb(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);

        Object res = shell.evaluate(() -> "book-of " + TEST_AUTHOR_2);

        String result = res.toString();
        String expected = "Book idAuthor(s)  Title    Genre     \n";

        assertThat(result).contains(expected,
                String.valueOf(book2.getId()), TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);
    }

    @Test
    public void addNewBookOneAuthorWhenSuccessfulTest() throws Exception {
        shell.evaluate(() -> "add-book " + TEST_GENRE_1 + " " + TEST_TITLE_1 + " " + TEST_AUTHOR_1);

        Author author = authorDao.getAuthorByName(TEST_AUTHOR_1);
        assertThat(author).isNotNull();

        Book book = bookDao.getBooksByAuthor(author).get(0);

        assertThat(book.getGenre().getGenreName()).isEqualTo(TEST_GENRE_1);
        assertThat(book.getTitle()).isEqualTo(TEST_TITLE_1);
    }

    @Test
    public void addNewBookOneAuthorWhenAlreadyExistsTest() throws Exception {
        addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);

        Object res = shell.evaluate(() -> "add-book " + TEST_GENRE_1 + " " + TEST_TITLE_1 + " " + TEST_AUTHOR_1);

        String result = res.toString();
        String expected = "Book already exists";

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void addNewGenreWhenSuccessfulTest() throws Exception {
        shell.evaluate(() -> "add-genre " + TEST_GENRE_1);

        Genre genre = genreDao.getGenreByName(TEST_GENRE_1);

        assertThat(genre).isNotNull();
    }

    @Test
    public void addNewGenreWhenAlreadyExistsTest() throws Exception {
        addTestGenre(TEST_GENRE_1);

        Object res = shell.evaluate(() -> "add-genre " + TEST_GENRE_1);

        String result = res.toString();
        String expected = "Genre already exists";

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void addNewAuthorTest() throws Exception {
        shell.evaluate(() -> "add-author " + TEST_AUTHOR_1);

        Author author = authorDao.getAuthorByName(TEST_AUTHOR_1);

        assertThat(author).isNotNull();
    }

    @Test
    public void updateBookTitleByIdTest() throws Exception {
        Book book = addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        Long id = book.getId();

        shell.evaluate(() -> "upd-title-id " + id + " " + TEST_TITLE_2);

        book = bookDao.getBookById(book.getId());
        assertThat(book.getTitle()).isEqualTo(TEST_TITLE_2);
    }

    @Test
    public void updateBookTitleByIdWhenNoBookTest() throws Exception {
        Object res = shell.evaluate(() -> "upd-title-id " + 100 + " " + TEST_TITLE_2);

        String result = res.toString();
        String expected = "Book doesn't exist";

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void deleteBookByIdTest() throws Exception {
        Book book1 = addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        Book book2 = addTestBookToDb(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);

        Long id = book2.getId();
        shell.evaluate(() -> "del-book " + id);

        List<Book> books = bookDao.getAllBooks();
        assertThat(books)
                .hasSize(1)
                .contains(book1)
                .doesNotContain(book2);
    }

    @Test
    public void deleteBookByIdWhenNoBookTest() throws Exception {
        Object res = shell.evaluate(() -> "del-book " + 100);

        String result = res.toString();
        String expected = "Book doesn't exist";

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void deleteAuthorByIdTest() throws Exception {
        Author author = addTestAuthor(TEST_AUTHOR_1);
        addTestAuthor(TEST_AUTHOR_2);

        shell.evaluate(() -> "del-auth " + author.getId());

        List<String> authors = authorDao.getAllAuthorsNames();
        assertThat(authors)
                .hasSize(1)
                .contains(TEST_AUTHOR_2)
                .doesNotContain(TEST_AUTHOR_1);
    }

    @Test
    public void deleteAuthorByIdWhenNoAuthorTest() throws Exception {
        Object res = shell.evaluate(() -> "del-auth " + 100);

        String result = res.toString();
        String expected = "Author doesn't exist";

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void deleteGenreTest() throws Exception {
        Genre genre1 = addTestGenre(TEST_GENRE_1);
        Genre genre2 = addTestGenre(TEST_GENRE_2);

        shell.evaluate(() -> "del-genre " + TEST_GENRE_1);

        List<Genre> genres = genreDao.getAllGenres();
        assertThat(genres)
                .hasSize(1)
                .contains(genre2)
                .doesNotContain(genre1);
    }

    private Book addTestBookToDb(String title, String authorName, String genreName) {
        Author author = new Author();
        author.setName(authorName);
        author = authorDao.addNewAuthor(author);
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Genre genre = addTestGenre(genreName);

        Book book = new Book();
        book.setTitle(title);
        book.setAuthors(authors);
        book.setGenre(genre);

        return bookDao.addNewBook(book);
    }

    private Author addTestAuthor(String testName) {
        Author author = new Author();
        author.setName(testName);
        return authorDao.addNewAuthor(author);
    }

    private Genre addTestGenre(String testName) {
        Genre genre = new Genre();
        genre.setGenreName(testName);
        genre = genreDao.addGenre(genre);
        return genre;
    }
}