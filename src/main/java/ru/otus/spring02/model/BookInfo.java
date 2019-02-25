package ru.otus.spring02.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "bookInfo")
public class BookInfo {

    private String id;
    private String title;
}
