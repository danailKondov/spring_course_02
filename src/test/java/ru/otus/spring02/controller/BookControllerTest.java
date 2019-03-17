package ru.otus.spring02.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.spring02.model.Author;
import ru.otus.spring02.model.Book;
import ru.otus.spring02.model.Genre;
import ru.otus.spring02.security.CustomUserDetailService;
import ru.otus.spring02.service.LibraryService;

import java.util.Arrays;
import java.util.HashSet;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {

    private static final String TEST_TITLE = "testName";
    private static final String TEST_AUTHOR = "testAuthor";
    private static final String TEST_GENRE = "testGenre";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private LibraryService libraryService;

    @MockBean
    CustomUserDetailService userDetailService;

    @Test
    public void showAllBooksOnIndexPageTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get("/api/books/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

        verify(libraryService).getAllBooks();
    }

    @Test
    public void getCommentNoAuthTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get("/api/books/1/comment"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    public void getCommentWithAuthTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get("/api/books/1/comment"))
                .andExpect(status().isOk());
    }

    @Test
    public void putBookNoAuthTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .put("/api/books/"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    public void putBookWithAuthTest() throws Exception {
        when(libraryService.getBookById(1L))
                .thenReturn(new Book(
                        TEST_TITLE,
                        new Genre(TEST_GENRE),
                        new HashSet<>(Arrays.asList(new Author(TEST_AUTHOR)))));

        mvc.perform(MockMvcRequestBuilders
                .put("/api/books/?id=1&title=test"))
                .andExpect(status().isOk());
    }

    @Test
    public void postBookNoAuthTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .post("/api/books/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void deleteBookNoAuthTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .delete("/api/books/1"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    public void deleteBookWithWrongAuthTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .delete("/api/books/1"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    public void deleteBookWithProperAuthTest() throws Exception {
        when(libraryService.deleteBookById(1L)).thenReturn(true);

        mvc.perform(MockMvcRequestBuilders
                .delete("/api/books/1"))
                .andExpect(status().isOk());
    }

    @WithMockUser(
            username = "test",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    public void getBookWithProperAuthTest() throws Exception {
        when(libraryService.getBookById(1L))
                .thenReturn(new Book(
                        TEST_TITLE,
                        new Genre(TEST_GENRE),
                        new HashSet<>(Arrays.asList(new Author(TEST_AUTHOR)))));

        mvc.perform(MockMvcRequestBuilders
                .get("/api/books/1"))
                .andExpect(status().isOk());
    }

    @WithMockUser(
            username = "wrong",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    public void getBookWithWrongAuthTest() throws Exception {
        when(libraryService.getBookById(1L))
                .thenReturn(new Book(
                        TEST_TITLE,
                        new Genre(TEST_GENRE),
                        new HashSet<>(Arrays.asList(new Author(TEST_AUTHOR)))));

        mvc.perform(MockMvcRequestBuilders
                .get("/api/books/1"))
                .andExpect(status().isForbidden());
    }
}