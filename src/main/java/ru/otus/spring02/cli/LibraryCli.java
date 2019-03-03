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
import ru.otus.spring02.model.Comment;
import ru.otus.spring02.model.Genre;
import ru.otus.spring02.service.LibraryServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.shell.table.CellMatchers.table;

@ShellComponent
public class LibraryCli {

    private LibraryServiceImpl libraryService;

    @Autowired
    public LibraryCli(LibraryServiceImpl libraryService) {
        this.libraryService = libraryService;
    }

    @ShellMethod(value = "Show all authors names", key = "all-names")
    public List<String> getAllAuthorsNames() {
        return libraryService.getAllAuthorsNames().collectList().block();
    }

    @ShellMethod(value = "Show all books", key = "all-books")
    public String getAllBooks() {
        return getTableFromList(libraryService.getAllBooks().collectList().block());
    }

    @ShellMethod(value = "Show all genres", key = "all-genres")
    public List<String> getAllGenres() {
        return libraryService.getAllGenres().collectList().block();
    }

    @ShellMethod(value = "Get books by authors name", key = "book-of")
    public String getBooksByAuthorsName(
            @ShellOption(help = "authors name") String name) {
        return getTableFromList(libraryService.getBooksByAuthorsName(name).collectList().block());
    }

    @ShellMethod(value = "Get comments by book id", key = "comm-by")
    public List<String> getAllCommentsForBook(
            @ShellOption(help = "book id") String bookId) {
        return libraryService.getAllCommentsTexts(bookId).collectList().block();
    }

    @ShellMethod(value = "Add new book, use comma as delimiter for authors", key = "add-book")
    public String addNewBook(
            @ShellOption(help = "genre") String genreName,
            @ShellOption(help = "title") String title,
            @ShellOption(help = "authors, use comma as delimiter ") String authors) {

        String[] authorsArr = authors.split(",");
        Set<Author> authorList = Arrays.stream(authorsArr).map(Author::new).collect(Collectors.toSet());
        Genre genre = new Genre(genreName);
        Book book = libraryService.addNewBook(new Book(title, genre, authorList)).block();
        if (book.getId() != null) {
            return "New book was added successfully";
        } else {
            return "Book already exists";
        }
    }

    @ShellMethod(value = "Add new genre", key = "add-genre")
    public void addNewGenre(
            @ShellOption(help = "genre name") String genre) {
        libraryService.addNewGenre(new Genre(genre)).block();
    }

    @ShellMethod(value = "Add new author", key = "add-author")
    public void addNewAuthor(
            @ShellOption(help = "author name") String authorName) {
        libraryService.addNewAuthor(new Author(authorName)).block();
    }

    @ShellMethod(value = "Add new comment", key = "add-comm")
    public void addNewCommentToBook(
            @ShellOption(help = "book id") String bookId,
            @ShellOption(help = "user name") String userName,
            @ShellOption(help = "comment") String comment) {
        libraryService.addComment(bookId, userName, comment).block();
    }

    @ShellMethod(value = "Update book title", key = "upd-title-id")
    public String updateBookTitleById(
            @ShellOption(help = "id") String id,
            @ShellOption(help = "new title") String newTitle) {
        Book book = libraryService.updateBookTitleById(id, newTitle).block();
        if (newTitle.equals(book.getTitle())) {
            return "Title was updated successfully";
        } else {
            return "Book doesn't exist";
        }
    }

    @ShellMethod(value = "Update comment", key = "upd-comm")
    public void updateCommentById(
            @ShellOption(help = "id") String id,
            @ShellOption(help = "new comment") String newComment) {
//        Comment comment = new Comment();
//        comment.setId(id);
//        comment.setCommentText(newComment);
        // после апдейта теряется инфа о книгах у комментов
        libraryService.updateComment(id, newComment).block();
    }

    @ShellMethod(value = "Delete book", key = "del-book")
    public String deleteBookById(
            @ShellOption(help = "id of book to delete") String id) {
        Long result = libraryService.deleteBookById(id).block();
        if (result > 0) {
            return "Book was deleted successfully";
        } else {
            return "Book doesn't exist";
        }
    }

    @ShellMethod(value = "Delete author", key = "del-auth")
    public String deleteAuthorById(
            @ShellOption(help = "id of author to delete") String id) {
        Long result = libraryService.deleteAuthorById(id).block();
        if (result > 0) {
            return "Author was deleted successfully";
        } else {
            return "Author doesn't exist";
        }
    }

    @ShellMethod(value = "Delete genre", key = "del-genre")
    public String deleteGenre(
            @ShellOption(help = "genre to delete") String genreName) {
        Long result = libraryService.deleteGenre(genreName).block();
        if (result > 0) {
            return "Genre was deleted successfully";
        } else {
            return "Genre doesn't exist";
        }
    }

    @ShellMethod(value = "Delete comment", key = "del-comm")
    public String deleteCommentById(
            @ShellOption(help = "id of comment to delete") String id) {
        Long result = libraryService.deleteCommentById(id).block();
        if (result > 0) {
            return "Comment was deleted successfully";
        } else {
            return "Comment doesn't exist";
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

            Genre genre = book.getGenre();
            String genreName = "";
            if (genre != null) {
                genreName = genre.getGenreName();
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
