package ru.otus.spring02.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "mongo_comments")
public class MongoComment {

    @Id
    private String id;
    private String userName;
    private String commentText;
    private String commentDate;
}
