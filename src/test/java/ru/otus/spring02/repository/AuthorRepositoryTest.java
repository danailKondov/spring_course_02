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

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataMongoTest
public class AuthorRepositoryTest {

    private static final String TEST_NAME_1 = "testName";
    private static final String TEST_NAME_2 = "testName2";

    @Autowired
    private AuthorRepository repository;

    @Before
    public void init() {
        repository.deleteAll().block();
    }

    @Test
    public void addNewAuthorTest() {
        Author author = new Author();
        author.setName(TEST_NAME_1);
        Mono<Author> authorMono = repository.save(author);
        assertThat(authorMono.block()).isNotNull();

        Flux<Author> authors = repository.findAll();
        StepVerifier
                .create(authors)
                .assertNext(aut -> {
                    assertThat(aut.getId()).isNotNull();
                    assertThat(aut.getName()).isEqualTo(TEST_NAME_1);
                })
                .thenAwait(Duration.ofSeconds(5))
                .expectComplete()
                .verify();
    }

    @Test
    public void getAllAuthorsTest() {

        Author author1 = addTestAuthor(TEST_NAME_1).block();
        Author author2 = addTestAuthor(TEST_NAME_2).block();

        Flux<Author> authorFlux = repository.findAll();
        StepVerifier
                .create(authorFlux)
                .expectNext(author1, author2)
                .thenAwait(Duration.ofSeconds(5))
                .verifyComplete();
    }

    @Test
    public void getAuthorByNameTest() {
        Mono<Author> authorMono = addTestAuthor(TEST_NAME_1);
        StepVerifier
                .create(authorMono.map(Author::getName))
                .expectNext(TEST_NAME_1).verifyComplete();

        authorMono = repository.findAuthorByName(TEST_NAME_1);
        StepVerifier
                .create(authorMono.map(Author::getName))
                .expectNext(TEST_NAME_1).verifyComplete();
    }

    @Test
    public void deleteAuthorTest() {
        Author author1 = addTestAuthor(TEST_NAME_1).block();
        Author author2 = addTestAuthor(TEST_NAME_2).block();

        Flux<Author> authorFlux = repository.findAll();
        StepVerifier
                .create(authorFlux)
                .expectNext(author1, author2)
                .thenAwait(Duration.ofSeconds(5))
                .verifyComplete();

        Mono<Long> result = repository.deleteAuthorById(author1.getId());
        assertThat(result.block() > 0);

        Flux<Author> authors = repository.findAll();
        StepVerifier
                .create(authors)
                .thenAwait(Duration.ofSeconds(5))
                .assertNext(aut -> {
                    assertThat(aut.getId()).isNotNull();
                    assertThat(aut.getName()).isEqualTo(TEST_NAME_2);
                })
                .thenAwait(Duration.ofSeconds(5))
                .expectComplete()
                .verify();
    }

    @Test
    public void deleteAllTest() {
        Author author1 = addTestAuthor(TEST_NAME_1).block();
        Author author2 = addTestAuthor(TEST_NAME_2).block();

        Flux<Author> authorFlux = repository.findAll();
        StepVerifier
                .create(authorFlux)
                .expectNext(author1, author2)
                .verifyComplete();

        repository.deleteAll().block();

        List<Author> authors = repository.findAll().collectList().block();
        assertThat(authors).isEmpty();
    }

    private Mono<Author> addTestAuthor(String testName) {
        Author author = new Author();
        author.setName(testName);
        return repository.save(author);
    }

}