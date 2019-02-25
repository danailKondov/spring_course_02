package ru.otus.spring02.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.spring02.model.Author;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataMongoTest
@DirtiesContext // в т.ч. in-memory база пересоздается каждый тест
public class AuthorRepositoryTest {

    private static final String TEST_NAME_1 = "testName";
    private static final String TEST_NAME_2 = "testName2";

    @Autowired
    private AuthorRepository repository;

    @Before
    public void init() {
        repository.deleteAll();
    }

    @Test
    public void addNewAuthorTest() {
        Author author = new Author();
        author.setName(TEST_NAME_1);
        repository.save(author);

        List<String> authors = getAllAuthorsNames();

        assertThat(authors)
                .isNotEmpty()
                .hasSize(1)
                .contains(TEST_NAME_1);
    }

    private List<String> getAllAuthorsNames() {
        return repository.findAll()
                .stream()
                .map(Author::getName)
                .collect(Collectors.toList());
    }

    @Test
    public void getAllAuthorsNamesTest() {
        List<String> authors = getAllAuthorsNames();
        assertThat(authors).isEmpty();

        addTestAuthor(TEST_NAME_1);
        addTestAuthor(TEST_NAME_2);

        authors = getAllAuthorsNames();

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

        List<String> authors = getAllAuthorsNames();
        assertThat(authors)
                .hasSize(2)
                .contains(TEST_NAME_1, TEST_NAME_2);

        Author author = repository.findAuthorByName(TEST_NAME_1);
        int result = repository.deleteAuthorById(author.getId());

        authors = getAllAuthorsNames();
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

        List<String> authors = getAllAuthorsNames();
        assertThat(authors)
                .hasSize(2)
                .contains(TEST_NAME_1, TEST_NAME_2);

        repository.deleteAll();

        authors = getAllAuthorsNames();
        assertThat(authors).isEmpty();
    }

    private void addTestAuthor(String testName) {
        Author author = new Author();
        author.setName(testName);
        repository.save(author);
    }

}