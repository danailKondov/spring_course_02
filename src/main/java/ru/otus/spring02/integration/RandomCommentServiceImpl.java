package ru.otus.spring02.integration;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.spring02.model.Book;
import ru.otus.spring02.service.LibraryServiceImpl;

import java.util.HashSet;

@Service
public class RandomCommentServiceImpl implements RandomCommentService {

    private LibraryServiceImpl libraryService;
    private String[] commentsPool = {"первый, хех!", "^-_-^", "чудесная книга!", "10 из 10!", "Абсолютный мастрид!",
            "Автор новый классик в данном жанре!", "Просто невероятно!", "Читал не отрываясь"};

    @Autowired
    public RandomCommentServiceImpl(LibraryServiceImpl libraryService) {
        this.libraryService = libraryService;
    }

    @Override
    public Book addRandomComment(Book book) {
        if (book.getComments() == null) {
            book.setComments(new HashSet<>());
        }
        libraryService.addComment(book.getId(), null, commentsPool[RandomUtils.nextInt(0, commentsPool.length)]);
        return book;
    }
}
