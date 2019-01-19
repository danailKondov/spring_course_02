package ru.otus.spring02.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.spring02.model.User;

/**
 * Created by хитрый жук on 19.01.2019.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findUserByUserName(String name);
}
