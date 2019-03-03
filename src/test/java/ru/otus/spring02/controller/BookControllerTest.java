package ru.otus.spring02.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import ru.otus.spring02.model.Book;
import ru.otus.spring02.service.LibraryService;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private WebTestClient testClient;

    @MockBean
    private LibraryService libraryService;

    @Test
    public void showAllBooksOnIndexPageTest() throws Exception {

        when(libraryService.getAllBooks()).thenReturn(Flux.just(new Book()));

        testClient
                .get()
                .uri("/api/books/")
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .exchange()
                .expectStatus()
                .isOk();
    }

}