package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmStorage filmStorage;
    private final User user = new User(1, "email", "l", "n", LocalDate.of(2000, 1, 1));
    Film film = new Film(1, "f1", "d1",
            LocalDate.of(1895, 12, 28), 120.0, new Mpa(1, "G"));

    @Test
    public void testFindUserById() {
        userStorage.addUser(user);

        Optional<User> userOptional = Optional.ofNullable(userStorage.getUserById(1));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testGetAllUsers() {
        assertEquals(0, userStorage.getUsers().size());
        userStorage.addUser(user);
        assertEquals(1, userStorage.getUsers().size());
    }

    @Test
    public void testAddUser() {
        int sizeBeforeAdd = userStorage.getUsers().size();
        userStorage.addUser(user);
        int sizeAfterAdd = userStorage.getUsers().size();
        assertTrue(sizeAfterAdd > sizeBeforeAdd);
    }

    @Test
    public void testUpdateUser() {
        userStorage.addUser(user);
        userStorage.addUser(user);
        User user = userStorage.getUserById(1);
        user.setName("newName");
        user.addFriend(2);
        userStorage.updateUser(user);
        assertEquals(userStorage.getUserById(1).getName(), user.getName());
        assertTrue(userStorage.getUserById(1).getFriends().contains(2));
    }

    @Test
    public void testAddFilm() {
        int sizeBeforeAdd = filmStorage.getFilms().size();
        filmStorage.addFilm(film);
        int sizeAfterAdd = filmStorage.getFilms().size();
        assertTrue(sizeAfterAdd > sizeBeforeAdd);
    }

    @Test
    public void testGetAllFilms() {
        List<Film> films = filmStorage.getFilms();
        assertEquals(0, filmStorage.getFilms().size());
        filmStorage.addFilm(film);
        assertEquals(1, filmStorage.getFilms().size());
    }

    @Test
    public void testUpdateFilm() {
        filmStorage.addFilm(film);
        Film oldFilm = filmStorage.getFilms().get(0);
        oldFilm.addGenre(new Genre(1, "Комедия"));
        filmStorage.update(oldFilm);

        assertEquals(oldFilm, filmStorage.getFilmById(1));
    }

    @Test
    public void testGetFilmById() {
        filmStorage.addFilm(film);
        assertEquals(1, filmStorage.getFilmById(1).getId());
    }

    @Test
    public void testGetAllGenres() {
        assertTrue(filmStorage.getGenres().size() > 0);
    }

    @Test
    public void testGetGenreById() {
        assertEquals(1, filmStorage.getGenreById(1).getId());
    }

    @Test
    public void testGetAllAgeRating() {
        assertTrue(filmStorage.getAgeRatings().size() > 0);
    }

    @Test
    public void testGetAgeRatingById() {
        assertEquals(filmStorage.getAgeRatingById(1).getId(), 1);

    }
}
