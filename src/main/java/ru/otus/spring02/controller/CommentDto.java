package ru.otus.spring02.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.spring02.model.Comment;

import java.text.SimpleDateFormat;
import java.util.Locale;

@Getter
@Setter
@NoArgsConstructor
public class CommentDto {

    private String title;
    private String user;
    private String text;
    private String date;
}
