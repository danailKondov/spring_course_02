package ru.otus.spring02.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.spring02.model.Author;
import ru.otus.spring02.model.Book;
import ru.otus.spring02.model.BookInfo;
import ru.otus.spring02.model.Comment;
import ru.otus.spring02.model.Genre;
import ru.otus.spring02.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataMongoTest
@DirtiesContext
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
        commentRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void addCommentTest() {
        Book book = addTestBookToDb(TEST_TITLE, TEST_AUTHOR, TEST_GENRE);
        User user = userRepository.save(new User(TEST_USER));
        BookInfo bookInfo = new BookInfo();
        bookInfo.setId(book.getId());
        bookInfo.setTitle(book.getTitle());
        Comment comment = new Comment(bookInfo, user, TEST_TEXT_1);

        commentRepository.save(comment);

        Optional<Comment> testComment = commentRepository.findById(comment.getId());
        assertThat(testComment.isPresent()).isTrue();
    }

    @Test
    public void getCommentByBookIdTest() {
        Book book = addTestBookToDb(TEST_TITLE, TEST_AUTHOR, TEST_GENRE);
        User user = userRepository.save(new User(TEST_USER));
        BookInfo bookInfo = new BookInfo();
        bookInfo.setId(book.getId());
        bookInfo.setTitle(book.getTitle());
        Comment comment1 = new Comment(bookInfo, user, TEST_TEXT_1);
        Comment comment2 = new Comment(bookInfo, user, TEST_TEXT_2);

        commentRepository.save(comment1);
        commentRepository.save(comment2);

        List<String> comments = commentRepository.findCommentsByBook_Id(book.getId())
                .stream()
                .map(Comment::getCommentText)
                .collect(Collectors.toList());

        assertThat(comments)
                .isNotEmpty()
                .hasSize(2)
                .contains(TEST_TEXT_1, TEST_TEXT_2);
    }

    @Test
    public void deleteCommentByIdWhenSuccessfulTest() {
        Book book = addTestBookToDb(TEST_TITLE, TEST_AUTHOR, TEST_GENRE);
        User user = userRepository.save(new User(TEST_USER));
        BookInfo bookInfo = new BookInfo();
        bookInfo.setId(book.getId());
        bookInfo.setTitle(book.getTitle());
        Comment comment = new Comment(bookInfo, user, TEST_TEXT_1);
        commentRepository.save(comment);
        String id = comment.getId();

        int result = commentRepository.deleteCommentById(id);

        assertThat(result > 0).isTrue();
        Optional<Comment> testComment = commentRepository.findById(comment.getId());
        assertThat(testComment.isPresent()).isFalse();
    }

    @Test
    public void deleteCommentByIdWhenNoCommentTest() throws Exception {
        int result = commentRepository.deleteCommentById("no_id");
        assertThat(result > 0).isFalse();
    }

    private Book addTestBookToDb(String title, String authorName, String genreName) {
        Author author = new Author();
        author.setName(authorName);
        author = authorRepository.save(author);
        Set<Author> authors = new HashSet<>();
        authors.add(author);

        Genre genre = addTestGenre(genreName);

        Book book = new Book();
        book.setTitle(title);
        book.setAuthors(authors);
        book.setGenre(genre);

        return bookRepository.save(book);
    }

    private Genre addTestGenre(String testName) {
        Genre genre = new Genre();
        genre.setGenreName(testName);
        return genreRepository.save(genre);
    }
}