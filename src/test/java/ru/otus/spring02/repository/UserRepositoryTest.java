package ru.otus.spring02.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.spring02.model.User;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by хитрый жук on 19.01.2019.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext
public class UserRepositoryTest {

    private static final String TEST_USER_1 = "testUser";
    private static final String TEST_USER_2 = "testUser2";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void addUserTest() throws Exception {
        User user = createTestUser(TEST_USER_1);

        User testUser = entityManager.find(User.class, user.getId());
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