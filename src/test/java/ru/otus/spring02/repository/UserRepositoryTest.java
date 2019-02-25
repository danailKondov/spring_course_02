package ru.otus.spring02.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.spring02.model.User;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataMongoTest
@DirtiesContext
public class UserRepositoryTest {

    private static final String TEST_USER_1 = "testUser";
    private static final String TEST_USER_2 = "testUser2";

    @Autowired
    private UserRepository userRepository;

    @Before
    public void init() {
        userRepository.deleteAll();
    }

    @Test
    public void addUserTest() throws Exception {
        User user = createTestUser(TEST_USER_1);

        User testUser = userRepository.findById(user.getId()).orElse(null);
        assertThat(testUser)
                .isNotNull()
                .hasFieldOrPropertyWithValue("userName", TEST_USER_1);
    }

    @Test
    public void getUserByNameTest() throws Exception {
        createTestUser(TEST_USER_1);
        createTestUser(TEST_USER_2);

        User testUser = userRepository.findUserByUserName(TEST_USER_2);

        assertThat(testUser)
                .isNotNull()
                .hasFieldOrPropertyWithValue("userName", TEST_USER_2);
    }

    private User createTestUser(String name) {
        User user = new User(name);
        return userRepository.save(user);
    }
}