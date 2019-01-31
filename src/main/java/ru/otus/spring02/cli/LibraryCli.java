package ru.otus.spring02.cli;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.AutoSizeConstraints;
import org.springframework.shell.table.SimpleHorizontalAligner;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModel;
import org.springframework.shell.table.TableModelBuilder;
import ru.otus.spring02.model.Book;
import ru.otus.spring02.model.Comment;
import ru.otus.spring02.service.LibraryServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.shell.table.CellMatchers.table;

/**
 * Created by хитрый жук on 23.12.2018.
 */
@ShellComponent
public class LibraryCli {

    private LibraryServiceImpl libraryService;

    @Autowired
    public LibraryCli(LibraryServiceImpl libraryService) {
        this.libraryService = libraryService;
    }

    @ShellMethod(value = "Show all authors names", key = "all-names")
    public List<String> getAllAuthorsNames() {
        return libraryService.getAllAuthorsNames();
    }

    @ShellMethod(value = "Show all books", key = "all-books")
    public String getAllBooks() {
        return getTableFromList(libraryService.getAllBooks());
    }

    @ShellMethod(value = "Show all genres", key = "all-genres")
    public List<String> getAllGenres() {
        return libraryService.getAllGenres();
    }

    @ShellMethod(value = "Get books by authors name", key = "book-of")
    public String getBooksByAuthorsName(
            @ShellOption(help = "authors name") String name) {
        return getTableFromList(libraryService.getBooksByAuthorsName(name));
    }

    @ShellMethod(value = "Get comments by book id", key = "comm-by")
    public List<String> getAllCommentsForBook(
            @ShellOption(help = "book id") String bookId) {
        return libraryService.getAllComments(bookId);
    }

    @ShellMethod(value = "Add new book, use comma as delimiter for authors", key = "add-book")
    public String addNewBook(
            @ShellOption(help = "genre") String genreName,
            @ShellOption(help = "title") String title,
            @ShellOption(help = "authors, use comma as delimiter ") String authors) {

        String[] authorsArr = authors.split(",");
        Set<String> authorList = Arrays.stream(authorsArr).collect(Collectors.toSet());
        boolean isSuccessful = libraryService.addNewBook(new Book(title, genreName, authorList));
        if (isSuccessful) {
            return "New book was added successfully";
        } else {
            return "Book already exists";
        }
    }

    @ShellMethod(value = "Add new comment", key = "add-comm")
    public String addNewCommentToBook(
            @ShellOption(help = "book id") String bookId,
            @ShellOption(help = "user name") String userName,
            @ShellOption(help = "comment") String comment) {
        boolean isSuccessful = libraryService.addComment(bookId, userName, comment);
        if (isSuccessful) {
            return "Comment was updated successfully";
        } else {
            return "Book with id = " + bookId + " doesn't exist";
        }
    }

    @ShellMethod(value = "Update book title", key = "upd-title-id")
    public String updateBookTitleById(
            @ShellOption(help = "id") String id,
            @ShellOption(help = "new title") String newTitle) {
        boolean isSuccessful = libraryService.updateBookTitleById(id, newTitle);
        if (isSuccessful) {
            return "Title was updated successfully";
        } else {
            return "Book doesn't exist";
        }
    }

    @ShellMethod(value = "Delete book", key = "del-book")
    public String deleteBookById(
            @ShellOption(help = "id of book to delete") String bookId) {
        boolean isSuccessful = libraryService.deleteBookById(bookId);
        if (isSuccessful) {
            return "Book was deleted successfully";
        } else {
            return "Book doesn't exist";
        }
    }


    private String getTableFromList(List<Book> books) {
        TableModelBuilder<String> modelBuilder = new TableModelBuilder<>();
        modelBuilder.addRow()
                .addValue("Book id")
                .addValue("Author(s)")
                .addValue("Title")
                .addValue("Genre");
        books.forEach(book -> {
            Optional<String> authors = book.getAuthors().stream().reduce((a, b) -> a + ", " + b);

            String genre = book.getGenre();
            String genreName = "";
            if (genre != null) {
                genreName = genre;
            }

            modelBuilder.addRow()
                    .addValue(String.valueOf(book.getId()))
                    .addValue(authors.orElse("no author defined"))
                    .addValue(book.getTitle())
                    .addValue(genreName);
        });
        TableModel model = modelBuilder.build();

        return new TableBuilder(model)
                .on(table())
                .addSizer(new AutoSizeConstraints())
                .addAligner(SimpleHorizontalAligner.left)
                .build()
                .render(400);
    }



}
