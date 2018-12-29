package ru.otus.spring02.model;

import java.util.List;

/**
 * Created by хитрый жук on 23.12.2018.
 */
public class Author {

    private Long id;
    private List<Book> books;
    private String name;

    public Author(String authorName) {
        this.name = authorName;
    }

    public Author(List<Book> books, String name) {
        this.books = books;
        this.name = name;
    }

    public Author() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Author author = (Author) o;

        if (getId() != null ? !getId().equals(author.getId()) : author.getId() != null) return false;
        if (getBooks() != null ? !getBooks().equals(author.getBooks()) : author.getBooks() != null) return false;
        return getName() != null ? getName().equals(author.getName()) : author.getName() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getBooks() != null ? getBooks().hashCode() : 0);
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        return result;
    }
}
