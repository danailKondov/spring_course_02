package ru.otus.spring02.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring02.dao.AuthorDao;
import ru.otus.spring02.dao.BookDao;
import ru.otus.spring02.dao.CommentDao;
import ru.otus.spring02.dao.GenreDao;
import ru.otus.spring02.dao.UserDao;
import ru.otus.spring02.model.Author;
import ru.otus.spring02.model.Book;
import ru.otus.spring02.model.Comment;
import ru.otus.spring02.model.Genre;
import ru.otus.spring02.model.User;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by хитрый жук on 26.12.2018.
 */
@Service
@Transactional
public class LibraryServiceImpl implements LibraryService {

    private BookDao bookDao;
    private AuthorDao authorDao;
    private GenreDao genreDao;
    private CommentDao commentDao;
    private UserDao userDao;

    @Autowired
    public LibraryServiceImpl(BookDao bookDao, AuthorDao authorDao, GenreDao genreDao, CommentDao commentDao, UserDao userDao) {
        this.bookDao = bookDao;
        this.authorDao = authorDao;
        this.genreDao = genreDao;
        this.userDao = userDao;
        this.commentDao = commentDao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        return bookDao.getAllBooks();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllAuthorsNames() {
        return authorDao.getAllAuthorsNames();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllGenres() {
        return genreDao.getAllGenres().stream()
                .map(Genre::getGenreName)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getBooksByAuthorsName(String name) {
        Author author = authorDao.getAuthorByName(name);
        if (author == null) {
            return Collections.emptyList();
        }
        return bookDao.getBooksByAuthor(author);
    }

    @Override
    public List<String> getAllComments(Long bookId) {
        return commentDao.getCommentsByBookId(bookId);
    }

    @Override
    public boolean addNewGenre(Genre genre) {
        Genre checkGenre = genreDao.getGenreByName(genre.getGenreName());
        if (checkGenre != null) {
            return false;
        }
        genre = genreDao.addGenre(genre);
        return genre.getId() != null;
    }

    @Override
    public boolean addNewBook(Book book) {

        Genre genre = genreDao.getGenreByName(book.getGenre().getGenreName());
        if (genre == null) {
            genre = genreDao.addGenre(book.getGenre());
        }
        book.setGenre(genre);

        Set<Author> authorSet = new HashSet<>();
        for (Author author : book.getAuthors()) {
            Author checkAuthor = authorDao.getAuthorByName(author.getName());
            if (checkAuthor == null) {
                author = authorDao.addNewAuthor(author);
                author.getBooks().add(book);
                authorSet.add(author);
            } else {
                checkAuthor.getBooks().add(book);
                authorSet.add(checkAuthor);
            }
        }
        book.setAuthors(authorSet);

        if(isBookDuplicate(book)) {
            return false;
        }

        book = bookDao.addNewBook(book);
        return book.getId() != null;
    }

    private boolean isBookDuplicate(Book book) {
        List<Book> booksWithSameTitle = bookDao.getBooksByTitle(book.getTitle());
        if (!booksWithSameTitle.isEmpty()) {
            for (Book bookFromDB : booksWithSameTitle) {
                if (book.getGenre().equals(bookFromDB.getGenre())
                        && book.getAuthors().containsAll(bookFromDB.getAuthors())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean addNewAuthor(Author author) {
        Author result = authorDao.addNewAuthor(author);
        return result.getId() != null;
    }

    @Override
    public boolean addComment(Long bookId, String userName, String comment) {
        Book book = bookDao.getBookById(bookId);
        if (book == null) {
            return false;
        }

        User user = userDao.getUserByName(userName);
        if (user == null) {
            user = userDao.addUser(new User(userName));
        }

        Comment com = commentDao.addComment(new Comment(book, user, comment));
        book.getComments().add(com);
        return true;
    }

    @Override
    public boolean updateBookTitleById(Long id, String newTitle) {
        return bookDao.updateBookTitleById(id, newTitle);
    }

    @Override
    public boolean updateComment(Comment comment) {
        Comment commentToUpdate = commentDao.getCommentById(comment.getId());
        if (commentToUpdate == null) {
            return false;
        }
        commentToUpdate.setCommentDate(comment.getCommentDate());
        commentToUpdate.setCommentText(comment.getCommentText());
        return true;
    }

    @Override
    public boolean deleteBookById(Long id) {
        return bookDao.deleteBookById(id);
    }

    @Override
    public boolean deleteAuthorById(Long id) {
        return authorDao.deleteAuthorById(id);
    }

    @Override
    public boolean deleteGenre(String genreName) {
        Genre genre = genreDao.getGenreByName(genreName);
        if (genre == null) {
            return false;
        }
        genreDao.deleteGenre(genre);
        return true;
    }

    @Override
    public boolean deleteCommentById(Long id) {
        return commentDao.deleteCommentById(id);
    }

    @Override
    public void deleteAll() {
        genreDao.deleteAll();
        userDao.deleteAll();
        authorDao.deleteAll();
        bookDao.deleteAll();
        commentDao.deleteAll();
    }
}
