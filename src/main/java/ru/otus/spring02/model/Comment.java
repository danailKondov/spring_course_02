package ru.otus.spring02.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"book", "user"})
@Document(collection = "book_comments")
public class Comment {

    @Id
    private String id;

    @DBRef
    private User user;

    private BookInfo book;

    private String commentText;

    private Date commentDate;

    public Comment(BookInfo book, User user, String comment) {
        this.book = book;
        this.user = user;
        commentText = comment;
        commentDate = new Date();
    }

    public Comment() {
        commentDate = new Date();
    }
}
