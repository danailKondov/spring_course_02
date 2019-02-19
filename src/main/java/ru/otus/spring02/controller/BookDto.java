package ru.otus.spring02.controller;


import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.spring02.model.Author;
import ru.otus.spring02.model.Book;
import ru.otus.spring02.model.Genre;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class BookDto {

    private Long id;
    private String authors;
    private String title;
    private String genre;
    private Set<CommentDto> comments;
}
