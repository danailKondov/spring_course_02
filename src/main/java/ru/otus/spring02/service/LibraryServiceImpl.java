package ru.otus.spring02.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class LibraryServiceImpl implements LibraryService {

    private BookRepository bookRepository;
    private AuthorRepository authorRepository;
    private GenreRepository genreRepository;
    private CommentRepository commentRepository;
    private UserRepository userRepository;

    @Autowired
    public LibraryServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository, GenreRepository genreRepository, CommentRepository commentRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    @HystrixCommand(groupKey = "BookGroup", commandKey = "getAllBooksCommand", fallbackMethod = "getDefaultBooks")
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> getDefaultBooks() {
        return Collections.emptyList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllAuthorsNames() {
        return authorRepository.findAllAuthorsNames();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllGenres() {
        return genreRepository.findAll().stream()
                .map(Genre::getGenreName)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getBooksByAuthorsName(String name) {
        Author author = authorRepository.findAuthorByName(name);
        if (author == null) {
            return Collections.emptyList();
        }
        return bookRepository.findBooksByAuthorId(author.getId());
    }

    @Override
    public List<String> getAllComments(Long bookId) {
        return commentRepository.findCommentsTextByBookId(bookId);
    }

    @Override
    public List<Comment> getAllFullComments(Long id) {
        return commentRepository.findCommentsByBook_Id(id);
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findBookById(id);
    }

    @Override
    public boolean addNewGenre(Genre genre) {
        Genre checkGenre = genreRepository.findGenreByGenreName(genre.getGenreName());
        if (checkGenre != null) {
            return false;
        }
        genre = genreRepository.save(genre);
        return genre.getId() != null;
    }

    @Override
    public Book addNewBook(Book book) {

        Genre genre = genreRepository.findGenreByGenreName(book.getGenre().getGenreName());
        if (genre == null) {
            genre = genreRepository.save(book.getGenre());
        }
        book.setGenre(genre);

        Set<Author> authorSet = new HashSet<>();
        for (Author author : book.getAuthors()) {
            Author checkAuthor = authorRepository.findAuthorByName(author.getName());
            if (checkAuthor == null) {
                author = authorRepository.save(author);
                author.getBooks().add(book);
                authorSet.add(author);
            } else {
                checkAuthor.getBooks().add(book);
                authorSet.add(checkAuthor);
            }
        }
        book.setAuthors(authorSet);

        return bookRepository.save(book);
    }

    @Override
    public boolean addNewAuthor(Author author) {
        Author result = authorRepository.save(author);
        return result.getId() != null;
    }

    @Override
    public boolean addComment(Long bookId, String userName, String comment) {
        Book book = bookRepository.findBookById(bookId);
        if (book == null) {
            return false;
        }

        User user = userRepository.findUserByUserName(userName);
        if (user == null) {
            user = userRepository.save(
                    new User(
                            userName,
                            "$2y$12$T1cKUFLjDPXnIwe8WZVGeuhmkzvsqzNNvNjbwebmro8fCW.1ppGJS",
                            "ROLE_USER")
            );
        }

        Comment com = commentRepository.save(new Comment(book, user, comment));
        book.getComments().add(com);
        return true;
    }

    @Override
    public boolean updateBookTitleById(Long id, String newTitle) {
        int result = bookRepository.updateBookTitleById(id, newTitle);
        return result > 0;
    }

    @Override
    public boolean updateComment(Comment comment) {
        Optional<Comment> optionalComment = commentRepository.findById(comment.getId());
        if (!optionalComment.isPresent()) {
            return false;
        }
        Comment commentToUpdate = optionalComment.get();
        commentToUpdate.setCommentDate(comment.getCommentDate());
        commentToUpdate.setCommentText(comment.getCommentText());
        return true;
    }

    @Override
    public boolean deleteBookById(Long id) {
        int result = bookRepository.deleteBookById(id);
        return result > 0;
    }

    @Override
    public boolean deleteAuthorById(Long id) {
        int result = authorRepository.deleteAuthorById(id);
        return result > 0;
    }

    @Override
    public boolean deleteGenre(String genreName) {
        int result = genreRepository.deleteGenreByGenreName(genreName);
        return result > 0;
    }

    @Override
    public boolean deleteCommentById(Long id) {
        int result = commentRepository.deleteCommentById(id);
        return result > 0;
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
