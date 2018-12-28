INSERT INTO genres (genre_name) VALUES ('детектив');
INSERT INTO genres (genre_name) VALUES ('роман');
INSERT INTO genres (genre_name) VALUES ('драма');
INSERT INTO genres (genre_name) VALUES ('сатира');
INSERT INTO genres (genre_name) VALUES ('комедия');

INSERT INTO authors (author_name) VALUES ('А.Кристи');
INSERT INTO authors (author_name) VALUES ('Л.Толстой');
INSERT INTO authors (author_name) VALUES ('Ф.Достоевский');
INSERT INTO authors (author_name) VALUES ('В.Пелевин');

INSERT INTO books (title, genre_id)
VALUES ('Восточный экспресс', (SELECT id FROM genres WHERE genre_name = 'детектив'));

INSERT INTO books (title, genre_id)
VALUES ('Война и мир', (SELECT id FROM genres WHERE genre_name = 'роман'));

INSERT INTO books (title, genre_id)
VALUES ('Преступление и наказание', (SELECT id FROM genres WHERE genre_name = 'драма'));

INSERT INTO books (title, genre_id)
VALUES ('Generation П', (SELECT id FROM genres WHERE genre_name = 'сатира'));

INSERT INTO books (title, genre_id)
VALUES ('Идиот', (SELECT id FROM genres WHERE genre_name = 'драма'));

INSERT INTO books (title, genre_id)
VALUES ('Война и преступление', (SELECT id FROM genres WHERE genre_name = 'детектив'));

INSERT INTO books (title)
VALUES ('Война и преступление2');


INSERT INTO books_authors (authors_id, books_id)
VALUES (
  (SELECT id FROM authors WHERE author_name = 'А.Кристи'),
  (SELECT id FROM books WHERE title = 'Восточный экспресс'));

INSERT INTO books_authors (authors_id, books_id)
VALUES (
  (SELECT id FROM authors WHERE author_name = 'Л.Толстой'),
  (SELECT id FROM books WHERE title = 'Война и мир')
);

INSERT INTO books_authors (authors_id, books_id)
VALUES (
  (SELECT id FROM authors WHERE author_name = 'Ф.Достоевский'),
  (SELECT id FROM books WHERE title = 'Преступление и наказание')
);

INSERT INTO books_authors (authors_id, books_id)
VALUES (
  (SELECT id FROM authors WHERE author_name = 'В.Пелевин'),
  (SELECT id FROM books WHERE title = 'Generation П')
);

INSERT INTO books_authors (authors_id, books_id)
VALUES (
  (SELECT id FROM authors WHERE author_name = 'Ф.Достоевский'),
  (SELECT id FROM books WHERE title = 'Война и преступление')
);

INSERT INTO books_authors (authors_id, books_id)
VALUES (
  (SELECT id FROM authors WHERE author_name = 'Л.Толстой'),
  (SELECT id FROM books WHERE title = 'Война и преступление')
);