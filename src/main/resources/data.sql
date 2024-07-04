-- INSERT INTO genres (name) VALUES('Комедия');
-- INSERT INTO genres (name) VALUES('Драма');
-- INSERT INTO genres (name) VALUES('Мультфильм');
-- INSERT INTO genres (name) VALUES('Триллер');
-- INSERT INTO genres (name) VALUES('Документальный');
-- INSERT INTO genres (name) VALUES('Боевик');
--
-- INSERT INTO ratings (name) VALUES('G');
-- INSERT INTO ratings (name) VALUES('PG');
-- INSERT INTO ratings (name) VALUES('PG-13');
-- INSERT INTO ratings (name) VALUES('R');
-- INSERT INTO ratings (name) VALUES('NC-17');
MERGE INTO genres (id, name)
    VALUES (1, 'Комедия'),
           (2, 'Драма'),
           (3, 'Мультфильм'),
           (4, 'Триллер'),
           (5, 'Документальный'),
           (6, 'Боевик');
MERGE INTO ratings (id, name)
    VALUES (1, 'G'),
           (2, 'PG'),
           (3, 'PG-13'),
           (4, 'R'),
           (5, 'NC-17');