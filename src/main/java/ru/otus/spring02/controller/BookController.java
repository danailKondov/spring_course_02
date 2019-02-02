package ru.otus.spring02.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.spring02.model.Author;
import ru.otus.spring02.model.Book;
import ru.otus.spring02.model.Comment;
import ru.otus.spring02.model.Genre;
import ru.otus.spring02.service.LibraryService;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class BookController {

    @Autowired
    private LibraryService libraryService;

    @GetMapping("/")
    public String showAllBooksOnIndexPage(Model model) {
        List<Book> books = libraryService.getAllBooks();
        List<BookDto> bookDtos = books
                .stream()
                .map(BookDto::new)
                .collect(Collectors.toList());
        model.addAttribute("books", bookDtos);
        return "index";
    }

    @GetMapping("/comment")
    public String showCommentsForBookId(@RequestParam(name = "id") Long id, Model model) {
        List<Comment> comments = libraryService.getAllFullComments(id);
        model.addAttribute("comments", comments);
        return "comment";
    }

    @GetMapping("/edit")
    public String showBookForEdit(@RequestParam(name = "id") Long id, Model model) {
        Book book = libraryService.getBookById(id);
        model.addAttribute("book", new BookDto(book));
        return "edit";
    }

    @PostMapping("/add")
    public String addNewBook(@RequestParam(name = "authors") String authors,
                             @RequestParam(name = "title") String title,
                             @RequestParam(name = "genre") String genre,
                             Model model) {
        Set<Author> authorSet = mapAuthors(authors);
        Genre genreToAdd = new Genre(genre);
        Book book = libraryService.addNewBook(new Book(title, genreToAdd, authorSet));
        model.addAttribute("addResult", book.getId() != null);
        return "redirect:/";
    }

    @GetMapping("/update")
    public String updateBook(@RequestParam(name = "title") String title,
                             @RequestParam(name = "id") Long id,
                             Model model) {
        libraryService.updateBookTitleById(id, title);
        Book book = libraryService.getBookById(id);
        model.addAttribute("book", new BookDto(book));
        return "edit";
    }

    @GetMapping("/delete")
    public String deleteBook(@RequestParam(name = "id") Long id) {
        libraryService.deleteBookById(id);
        return "redirect:/";
    }

    private Set<Author> mapAuthors(String authors) {
        String[] authorsArr = authors.split(",");
        return Arrays.stream(authorsArr)
                .map(s -> new Author(s.trim()))
                .collect(Collectors.toSet());
    }
}
