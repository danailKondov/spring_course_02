package ru.otus.spring02.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.spring02.model.Book;
import ru.otus.spring02.service.LibraryService;

import java.util.List;

import static ru.otus.spring02.util.Mapper.mapBookListToDto;
import static ru.otus.spring02.util.Mapper.mapBookToDto;
import static ru.otus.spring02.util.Mapper.mapCommentListToDto;
import static ru.otus.spring02.util.Mapper.mapDtoToBook;

@RestController
@RequestMapping("api/books")
public class BookController {

    @Autowired
    private LibraryService libraryService;

    @GetMapping("/")
    public List<BookDto> showAllBooksOnIndexPage() {
        return mapBookListToDto(libraryService.getAllBooks());
    }

    @GetMapping("/{bookId}/comment")
    public List<CommentDto> showCommentsForBookId(@PathVariable("bookId") Long bookId) {
        return mapCommentListToDto(libraryService.getAllFullComments(bookId));
    }

    @PreAuthorize("hasNameOf('test')")
    @GetMapping("/{bookId}")
    public BookDto findBookById(@PathVariable("bookId") Long bookId) {
        return mapBookToDto(libraryService.getBookById(bookId));
    }

    @PutMapping("/{bookId}")
    public BookDto showBookForEdit(@PathVariable("bookId") Long bookId) {
        return mapBookToDto(libraryService.getBookById(bookId));
    }

    @PostMapping("/")
    public ResponseEntity<BookDto> addNewBook(@RequestBody BookDto bookDto) {
        Book book = libraryService.addNewBook(mapDtoToBook(bookDto));
        return book.getId() != null?
                new ResponseEntity<>(mapBookToDto(book), HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/")
    public BookDto updateBook(@RequestParam(name = "title") String title,
                             @RequestParam(name = "id") Long id) {
        libraryService.updateBookTitleById(id, title);
        return mapBookToDto(libraryService.getBookById(id));
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity deleteBook(@PathVariable("bookId") Long bookId) {
        boolean result = libraryService.deleteBookById(bookId);
        return result?
                new ResponseEntity(HttpStatus.OK) :
                new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
