INSERT INTO genres (genre_name) VALUES ('детектив');
INSERT INTO genres (genre_name) VALUES ('роман');
INSERT INTO genres (genre_name) VALUES ('драма');
INSERT INTO genres (genre_name) VALUES ('сатира');
INSERT INTO genres (genre_name) VALUES ('комедия');

INSERT INTO authors (author_name) VALUES ('А.Кристи');
INSERT INTO authors (author_name) VALUES ('Л.Толстой');
INSERT INTO authors (author_name) VALUES ('Ф.Достоевский');
INSERT INTO authors (author_name) VALUES ('В.Пелевин');

INSERT INTO users (user_name, password, role) VALUES ('UsualTroll', '$2y$12$T1cKUFLjDPXnIwe8WZVGeuhmkzvsqzNNvNjbwebmro8fCW.1ppGJS', 'ROLE_USER');
INSERT INTO users (user_name, password, role) VALUES ('FatTroll', '$2y$12$T1cKUFLjDPXnIwe8WZVGeuhmkzvsqzNNvNjbwebmro8fCW.1ppGJS', 'ROLE_USER');
INSERT INTO users (user_name, password, role) VALUES ('ThinTroll', '$2y$12$T1cKUFLjDPXnIwe8WZVGeuhmkzvsqzNNvNjbwebmro8fCW.1ppGJS', 'ROLE_USER');
INSERT INTO users (user_name, password, role) VALUES ('test', 'test', 'ROLE_USER');
INSERT INTO users (user_name, password, role) VALUES ('test2', 'test2', 'ROLE_ADMIN');

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

INSERT INTO book_comments (comment_text, comment_date, user_id, book_id)
VALUES ('Читалось долго и с усилием, чтобы хоть что-то понять и отделить юмор и сатиру от идеи. Кажется, мне это не удалось...',
        '2017-10-25 15:46:00',
        (SELECT id FROM users WHERE user_name = 'UsualTroll'),
        (SELECT id FROM books WHERE title = 'Generation П'));

INSERT INTO book_comments (comment_text, comment_date, user_id, book_id)
VALUES ('Редкая совместная работа двух классиков русской литературы в жанре детектива. Мастрид для всех думающих людей.',
        '2018-11-24 16:50:00',
        (SELECT id FROM users WHERE user_name = 'FatTroll'),
        (SELECT id FROM books WHERE title = 'Война и преступление'));

INSERT INTO book_comments (comment_text, comment_date, user_id, book_id)
VALUES ('Кроме матерных слов в этом отзыве написать нечего. Отвратительная компания. ' ||
        'У нас домашний переезд Благовещенск - Ярославль. Контейнер 5тн. Сдали и заключили контейнер на доставку 14 октября. ' ||
        'Сейчас январь - контейнер до сих пор не доставили. Заплатили 70 т.р. Контейнер 2 месяца стоял у них на площадке. ',
        '2019-01-15 12:30:00',
        (SELECT id FROM users WHERE user_name = 'ThinTroll'),
        (SELECT id FROM books WHERE title = 'Восточный экспресс'));


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