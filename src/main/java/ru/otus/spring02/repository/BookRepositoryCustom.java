package ru.otus.spring02.repository;

public interface BookRepositoryCustom {

    Long updateBookTitleById(String id, String newTitle);
}
