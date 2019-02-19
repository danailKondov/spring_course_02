package ru.otus.spring02.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
public class BookController {

    @Autowired
    private LibraryService libraryService;

    @GetMapping("/all")
    public List<BookDto> showAllBooksOnIndexPage() {
        return mapBookListToDto(libraryService.getAllBooks());
    }

    @GetMapping("/comment")
    public List<CommentDto> showCommentsForBookId(@RequestParam(name = "id") Long id) {
        return mapCommentListToDto(libraryService.getAllFullComments(id));
    }

    @PutMapping("/edit")
    public BookDto showBookForEdit(@RequestParam(name = "id") Long id) {
        return mapBookToDto(libraryService.getBookById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<BookDto> addNewBook(@RequestBody BookDto bookDto) {
        Book book = libraryService.addNewBook(mapDtoToBook(bookDto));
        return book.getId() != null?
                new ResponseEntity<>(mapBookToDto(book), HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update")
    public BookDto updateBook(@RequestParam(name = "title") String title,
                             @RequestParam(name = "id") Long id) {
        libraryService.updateBookTitleById(id, title);
        return mapBookToDto(libraryService.getBookById(id));
    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteBook(@RequestParam(name = "id") Long id) {
        boolean result = libraryService.deleteBookById(id);
        return result?
                new ResponseEntity(HttpStatus.OK) :
                new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
