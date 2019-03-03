package ru.otus.spring02.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.spring02.model.Author;
import ru.otus.spring02.model.Book;
import ru.otus.spring02.model.BookInfo;
import ru.otus.spring02.model.Comment;
import ru.otus.spring02.model.Genre;
import ru.otus.spring02.model.User;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataMongoTest
public class BookRepositoryTest {

    private static final String TEST_TITLE_1 = "testName";
    private static final String TEST_TITLE_2 = "testName2";
    private static final String TEST_AUTHOR_1 = "testAuthor";
    private static final String TEST_AUTHOR_2 = "testAuthor2";
    private static final String TEST_GENRE_1 = "testGenre";
    private static final String TEST_GENRE_2 = "testGenre2";
    private static final String TEST_GENRE_3 = "testGenre3";
    private static final String TEST_AUTHOR_3 = "testAuthor3";

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
        authorRepository.deleteAll().block();
        genreRepository.deleteAll().block();
    }

    @Test
    public void addNewBookTest() throws Exception {
        addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1).block();

        Flux<Book> books = bookRepository.findAll();

        StepVerifier
                .create(books)
                .assertNext(book -> {
                    assertThat(book.getId()).isNotNull();
                    assertThat(book.getTitle()).isEqualTo(TEST_TITLE_1);
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void getAllBooksTest() throws Exception {
        Flux<Book> books = bookRepository.findAll();
        StepVerifier
                .create(books)
                .verifyComplete();

        Book book1 = addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1).block();
        Book book2 = addTestBookToDb(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2).block();

        Flux<Book> books2 = bookRepository.findAll();
        StepVerifier
                .create(books2)
                .expectNext(book1, book2)
                .expectComplete()
                .verify();
    }

    @Test
    public void getAllTitlesTest() throws Exception {
        addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1).block();
        addTestBookToDb(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2).block();

        Flux<String> titles = bookRepository.findAll().map(Book::getTitle);

        StepVerifier
                .create(titles)
                .expectNext(TEST_TITLE_1, TEST_TITLE_2)
                .expectComplete()
                .verify();
    }

    @Test
    public void getBookByAuthorTest() throws Exception {
        Book expectedBook = addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1).block();
        addTestBookToDb(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2).block();
        Iterator<Author> iterator = expectedBook.getAuthors().iterator();
        Author author = iterator.next();

        Flux<Book> books = bookRepository.findAllByAuthorsId(author.getId());

        Flux<Book> booksAll = bookRepository.findAll();
        StepVerifier
                .create(booksAll)
                .expectNextCount(2)
                .expectComplete()
                .verify();

        StepVerifier
                .create(books)
                .assertNext(book -> assertThat(book).isEqualTo(expectedBook))
                .expectComplete()
                .verify();
    }

    @Test
    public void getBookByIdTest() throws Exception {
        Book expectedBook = addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1).block();
        addTestBookToDb(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2).block();

        String id = expectedBook.getId();
        Mono<Book> resultBook = bookRepository.findBookById(id);

        StepVerifier
                .create(resultBook)
                .assertNext(book -> assertThat(book).isEqualTo(expectedBook))
                .expectComplete()
                .verify();
    }

    @Test
    public void getBookByTitleTest() throws Exception {
        Book expectedBook1 = addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1).block();
        Book expectedBook2 = addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_3, TEST_GENRE_3).block();
        addTestBookToDb(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2).block();

        Flux<Book> resultBooks = bookRepository.findBooksByTitle(TEST_TITLE_1);
        StepVerifier
                .create(resultBooks)
                .expectNext(expectedBook1, expectedBook2)
                .expectComplete()
                .verify();
    }

    @Test
    public void deleteBookByIdTest() throws Exception {
        Book book1 = addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1).block();
        Book book2 = addTestBookToDb(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2).block();

        User user = userRepository.save(new User("user")).block();
        BookInfo bookInfo = new BookInfo();
        bookInfo.setId(book1.getId());
        bookInfo.setTitle(book1.getTitle());
        Comment comment = new Comment(bookInfo, user, "test");
        commentRepository.save(comment).block();

        Flux<Book> books = bookRepository.findAll();
        StepVerifier
                .create(books)
                .expectNext(book1, book2)
                .expectComplete()
                .verify();

        Long result = bookRepository.deleteBookById(book1.getId()).block();
        assertThat(result > 0).isTrue();

        Flux<Book> books2 = bookRepository.findAll();
        StepVerifier
                .create(books2)
                .assertNext(book -> assertThat(book).isEqualTo(book2))
                .expectComplete()
                .verify();

//        Flux<Comment> commentFlux = commentRepository.findCommentsByBook_Id(book1.getId());
//        StepVerifier.create(commentFlux).verifyComplete();
    }

    @Test
    public void deleteAllTest() throws Exception {
        Book book1 = addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1).block();
        Book book2 = addTestBookToDb(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2).block();

        Flux<Book> books = bookRepository.findAll();
        StepVerifier
                .create(books)
                .expectNext(book1, book2)
                .expectComplete()
                .verify();

        bookRepository.deleteAll().block();

        Flux<Book> books2 = bookRepository.findAll();
        StepVerifier
                .create(books2)
                .verifyComplete();
    }

    private Mono<Book> addTestBookToDb(String title, String authorName, String genreName) {
        Author author = new Author();
        author.setName(authorName);
        Mono<Author> authorMono = authorRepository.save(author);
        Set<Author> authors = new HashSet<>();
        authors.add(authorMono.block());

        Genre genre = addTestGenre(genreName).block();

        Book book = new Book();
        book.setTitle(title);
        book.setAuthors(authors);
        book.setGenre(genre);

        return bookRepository.save(book);
    }

    private Mono<Genre> addTestGenre(String testName) {
        Genre genre = new Genre();
        genre.setGenreName(testName);
        return genreRepository.save(genre);
    }
}