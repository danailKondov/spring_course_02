package ru.otus.spring02.model;

/**
 * Created by хитрый жук on 23.12.2018.
 */
public class Genre {

    private Long id;
    private String genreName;

    public Genre(String genre) {
        this.genreName = genre;
    }

    public Genre() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Genre genre = (Genre) o;

        if (getId() != null ? !getId().equals(genre.getId()) : genre.getId() != null) return false;
        return getGenreName() != null ? getGenreName().equals(genre.getGenreName()) : genre.getGenreName() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getGenreName() != null ? getGenreName().hashCode() : 0);
        return result;
    }
}
