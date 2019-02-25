package ru.otus.spring02.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"authors", "comments"})
@Document(collection = "books")
public class Book {

    @Id
    private String id;

    @DBRef
    private Set<Author> authors;

    @DBRef
    private Set<Comment> comments;

    private String title;

    @DBRef
    private Genre genre;

    public Book(String title, Genre genre, Set<Author> authorList) {
        this.authors = authorList;
        this.genre = genre;
        this.title = title;
    }
}
