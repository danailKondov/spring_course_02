package ru.otus.spring02.exceptions;

public class NoCommentWithIdLibraryException extends RuntimeException{

    public NoCommentWithIdLibraryException(String message) {
        super(message);
    }
}
