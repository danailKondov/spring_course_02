package ru.otus.spring02.integration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.spring02.model.Book;

@MessagingGateway
public interface BookGateway {

    @Gateway(requestChannel = "inBookChannel", replyChannel = "outBookChannel")
    public Book processNewBook(Book book);
}
