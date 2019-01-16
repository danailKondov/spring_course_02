package ru.otus.spring02.dao;

import ru.otus.spring02.model.User;

/**
 * Created by хитрый жук on 15.01.2019.
 */
public interface UserDao {
    User addUser(User user);

    User getUserByName(String userName);

    void deleteAll();
}
