package ru.otus.spring02.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "mongo_books")
public class MongoBook {

    @Id
    private String id;
    private String authors;
    private Set<MongoComment> comments;
    private String title;
    private String genre;
}
