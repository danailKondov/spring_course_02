package ru.otus.spring02.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring02.model.Author;
import ru.otus.spring02.model.Book;
import ru.otus.spring02.model.Comment;
import ru.otus.spring02.model.Genre;

public interface LibraryService {

    Flux<Book> getAllBooks();
    Flux<String> getAllAuthorsNames();
    Flux<String> getAllGenres();
    Flux<Book> getBooksByAuthorsName(String name);
    Flux<String> getAllCommentsTexts(String bookId);
    Flux<Comment> getAllComments(String id);
    Mono<Book> getBookById(String id);

    Mono<Genre> addNewGenre(Genre genre);
    Mono<Book> addNewBook(Book book);
    Mono<Author> addNewAuthor(Author author);
    Mono<Comment> addComment(String bookId, String userName, String comment);

    Mono<Book> updateBookTitleById(String id, String newTitle);
    Mono<Book> updateBook(Mono<Book> bookMono);
    Mono<Comment> updateComment(Mono<Comment> comment);
    Mono<Comment> updateComment(String id, String newComment);

    Mono<Long> deleteBookById(String id);
    Mono<Long> deleteAuthorById(String id);
    Mono<Long> deleteGenre(String genreName);
    Mono<Long> deleteCommentById(String id);
    void deleteAll();
}
