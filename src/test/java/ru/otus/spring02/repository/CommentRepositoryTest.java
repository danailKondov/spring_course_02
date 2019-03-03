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
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataMongoTest
public class CommentRepositoryTest {

    private static final String TEST_USER = "testUser";
    private static final String TEST_TEXT_1 = "testText";
    private static final String TEST_TEXT_2 = "testText2";
    private static final String TEST_TITLE = "testName";
    private static final String TEST_AUTHOR = "testAuthor";
    private static final String TEST_GENRE = "testGenre";

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Before
    public void init() {
        commentRepository.deleteAll().block();
        userRepository.deleteAll().block();
    }

    @Test
    public void addCommentTest() {
        Book book = addTestBookToDb(TEST_TITLE, TEST_AUTHOR, TEST_GENRE).block();
        User user = userRepository.save(new User(TEST_USER)).block();
        BookInfo bookInfo = new BookInfo();
        bookInfo.setId(book.getId());
        bookInfo.setTitle(book.getTitle());
        Comment comment = new Comment(bookInfo, user, TEST_TEXT_1);

        Comment comment2 = commentRepository.save(comment).block();

        Mono<Comment> testComment = commentRepository.findById(comment2.getId());
        StepVerifier
                .create(testComment)
                .assertNext(comment1 -> assertThat(comment1).isEqualTo(comment))
                .expectComplete()
                .verify();
    }

    @Test
    public void getCommentByBookIdTest() {
        Book book = addTestBookToDb(TEST_TITLE, TEST_AUTHOR, TEST_GENRE).block();
        User user = userRepository.save(new User(TEST_USER)).block();
        BookInfo bookInfo = new BookInfo();
        bookInfo.setId(book.getId());
        bookInfo.setTitle(book.getTitle());
        Comment comment1 = new Comment(bookInfo, user, TEST_TEXT_1);
        Comment comment2 = new Comment(bookInfo, user, TEST_TEXT_2);

        commentRepository.save(comment1).block();
        commentRepository.save(comment2).block();

        Flux<String> comments = commentRepository.findCommentsByBook_Id(book.getId()).map(Comment::getCommentText);
        StepVerifier.create(comments)
                .expectNext(TEST_TEXT_1, TEST_TEXT_2)
                .expectComplete()
                .verify();
    }

    @Test
    public void updateTest() {
        Book book = addTestBookToDb(TEST_TITLE, TEST_AUTHOR, TEST_GENRE).block();
        User user = userRepository.save(new User(TEST_USER)).block();
        BookInfo bookInfo = new BookInfo();
        bookInfo.setId(book.getId());
        bookInfo.setTitle(book.getTitle());
        Comment comment = new Comment(bookInfo, user, TEST_TEXT_1);

        Comment comment1 = commentRepository.save(comment).block();
        comment1.setCommentText(TEST_TEXT_2);
        Comment comment3 = commentRepository.save(comment1).block();

        assertThat(comment3.getId()).isEqualTo(comment1.getId());
        assertThat(comment3.getCommentText()).isEqualTo(TEST_TEXT_2);
    }

    @Test
    public void deleteCommentByIdWhenSuccessfulTest() {
        Book book = addTestBookToDb(TEST_TITLE, TEST_AUTHOR, TEST_GENRE).block();
        User user = userRepository.save(new User(TEST_USER)).block();
        BookInfo bookInfo = new BookInfo();
        bookInfo.setId(book.getId());
        bookInfo.setTitle(book.getTitle());
        Comment comment = new Comment(bookInfo, user, TEST_TEXT_1);
        Comment comment2 = commentRepository.save(comment).block();
        String id = comment2.getId();

        Mono<Long> result = commentRepository.deleteCommentById(id);
        assertThat(result.block() > 0).isTrue();

        Mono<Comment> testComment = commentRepository.findById(comment.getId());
        StepVerifier
                .create(testComment)
                .expectComplete()
                .verify();
    }

    @Test
    public void deleteCommentByIdWhenNoCommentTest() throws Exception {
        Long result = commentRepository.deleteCommentById("no_id").block();
        assertThat(result > 0).isFalse();
    }

    @Test
    public void deleteCommentByBookIdTest() {
        Book book = addTestBookToDb(TEST_TITLE, TEST_AUTHOR, TEST_GENRE).block();
        User user = userRepository.save(new User(TEST_USER)).block();
        BookInfo bookInfo = new BookInfo();
        bookInfo.setId(book.getId());
        bookInfo.setTitle(book.getTitle());
        Comment comment = new Comment(bookInfo, user, TEST_TEXT_1);
       commentRepository.save(comment).block();

        Mono<Long> result = commentRepository.deleteCommentByBook_Id(bookInfo.getId());
        assertThat(result.block() > 0).isTrue();

        Mono<Comment> testComment = commentRepository.findById(comment.getId());
        StepVerifier
                .create(testComment)
                .expectComplete()
                .verify();
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

    private Mono<Genre> addTestGenre(String testName) {
        Genre genre = new Genre();
        genre.setGenreName(testName);
        return genreRepository.save(genre);
    }
}