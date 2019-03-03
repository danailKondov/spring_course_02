package ru.otus.spring02.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ru.otus.spring02.model.User;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {

    Mono<User> findUserByUserName(String name);
}
