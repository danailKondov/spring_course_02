package ru.otus.spring02.exceptions;

public class NoBookWithSuchIdLibraryException extends RuntimeException {

    public NoBookWithSuchIdLibraryException(String message) {
        super(message);
    }
}
