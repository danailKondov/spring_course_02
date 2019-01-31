package ru.otus.spring02.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.spring02.model.Book;
import ru.otus.spring02.model.Comment;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by хитрый жук on 28.12.2018.
 */
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
    private static final String TEST_USER_1 = "testUser";
    private static final String TEST_TEXT_1 = "testText";
    private static final String TEST_USER_2 = "testUser2";
    private static final String TEST_TEXT_2 = "testText2";

    @Autowired
    private BookRepository bookRepository;

    @Before
    public void init() {
        bookRepository.deleteAll();
    }

    @Test
    public void addNewBookTest() throws Exception {
        Set<String> authors = new HashSet<>();
        authors.add(TEST_AUTHOR_1);

        Book book = new Book();
        book.setTitle(TEST_TITLE_1);
        book.setGenre(TEST_GENRE_1);
        book.setAuthors(authors);

        bookRepository.save(book);

        List<Book> books = bookRepository.findAll();

        assertThat(books)
                .hasSize(1)
                .contains(book);
    }

    @Test
    public void getAllBooksTest() throws Exception {
        List<Book> books = bookRepository.findAll();
        assertThat(books).isEmpty();

        Book book1 = addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        Book book2 = addTestBookToDb(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);

        books = bookRepository.findAll();
        assertThat(books)
                .isNotEmpty()
                .hasSize(2)
                .contains(book1, book2);
    }

    @Test
    public void getBookByAuthorTest() throws Exception {
        Book expectedBook = addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        addTestBookToDb(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);
        Iterator<String> iterator = expectedBook.getAuthors().iterator();
        String author = iterator.next();

        List<Book> books = bookRepository.findBooksByAuthors(author);
        Book resultBook = books.get(0);

        assertThat(resultBook).isEqualTo(expectedBook);
    }

    @Test
    public void getBookByIdTest() throws Exception {
        Book expectedBook = addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        addTestBookToDb(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);

        String id = expectedBook.getId();
        Book resultBook = bookRepository.findBookById(id);

        assertThat(resultBook).isEqualTo(expectedBook);
    }

    @Test
    public void getBookByTitleTest() throws Exception {
        Book expectedBook1 = addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        Book expectedBook2 = addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_3, TEST_GENRE_3);
        addTestBookToDb(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);

        List<Book> resultBooks = bookRepository.findBooksByTitle(TEST_TITLE_1);

        assertThat(resultBooks)
                .isNotEmpty()
                .hasSize(2)
                .contains(expectedBook1, expectedBook2);
    }

    @Test
    public void getCommentByBookIdTest() {
        Set<String> authors = new HashSet<>();
        authors.add(TEST_AUTHOR_1);
        Book book = new Book(TEST_TITLE_1, TEST_GENRE_1, authors);
        Comment comment1 = new Comment(TEST_USER_1, TEST_TEXT_1);
        Comment comment2 = new Comment(TEST_USER_2, TEST_TEXT_2);
        Set<Comment> comments = new HashSet<>();
        comments.add(comment1);
        comments.add(comment2);
        book.setComments(comments);
        bookRepository.save(book);

        Book book1 = bookRepository.findBookWithCommentsById(book.getId());

        assertThat(book1.getComments())
                .isNotEmpty()
                .hasSize(2)
                .contains(comment1, comment2);
    }

    @Test
    public void deleteBookByIdTest() throws Exception {
        Book book1 = addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        Book book2 = addTestBookToDb(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);

        List<Book> books = bookRepository.findAll();
        assertThat(books)
                .isNotEmpty()
                .hasSize(2)
                .contains(book1, book2);

        int result = bookRepository.deleteBookById(book1.getId());

        books = bookRepository.findAll();
        assertThat(result > 0).isTrue();
        assertThat(books)
                .isNotEmpty()
                .hasSize(1)
                .contains(book2)
                .doesNotContain(book1);
    }

    @Test
    public void deleteAllTest() throws Exception {
        Book book1 = addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        Book book2 = addTestBookToDb(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);

        List<Book> books = bookRepository.findAll();
        assertThat(books)
                .isNotEmpty()
                .hasSize(2)
                .contains(book1, book2);

        bookRepository.deleteAll();

        books = bookRepository.findAll();
        assertThat(books).isEmpty();
    }

    private Book addTestBookToDb(String title, String authorName, String genreName) {
        Set<String> authors = new HashSet<>();
        authors.add(authorName);


        Book book = new Book();
        book.setTitle(title);
        book.setAuthors(authors);
        book.setGenre(genreName);

        return bookRepository.save(book);
    }
}