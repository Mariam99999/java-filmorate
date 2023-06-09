MERGE INTO PUBLIC.GENRE (GENRE_ID, TITLE)
    VALUES (1,'Комедия'),
           (2,'Драма'),
           (3,'Мультфильм'),
           (4,'Триллер'),
           (5,'Документальный'),
           (6,'Боевик');



MERGE INTO PUBLIC.RATING (RATING_ID, TITLE)
VALUES (1,'G'),
(2,'PG'),
(3,'PG-13'),
(4,'R'),
(5,'NC-17');


INSERT INTO PUBLIC.FRIEND_STATUS (STATUS_TITLE)
VALUES ('UNCONFIRMED');
INSERT INTO PUBLIC.FRIEND_STATUS (STATUS_TITLE)
VALUES ('CONFIRMED');
INSERT INTO PUBLIC.FRIEND_STATUS (STATUS_TITLE)
VALUES ('UNKNOWN');

-- INSERT INTO PUBLIC.FILM_GENRE (FILM_ID, GENRE_ID)
-- VALUES (1, 1);
-- INSERT INTO PUBLIC.FILM_GENRE (FILM_ID, GENRE_ID)
-- VALUES (2, 2);
-- INSERT INTO PUBLIC.FILM_GENRE (FILM_ID, GENRE_ID)
-- VALUES (3, 3);
-- INSERT INTO PUBLIC.FILM_GENRE (FILM_ID, GENRE_ID)
-- VALUES (1, 2);

-- INSERT INTO PUBLIC.FILM_LIKES (FILM_ID, USER_ID)
-- VALUES (1, 1);
-- INSERT INTO PUBLIC.FILM_LIKES (FILM_ID, USER_ID)
-- VALUES (1, 2);
-- INSERT INTO PUBLIC.FILM_LIKES (FILM_ID, USER_ID)
-- VALUES (2, 2);
-- INSERT INTO PUBLIC.FILM_LIKES (FILM_ID, USER_ID)
-- VALUES (3, 3);

-- INSERT INTO PUBLIC.FILM_RATING (FILM_ID, RATING_ID)
-- VALUES (1, 1);
-- INSERT INTO PUBLIC.FILM_RATING (FILM_ID, RATING_ID)
-- VALUES (2, 2);
-- INSERT INTO PUBLIC.FILM_RATING (FILM_ID, RATING_ID)
-- VALUES (3, 3);

-- INSERT INTO PUBLIC.USER_FRIENDS (USER_ID, FRIEND_ID, STATUS_ID)
-- VALUES (1, 2, DEFAULT);
-- INSERT INTO PUBLIC.USER_FRIENDS (USER_ID, FRIEND_ID, STATUS_ID)
-- VALUES (2, 3, 2);
-- INSERT INTO PUBLIC.USER_FRIENDS (USER_ID, FRIEND_ID, STATUS_ID)
-- VALUES (3, 1, 3);


