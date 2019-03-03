package ru.otus.spring02.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring02.exceptions.NoBookWithSuchIdLibraryException;
import ru.otus.spring02.exceptions.NoCommentWithIdLibraryException;
import ru.otus.spring02.model.BookInfo;
import ru.otus.spring02.repository.AuthorRepository;
import ru.otus.spring02.repository.BookRepository;
import ru.otus.spring02.repository.CommentRepository;
import ru.otus.spring02.repository.GenreRepository;
import ru.otus.spring02.repository.UserRepository;
import ru.otus.spring02.model.Author;
import ru.otus.spring02.model.Book;
import ru.otus.spring02.model.Comment;
import ru.otus.spring02.model.Genre;
import ru.otus.spring02.model.User;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LibraryServiceImpl implements LibraryService {

    private BookRepository bookRepository;
    private AuthorRepository authorRepository;
    private GenreRepository genreRepository;
    private CommentRepository commentRepository;
    private UserRepository userRepository;

    @Autowired
    public LibraryServiceImpl(BookRepository bookRepository,
                              AuthorRepository authorRepository,
                              GenreRepository genreRepository,
                              CommentRepository commentRepository,
                              UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public Flux<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Flux<String> getAllAuthorsNames() {
        return authorRepository.findAll().map(Author::getName);
    }

    @Override
    public Flux<String> getAllGenres() {
        return genreRepository.findAll().map(Genre::getGenreName);
    }

    @Override
    public Flux<Book> getBooksByAuthorsName(String name) {
        return bookRepository.findAll().filter(book -> {
            if (book.getAuthors() != null) {
                for (Author author : book.getAuthors()) {
                    if (name.equals(author.getName())) {
                        return true;
                    }
                }
            }
            return false;
        });
    }

    @Override
    public Flux<String> getAllCommentsTexts(String bookId) {
        return commentRepository.findCommentsByBook_Id(bookId)
                .map(Comment::getCommentText);
    }

    @Override
    public Flux<Comment> getAllComments(String id) {
        return commentRepository.findCommentsByBook_Id(id);
    }

    @Override
    public Mono<Book> getBookById(String id) {
        return bookRepository.findBookById(id);
    }

    @Override
    public Mono<Genre> addNewGenre(final Genre genre) {
        return genreRepository
                .findGenreByGenreName(genre.getGenreName())
                .switchIfEmpty(genreRepository.save(genre));
    }

    @Override
    public Mono<Book> addNewBook(Book book) {

        Mono<Genre> genre = this.addNewGenre(book.getGenre());

        Mono<Set<Author>> authors = Flux.fromIterable(book.getAuthors())
                .flatMap(this::addNewAuthor)
                .collect(Collectors.toSet());

        return Mono.zip(authors, genre, (a, g) -> new Book(book.getTitle(), g, a))
                .flatMap(bookRepository::save);
    }

    @Override
    public Mono<Author> addNewAuthor(Author author) {
        return authorRepository
                .findAuthorByName(author.getName())
                .switchIfEmpty(authorRepository.save(author));
    }

    @Override
    public Mono<Comment> addComment(final String bookId, final String userName, final String comment) {

        Mono<BookInfo> bookInfoMono = bookRepository
                .findBookById(bookId)
                .map(BookInfo::new)
                .switchIfEmpty(Mono.error(new NoBookWithSuchIdLibraryException(bookId)));

        Mono<User> userMono = userRepository
                .findUserByUserName(userName)
                .switchIfEmpty(userRepository.save(new User(userName)));

        return Mono.zip(bookInfoMono, userMono, (book, user) -> new Comment(book, user, comment))
                .flatMap(commentRepository::save);
    }

    @Override
    public Mono<Book> updateBookTitleById(String id, String newTitle) {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(newTitle);
                    return book;
                })
                .flatMap(bookRepository::save)
                .switchIfEmpty(Mono.error(new NoBookWithSuchIdLibraryException(id)));
    }

    @Override
    public Mono<Book> updateBook(Mono<Book> bookMono) {
        return bookRepository.save(bookMono);
    }

    @Override
    public Mono<Comment> updateComment(Mono<Comment> comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Mono<Comment> updateComment(String id, String text) {
        return commentRepository.findById(id)
                .flatMap(comm -> {
                    comm.setCommentText(text);
                    return Mono.just(comm);
                })
                .flatMap(commentRepository::save)
                .switchIfEmpty(Mono.error(new NoCommentWithIdLibraryException(id)));
    }

    @Override
    public Mono<Long> deleteBookById(String id) {
        commentRepository.deleteCommentByBook_Id(id);
        return bookRepository.deleteBookById(id);
    }

    @Override
    public Mono<Long> deleteAuthorById(String id) {
        return authorRepository.deleteAuthorById(id);
    }

    @Override
    public Mono<Long> deleteGenre(String genreName) {
        return genreRepository.deleteGenreByGenreName(genreName);
    }

    @Override
    public Mono<Long> deleteCommentById(String id) {
        return commentRepository.deleteCommentById(id);
    }

    @Override
    public void deleteAll() {
        genreRepository.deleteAll();
        userRepository.deleteAll();
        authorRepository.deleteAll();
        bookRepository.deleteAll();
        commentRepository.deleteAll();
    }
}
