package ru.otus.spring02.checks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.spring02.model.Book;
import ru.otus.spring02.repository.BookRepository;

import java.util.List;

@Component
public class NumOfCommentsHealthCheck implements HealthIndicator {

    private static final Long NO_COMMENTS_THRESHOLD = 1L;
    private BookRepository bookRepository;

    @Autowired
    public NumOfCommentsHealthCheck(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Health health() {
        List<Book> bookList = bookRepository.findAll();
        Long booksWithNoComments = bookList.stream()
                .filter(book -> book.getComments() == null || book.getComments().size() == 0)
                .count();
        if (booksWithNoComments >= NO_COMMENTS_THRESHOLD) {
            return Health.down().withDetail("Books with no comments: ", booksWithNoComments).build();
        }
        return Health.up().withDetail("Books with no comments: ", booksWithNoComments).build();
    }
}
