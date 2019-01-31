package ru.otus.spring02.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.spring02.repository.BookRepository;
import ru.otus.spring02.model.Book;
import ru.otus.spring02.model.Comment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LibraryServiceImpl implements LibraryService {

    private BookRepository bookRepository;

    @Autowired
    public LibraryServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public List<String> getAllAuthorsNames() {
        List<String> result = new ArrayList<>();
        bookRepository.findAll().stream().forEach(book -> {result.addAll(book.getAuthors());});
        return result;
    }

    @Override
    public List<String> getAllGenres() {
        return bookRepository.findAll().stream().map(Book::getGenre).collect(Collectors.toList());
    }

    @Override
    public List<Book> getBooksByAuthorsName(String name) {
        return bookRepository.findBooksByAuthors(name);
    }

    @Override
    public List<String> getAllComments(String bookId) {
        return bookRepository.findBookWithCommentsById(bookId).getComments().stream().map(Comment::getCommentText).collect(Collectors.toList());
    }

    @Override
    public boolean addNewBook(Book book) {
        book = bookRepository.save(book);
        return book.getId() != null;
    }

    @Override
    public boolean addComment(String bookId, String userName, String comment) {
        Book book = bookRepository.findBookById(bookId);
        if (book == null) {
            return false;
        }

        Set<Comment> comments = book.getComments();
        if (comments == null) {
            comments = new HashSet<>();
        }
        comments.add(new Comment(userName, comment));
        book.setComments(comments);
        book = bookRepository.save(book);
        return book.getId() != null;
    }

    @Override
    public boolean updateBookTitleById(String bookId, String newTitle) {
        Book book = bookRepository.findBookById(bookId);
        if (book == null) {
            return false;
        }

        book.setTitle(newTitle);
        book = bookRepository.save(book);
        return book.getId() != null;
    }

    @Override
    public boolean deleteBookById(String bookId) {
        int result = bookRepository.deleteBookById(bookId);
        return result > 0;
    }

    @Override
    public void deleteAll() {
        bookRepository.deleteAll();
    }
}
