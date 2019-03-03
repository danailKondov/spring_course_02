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
import ru.otus.spring02.model.User;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataMongoTest
public class UserRepositoryTest {

    private static final String TEST_USER_1 = "testUser";
    private static final String TEST_USER_2 = "testUser2";

    @Autowired
    private UserRepository userRepository;

    @Before
    public void init() {
        userRepository.deleteAll().block();
    }

    @Test
    public void addUserTest() throws Exception {
        createTestUser(TEST_USER_1).block();

        Flux<User> userFlux = userRepository.findAll();
        StepVerifier
                .create(userFlux)
                .assertNext(user1 -> {
                    assertThat(user1.getId()).isNotNull();
                    assertThat(user1.getUserName()).isEqualTo(TEST_USER_1);
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void getUserByNameTest() throws Exception {
        createTestUser(TEST_USER_1).block();
        createTestUser(TEST_USER_2).block();

        Mono<User> testUser = userRepository.findUserByUserName(TEST_USER_2);

        StepVerifier
                .create(testUser)
                .assertNext(user1 -> {
                    assertThat(user1.getId()).isNotNull();
                    assertThat(user1.getUserName()).isEqualTo(TEST_USER_2);
                })
                .expectComplete()
                .verify();
    }

    private Mono<User> createTestUser(String name) {
        User user = new User(name);
        return userRepository.save(user);
    }
}