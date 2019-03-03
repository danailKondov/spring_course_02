package ru.otus.spring02.util;

import ru.otus.spring02.controller.BookDto;
import ru.otus.spring02.controller.CommentDto;
import ru.otus.spring02.model.Author;
import ru.otus.spring02.model.Book;
import ru.otus.spring02.model.Comment;
import ru.otus.spring02.model.Genre;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class Mapper {

    public static Book mapDtoToBook(BookDto bookDto) {
        Set<Author> authors = mapAuthors(bookDto.getAuthors());
        Genre genre = new Genre(bookDto.getGenre());
        return new Book(bookDto.getTitle(), genre, authors);
    }

    public static BookDto mapBookToDto(Book book) {
        BookDto result = new BookDto();
        result.setId(book.getId());
        if (book.getAuthors() != null) {
            result.setAuthors(book
                    .getAuthors()
                    .stream()
                    .map(Author::getName)
                    .reduce((x, y) -> x + ", " + y)
                    .orElse("no author"));
        }
        result.setTitle(book.getTitle());
        Genre genre = book.getGenre();
        if (genre != null) {
            result.setGenre(genre.getGenreName());
        } else {
            result.setGenre(" ");
        }
        Set<CommentDto> commDto = Collections.emptySet();
        if (book.getComments() != null) {
            commDto = book.getComments()
                    .stream()
                    .map(Mapper::mapCommentToDto)
                    .collect(Collectors.toSet());
        }
        result.setComments(commDto);
        return result;
    }

//    public static List<BookDto> mapBookListToDto(List<Book> books) {
//        return books
//                .stream()
//                .map(Mapper::mapBookToDto)
//                .collect(Collectors.toList());
//    }

    public static CommentDto mapCommentToDto(Comment comment) {
        CommentDto result = new CommentDto();
        result.setTitle(comment.getBook().getTitle());
        result.setUser(comment.getUser().getUserName());
        result.setText(comment.getCommentText());
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy", new Locale("ru"));
        result.setDate(format.format(comment.getCommentDate()));
        return result;
    }

//    public static List<CommentDto> mapCommentListToDto(List<Comment> comments) {
//        return comments
//                .stream()
//                .map(Mapper::mapCommentToDto)
//                .collect(Collectors.toList());
//    }

    private static Set<Author> mapAuthors(String authors) {
        String[] authorsArr = authors.split(",");
        return Arrays.stream(authorsArr)
                .map(s -> new Author(s.trim()))
                .collect(Collectors.toSet());
    }

}
