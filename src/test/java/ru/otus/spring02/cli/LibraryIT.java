package ru.otus.spring02.cli;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Shell;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.spring02.model.Comment;
import ru.otus.spring02.repository.BookRepository;
import ru.otus.spring02.model.Book;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    private static final String TEST_USER_1 = "testUser";
    private static final String TEST_USER_2 = "testUser2";
    private static final String TEST_TEXT_1 = "testText1";
    private static final String TEST_TEXT_2 = "testText2";

    @Autowired
    private Shell shell;

    @Autowired
    private BookRepository bookRepository;

    @Before
    public void init() {
        bookRepository.deleteAll();
    }

    @Test
    public void getAllAuthorsNamesTest() throws Exception {
        addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        addTestBookToDb(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);

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

        assertThat(result).contains("Book id", "Author(s)", "Title", "Genre",
                String.valueOf(book1.getId()), TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1,
                String.valueOf(book2.getId()), TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);
    }

    @Test
    public void getAllGenresTest() throws Exception {
        addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        addTestBookToDb(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);

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

        assertThat(result).contains("Book id", "Author(s)", "Title", "Genre",
                String.valueOf(book2.getId()), TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);
    }

    @Test
    public void getAllCommentsForBookTest() throws Exception {
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

        Object result = shell.evaluate(() -> "comm-by " + book.getId());

        assertThat(result.toString())
                .isNotNull()
                .contains(TEST_TEXT_1, TEST_TEXT_2);
    }


    @Test
    public void addNewCommentToBookTest() throws Exception {
        Book book = addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        String id = book.getId();

        shell.evaluate(() -> "add-comm " + id + " " + TEST_USER_1 + " " + TEST_TEXT_1);

        Book result = bookRepository.findBookWithCommentsById(id);
        Comment comment = result.getComments().iterator().next();
        assertThat(comment.getCommentText())
                .isNotNull()
                .contains(TEST_TEXT_1);
    }

    @Test
    public void updateBookTitleByIdTest() throws Exception {
        Book book = addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        String id = book.getId();

        shell.evaluate(() -> "upd-title-id " + id + " " + TEST_TITLE_2);

        book = bookRepository.findBookById(book.getId());
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

        String id = book2.getId();
        shell.evaluate(() -> "del-book " + id);

        List<Book> books = bookRepository.findAll();
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

//    @Test
//    public void deleteAuthorByIdTest() throws Exception {
//        Author author = addTestAuthor(TEST_AUTHOR_1);
//        addTestAuthor(TEST_AUTHOR_2);
//
//        shell.evaluate(() -> "del-auth " + author.getId());
//
//        List<String> authors = authorRepository.findAllAuthorsNames();
//        assertThat(authors)
//                .hasSize(1)
//                .contains(TEST_AUTHOR_2)
//                .doesNotContain(TEST_AUTHOR_1);
//    }
//
//    @Test
//    public void deleteAuthorByIdWhenNoAuthorTest() throws Exception {
//        Object res = shell.evaluate(() -> "del-auth " + 100);
//
//        String result = res.toString();
//        String expected = "Author doesn't exist";
//
//        assertThat(result).isEqualTo(expected);
//    }
//
//    @Test
//    public void deleteGenreTest() throws Exception {
//        Genre genre1 = addTestGenre(TEST_GENRE_1);
//        Genre genre2 = addTestGenre(TEST_GENRE_2);
//
//        shell.evaluate(() -> "del-genre " + TEST_GENRE_1);
//
//        List<Genre> genres = genreRepository.findAll();
//        assertThat(genres)
//                .hasSize(1)
//                .contains(genre2)
//                .doesNotContain(genre1);
//    }
//
//    @Test
//    public void deleteCommentByIdTest() throws Exception {
//        Book book = addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
//        User user = userRepository.save(new User(TEST_USER));
//        Comment comment = new Comment(book, user, TEST_TEXT_1);
//        commentRepository.save(comment);
//        Long id = comment.getId();
//
//        shell.evaluate(() -> "del-comm " + id);
//
//        Optional<Comment> testComment = commentRepository.findById(id);
//        assertThat(testComment.isPresent()).isFalse();
//    }

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