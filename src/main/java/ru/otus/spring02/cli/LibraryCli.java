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
import ru.otus.spring02.model.Author;
import ru.otus.spring02.model.Book;
import ru.otus.spring02.model.Genre;
import ru.otus.spring02.service.LibraryServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

    @ShellMethod(value = "Add new book, use comma as delimiter for authors", key = "add-book")
    public String addNewBook(
            @ShellOption(help = "genre") String genreName,
            @ShellOption(help = "title") String title,
            @ShellOption(help = "authors, use comma as delimiter ") String authors) {

        String[] authorsArr = authors.split(",");
        List<Author> authorList = Arrays.stream(authorsArr).map(Author::new).collect(Collectors.toList());
        Genre genre = new Genre(genreName);
        boolean isSuccessful = libraryService.addNewBook(new Book(title, genre, authorList));
        if (isSuccessful) {
            return "New book was added successfully";
        } else {
            return "Book already exists";
        }
    }

    @ShellMethod(value = "Add new genre", key = "add-genre")
    public String addNewGenre(
            @ShellOption(help = "genre name") String genre) {
        boolean isSuccessful = libraryService.addNewGenre(new Genre(genre));
        if (isSuccessful) {
            return "New genre was added successfully";
        } else {
            return "Genre already exists";
        }
    }

    @ShellMethod(value = "Add new author", key = "add-author")
    public void addNewAuthor(
            @ShellOption(help = "author name") String authorName) {
        libraryService.addNewAuthor(new Author(authorName));
    }

    @ShellMethod(value = "Update book title", key = "upd-title-id")
    public String updateBookTitleById(
            @ShellOption(help = "id") Long id,
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
            @ShellOption(help = "id of book to delete") Long id) {
        boolean isSuccessful = libraryService.deleteBookById(id);
        if (isSuccessful) {
            return "Book was deleted successfully";
        } else {
            return "Book doesn't exist";
        }
    }

    @ShellMethod(value = "Delete author", key = "del-auth")
    public String deleteAuthorById(
            @ShellOption(help = "id ofauthor to delete") Long id) {
        boolean isSuccessful = libraryService.deleteAuthorById(id);
        if (isSuccessful) {
            return "Author was deleted successfully";
        } else {
            return "Author doesn't exist";
        }
    }

    @ShellMethod(value = "Delete genre", key = "del-genre")
    public String deleteGenre(
            @ShellOption(help = "genre to delete") String genreName) {
        boolean isSuccessful = libraryService.deleteGenre(genreName);
        if (isSuccessful) {
            return "Genre was deleted successfully";
        } else {
            return "Genre doesn't exist";
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
            Optional<String> authors = book.getAuthors().stream().map(Author::getName).reduce((a, b) -> a + ", " + b);
            modelBuilder.addRow()
                    .addValue(String.valueOf(book.getId()))
                    .addValue(authors.orElse("no author defined"))
                    .addValue(book.getTitle())
                    .addValue(book.getGenre().getGenreName());
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
