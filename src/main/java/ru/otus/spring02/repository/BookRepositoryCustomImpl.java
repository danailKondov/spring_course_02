package ru.otus.spring02.repository;

import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import ru.otus.spring02.model.Book;

public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Long updateBookTitleById(String id, String newTitle) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        Update update = new Update();
        update.set("title", newTitle);
        UpdateResult result = mongoTemplate.updateFirst(query, update, Book.class);
        return result.getModifiedCount();
    }
}
