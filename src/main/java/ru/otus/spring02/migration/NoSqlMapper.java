package ru.otus.spring02.migration;

import org.springframework.stereotype.Component;
import ru.otus.spring02.model.Author;
import ru.otus.spring02.model.Book;
import ru.otus.spring02.model.Comment;
import ru.otus.spring02.model.Genre;
import ru.otus.spring02.model.MongoBook;
import ru.otus.spring02.model.MongoComment;
import ru.otus.spring02.model.User;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Component
public class NoSqlMapper {

    private SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy", new Locale("ru"));

    public MongoBook mapBookToMongo(Book book) {
        MongoBook mongoBook = new MongoBook();
        String authors = book.getAuthors()
                .stream()
                .map(Author::getName)
                .reduce((s1, s2) -> s1 + ", " + s2)
                .orElse(" ");
        mongoBook.setAuthors(authors);
        mongoBook.setTitle(book.getTitle());
        Genre genre = book.getGenre();
        String genreName = " ";
        if (genre != null && genre.getGenreName() != null) {
            genreName = genre.getGenreName();
        }
        mongoBook.setGenre(genreName);

        Set<Comment> comments = book.getComments();
        if (comments != null && comments.size() > 0) {
            Set<MongoComment> mongoComments = new HashSet<>();

            for (Comment comment : comments) {
                MongoComment mongoComment = new MongoComment();
                User user = comment.getUser();
                if (user != null) {
                    mongoComment.setUserName(user.getUserName());
                }
                mongoComment.setCommentText(comment.getCommentText());
                mongoComment.setCommentDate(format.format(comment.getCommentDate()));
                mongoComments.add(mongoComment);
            }
            mongoBook.setComments(mongoComments);
        }

        return mongoBook;
    }
}
