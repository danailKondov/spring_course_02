package ru.otus.spring02.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.spring02.model.Author;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by хитрый жук on 28.12.2018.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext // в т.ч. in-memory база пересоздается каждый тест
public class AuthorRepositoryTest {

    private static final String TEST_NAME_1 = "testName";
    private static final String TEST_NAME_2 = "testName2";

    @Autowired
    private AuthorRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void addNewAuthorTest() {
        Author author = new Author();
        author.setName(TEST_NAME_1);
        repository.save(author);

        List<String> authors = repository.findAllAuthorsNames();
        assertThat(authors)
                .isNotEmpty()
                .hasSize(1)
                .contains(TEST_NAME_1);
    }

    @Test
    public void getAllAuthorsNamesTest() {
        List<String> authors = repository.findAllAuthorsNames();
        assertThat(authors).isEmpty();

        addTestAuthor(TEST_NAME_1);
        addTestAuthor(TEST_NAME_2);

        authors = repository.findAllAuthorsNames();

        assertThat(authors)
                .hasSize(2)
                .contains(TEST_NAME_1, TEST_NAME_2);
    }

    @Test
    public void getAuthorByNameTest() {
        addTestAuthor(TEST_NAME_1);
        Author author = repository.findAuthorByName(TEST_NAME_1);
        assertThat(author.getName()).isEqualTo(TEST_NAME_1);
    }

    @Test
    public void deleteAuthorTest() {
        addTestAuthor(TEST_NAME_1);
        addTestAuthor(TEST_NAME_2);

        List<String> authors = repository.findAllAuthorsNames();
        assertThat(authors)
                .hasSize(2)
                .contains(TEST_NAME_1, TEST_NAME_2);

        Author author = repository.findAuthorByName(TEST_NAME_1);
        int result = repository.deleteAuthorById(author.getId());

        authors = repository.findAllAuthorsNames();
        assertThat(result > 0).isTrue();
        assertThat(authors)
                .hasSize(1)
                .contains(TEST_NAME_2)
                .doesNotContain(TEST_NAME_1);
    }

    @Test
    public void deleteAllTest() {
        addTestAuthor(TEST_NAME_1);
        addTestAuthor(TEST_NAME_2);

        List<String> authors = repository.findAllAuthorsNames();
        assertThat(authors)
                .hasSize(2)
                .contains(TEST_NAME_1, TEST_NAME_2);

        repository.deleteAll();

        authors = repository.findAllAuthorsNames();
        assertThat(authors).isEmpty();
    }

    private void addTestAuthor(String testName) {
        Author author = new Author();
        author.setName(testName);
        entityManager.persist(author);
    }

}