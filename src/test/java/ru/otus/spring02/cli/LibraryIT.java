package ru.otus.spring02.cli;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Shell;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.spring02.exceptions.NoBookWithSuchIdLibraryException;
import ru.otus.spring02.model.BookInfo;
import ru.otus.spring02.repository.AuthorRepository;
import ru.otus.spring02.repository.BookRepository;
import ru.otus.spring02.repository.CommentRepository;
import ru.otus.spring02.repository.GenreRepository;
import ru.otus.spring02.repository.UserRepository;
import ru.otus.spring02.model.Author;
import ru.otus.spring02.model.Book;
import ru.otus.spring02.model.Comment;
import ru.otus.spring02.model.Genre;
import ru.otus.spring02.model.User;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LibraryIT {

    private static final String TEST_TITLE_1 = "testName";
    private static final String TEST_TITLE_2 = "testName2";
    private static final String TEST_AUTHOR_1 = "testAuthor";
    private static final String TEST_AUTHOR_2 = "testAuthor2";
    private static final String TEST_GENRE_1 = "testGenre";
    private static final String TEST_GENRE_2 = "testGenre2";
    private static final String TEST_USER = "testUser";
    private static final String TEST_TEXT_1 = "testText1";
    private static final String TEST_TEXT_2 = "testText2";

    @Autowired
    private Shell shell;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void init() {
        bookRepository.deleteAll().block();
        genreRepository.deleteAll().block();
        authorRepository.deleteAll().block();
        userRepository.deleteAll().block();
        commentRepository.deleteAll().block();
    }

    @Test
    public void getAllAuthorsNamesTest() throws Exception {
        addTestAuthor(TEST_AUTHOR_1).block();
        addTestAuthor(TEST_AUTHOR_2).block();

        Object res = shell.evaluate(() -> "all-names");

        String result = res.toString();
        String expected = "[" + TEST_AUTHOR_1 + ", " + TEST_AUTHOR_2 + "]";

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void getAllBooksTest() throws Exception {
        Book book1 = addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1).block();
        Book book2 = addTestBookToDb(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2).block();

        Object res = shell.evaluate(() -> "all-books");

        String result = res.toString();

        assertThat(result).contains(
                book1.getId(), TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1,
                book2.getId(), TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);
    }

    @Test
    public void getAllGenresTest() throws Exception {
        addTestGenre(TEST_GENRE_1).block();
        addTestGenre(TEST_GENRE_2).block();

        Object res = shell.evaluate(() -> "all-genres");

        String result = res.toString();
        String expected = "[" + TEST_GENRE_1 + ", " + TEST_GENRE_2 + "]";

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void getBooksByAuthorsNameTest() throws Exception {
        addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1).block();
        Book book2 = addTestBookToDb(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2).block();

        Object res = shell.evaluate(() -> "book-of " + TEST_AUTHOR_2);

        String result = res.toString();

        assertThat(result)
                .contains(book2.getId(), TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);
    }

    @Test
    public void getAllCommentsForBookTest() throws Exception {
        Book book = addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1).block();
        BookInfo bookInfo = new BookInfo();
        bookInfo.setId(book.getId());
        bookInfo.setTitle(book.getTitle());
        User user = userRepository.save(new User(TEST_USER)).block();
        Comment comment1 = new Comment(bookInfo, user, TEST_TEXT_1);
        Comment comment2 = new Comment(bookInfo, user, TEST_TEXT_2);

        commentRepository.save(comment1).block();
        commentRepository.save(comment2).block();

        Object result = shell.evaluate(() -> "comm-by " + book.getId());

        assertThat(result.toString())
                .isNotNull()
                .contains(TEST_TEXT_1, TEST_TEXT_2);
    }

    @Test
    public void addNewBookTest() throws Exception {
        shell.evaluate(() -> "add-book " + TEST_GENRE_1 + " " + TEST_TITLE_1 + " " + TEST_AUTHOR_1);

        Author author = authorRepository.findAuthorByName(TEST_AUTHOR_1).block();
        assertThat(author).isNotNull();

        Flux<Book> books = bookRepository.findAllByAuthorsId(author.getId());
        StepVerifier
                .create(books)
                .assertNext(book -> {
                    assertThat(book.getTitle()).isEqualTo(TEST_TITLE_1);
                    assertThat(book.getGenre().getGenreName()).isEqualTo(TEST_GENRE_1);
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void addNewGenreTest() throws Exception {
        shell.evaluate(() -> "add-genre " + TEST_GENRE_1);

        Genre genre = genreRepository.findGenreByGenreName(TEST_GENRE_1).block();

        assertThat(genre).isNotNull();
    }

    @Test
    public void addNewAuthorTest() throws Exception {
        shell.evaluate(() -> "add-author " + TEST_AUTHOR_1);

        Author author = authorRepository.findAuthorByName(TEST_AUTHOR_1).block();

        assertThat(author).isNotNull();
    }

    @Test
    public void addNewCommentToBookTest() throws Exception {
        Book book = addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1).block();
        String id = book.getId();

        shell.evaluate(() -> "add-comm " + id + " " + TEST_USER + " " + TEST_TEXT_1);

        Flux<Comment> result = commentRepository.findCommentsByBook_Id(id);
        StepVerifier
                .create(result)
                .assertNext(comment -> assertThat(comment.getCommentText()).isEqualTo(TEST_TEXT_1))
                .expectComplete()
                .verify();
    }

    @Test
    public void updateBookTitleByIdTest() throws Exception {
        Book book = addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1).block();
        String id = book.getId();

        shell.evaluate(() -> "upd-title-id " + id + " " + TEST_TITLE_2);

        book = bookRepository.findBookById(book.getId()).block();
        assertThat(book.getTitle()).isEqualTo(TEST_TITLE_2);
    }

    @Test
    public void updateBookTitleByIdWhenNoBookTest() throws Exception {
        Object res = shell.evaluate(() -> "upd-title-id " + 100 + " " + TEST_TITLE_2);

        String result = res.toString();
        String expected = "ru.otus.spring02.exceptions.NoBookWithSuchIdLibraryException: 100";

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void updateCommentByIdTest() throws Exception {
        Book book = addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1).block();
        User user = userRepository.save(new User(TEST_USER)).block();
        BookInfo bookInfo = new BookInfo();
        bookInfo.setId(book.getId());
        bookInfo.setTitle(book.getTitle());
        Comment comment = new Comment(bookInfo, user, TEST_TEXT_1);
        Comment comment2 = commentRepository.save(comment).block();

        shell.evaluate(() -> "upd-comm " + comment2.getId() + " " + TEST_TEXT_2);

        Mono<Comment> updatedComment = commentRepository.findById(comment2.getId());
        StepVerifier
                .create(updatedComment)
                .assertNext(comment1 -> {
                    assertThat(comment1.getCommentText()).isEqualTo(TEST_TEXT_2);
                    assertThat(comment1.getBook().getTitle()).isEqualTo(TEST_TITLE_1);
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void deleteBookByIdTest() throws Exception {
        Book book1 = addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1).block();
        Book book2 = addTestBookToDb(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2).block();

        String id = book2.getId();
        shell.evaluate(() -> "del-book " + id);

        Flux<Book> books = bookRepository.findAll();
        StepVerifier
                .create(books)
                .assertNext(book -> assertThat(book).isEqualTo(book1))
                .expectComplete()
                .verify();
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
        Author author = addTestAuthor(TEST_AUTHOR_1).block();
        addTestAuthor(TEST_AUTHOR_2).block();

        shell.evaluate(() -> "del-auth " + author.getId());

        Flux<Author> authors = authorRepository.findAll();
        StepVerifier
                .create(authors)
                .thenAwait(Duration.ofSeconds(5))
                .assertNext(aut -> {
                    assertThat(aut.getId()).isNotNull();
                    assertThat(aut.getName()).isEqualTo(TEST_AUTHOR_2);
                })
                .expectComplete()
                .verify();
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
        addTestGenre(TEST_GENRE_1).block();
        addTestGenre(TEST_GENRE_2).block();

        shell.evaluate(() -> "del-genre " + TEST_GENRE_1);

        Flux<Genre> genres = genreRepository.findAll();
        StepVerifier.create(genres)
                .assertNext(gen -> {
                    assertThat(gen.getId()).isNotNull();
                    assertThat(gen.getGenreName()).isEqualTo(TEST_GENRE_2);
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void deleteCommentByIdTest() throws Exception {
        Book book = addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1).block();
        User user = userRepository.save(new User(TEST_USER)).block();
        BookInfo bookInfo = new BookInfo();
        bookInfo.setId(book.getId());
        bookInfo.setTitle(book.getTitle());
        Comment comment = new Comment(bookInfo, user, TEST_TEXT_1);
        Comment comment1 = commentRepository.save(comment).block();
        String id = comment1.getId();

        shell.evaluate(() -> "del-comm " + id);

        Mono<Comment> testComment = commentRepository.findById(id);
        StepVerifier
                .create(testComment)
                .verifyComplete();
    }

    private Mono<Book> addTestBookToDb(String title, String authorName, String genreName) {
        Author author = new Author();
        author.setName(authorName);
        author = authorRepository.save(author).block();
        Set<Author> authors = new HashSet<>();
        authors.add(author);

        Genre genre = addTestGenre(genreName).block();

        Book book = new Book();
        book.setTitle(title);
        book.setAuthors(authors);
        book.setGenre(genre);

        return bookRepository.save(book);
    }

    private Mono<Author> addTestAuthor(String testName) {
        Author author = new Author();
        author.setName(testName);
        return authorRepository.save(author);
    }

    private Mono<Genre> addTestGenre(String testName) {
        Genre genre = new Genre();
        genre.setGenreName(testName);
        return genreRepository.save(genre);
    }
}