package ru.otus.spring02.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.spring02.model.Author;
import ru.otus.spring02.model.Book;
import ru.otus.spring02.model.Comment;
import ru.otus.spring02.model.Genre;
import ru.otus.spring02.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by хитрый жук on 15.01.2019.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext
@Import({CommentDaoImpl.class, UserDaoImpl.class})
public class CommentDaoTest {

    private static final String TEST_USER = "testUser";
    private static final String TEST_TEXT_1 = "testText";
    private static final String TEST_TEXT_2 = "testText2";
    private static final String TEST_TITLE = "testName";
    private static final String TEST_AUTHOR = "testAuthor";
    private static final String TEST_GENRE = "testGenre";

    @Autowired
    private CommentDaoImpl commentDao;

    @Autowired
    private UserDaoImpl userDao;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void addCommentTest() {
        Book book = addTestBookToDb(TEST_TITLE, TEST_AUTHOR, TEST_GENRE);
        User user = userDao.addUser(new User(TEST_USER));
        Comment comment = new Comment(book, user, TEST_TEXT_1);

        commentDao.addComment(comment);

        Comment testComment = commentDao.getCommentById(comment.getId());
        assertThat(testComment).isNotNull();
    }

    @Test
    public void getCommentByBookIdTest() {
        Book book = addTestBookToDb(TEST_TITLE, TEST_AUTHOR, TEST_GENRE);
        User user = userDao.addUser(new User(TEST_USER));
        Comment comment1 = new Comment(book, user, TEST_TEXT_1);
        Comment comment2 = new Comment(book, user, TEST_TEXT_2);

        commentDao.addComment(comment1);
        commentDao.addComment(comment2);

        List<String> coments = commentDao.getCommentsByBookId(book.getId());

        assertThat(coments)
                .isNotEmpty()
                .hasSize(2)
                .contains(TEST_TEXT_1, TEST_TEXT_2);
    }

    @Test
    public void deleteCommentByIdWhenSuccessfulTest() {
        Book book = addTestBookToDb(TEST_TITLE, TEST_AUTHOR, TEST_GENRE);
        User user = userDao.addUser(new User(TEST_USER));
        Comment comment = new Comment(book, user, TEST_TEXT_1);
        commentDao.addComment(comment);
        Long id = comment.getId();

        commentDao.deleteCommentById(id);

        Comment testComment = commentDao.getCommentById(id);
        assertThat(testComment).isNull();
    }

    @Test
    public void deleteCommentByIdWhenNoCommentTest() {
        boolean result = commentDao.deleteCommentById(1000L);
        assertThat(result).isFalse();
    }

    private Book addTestBookToDb(String title, String authorName, String genreName) {
        Author author = new Author();
        author.setName(authorName);
        author = entityManager.persist(author);
        Set<Author> authors = new HashSet<>();
        authors.add(author);

        Genre genre = addTestGenre(genreName);

        Book book = new Book();
        book.setTitle(title);
        book.setAuthors(authors);
        book.setGenre(genre);

        return entityManager.persist(book);
    }

    private Genre addTestGenre(String testName) {
        Genre genre = new Genre();
        genre.setGenreName(testName);
        return entityManager.persist(genre);
    }
}