package ru.otus.spring02.controller;


import lombok.Data;
import ru.otus.spring02.model.Author;
import ru.otus.spring02.model.Book;
import ru.otus.spring02.model.Comment;
import ru.otus.spring02.model.Genre;

import java.util.Set;

@Data
public class BookDto {

    private Long id;
    private String authors;
    private String title;
    private String genre;
    private Set<Comment> comments;

    public BookDto(Book book) {
        this.id = book.getId();
        this.authors = book.
                getAuthors().
                stream().
                map(Author::getName).
                reduce((x, y) -> x + ", " + y).
                orElse("no author");
        this.title = book.getTitle();
        Genre genre = book.getGenre();
        if (genre != null) {
            this.genre = genre.getGenreName();
        } else {
            this.genre = " ";
        }
        this.comments = book.getComments();
    }
}
