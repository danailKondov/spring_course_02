package ru.otus.spring02.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring02.service.LibraryService;
import ru.otus.spring02.util.Mapper;


import javax.websocket.server.PathParam;

import static ru.otus.spring02.util.Mapper.mapDtoToBook;

@RestController
@RequestMapping("api/books")
public class BookController {

    @Autowired
    private LibraryService libraryService;

    @GetMapping("/")
    public Flux<BookDto> showAllBooksOnIndexPage() {
        return libraryService.getAllBooks().map(Mapper::mapBookToDto);
    }

    @GetMapping("/{bookId}/comment")
    public Flux<CommentDto> showCommentsForBookId(@PathVariable(name = "bookId") String bookId) {
        return libraryService
                .getAllComments(bookId)
                .map(Mapper::mapCommentToDto);
    }

    @GetMapping("/{bookId}")
    public Mono<BookDto> showBookForEdit(@PathVariable("bookId") String bookId) {
        return libraryService
                .getBookById(bookId)
                .map(Mapper::mapBookToDto);
    }

    @PostMapping("/")
    public Mono<BookDto> addNewBook(@RequestBody BookDto bookDto) {
        return libraryService.addNewBook(mapDtoToBook(bookDto))
                .map(Mapper::mapBookToDto)
                .switchIfEmpty(Mono.error(new Exception()));
    }

    @PutMapping("/")
    public Mono<BookDto> updateBook(@RequestBody Mono<BookDto> bookDto) {
        return libraryService
                .updateBook(bookDto.map(Mapper::mapDtoToBook))
                .map(Mapper::mapBookToDto);
    }

    @DeleteMapping("/{bookId}")
    public Mono<ResponseEntity> deleteBook(@PathVariable("bookId") String bookId) {
        return libraryService.deleteBookById(bookId)
                .map(r -> new ResponseEntity(r == 0? HttpStatus.NOT_FOUND: HttpStatus.OK));
    }
}
