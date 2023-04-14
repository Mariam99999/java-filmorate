package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class FilmControllerTests {
    @Test
    public void checkValidity() {
        FilmController filmController = new FilmController();
        Film film1 = new Film(0, "name", "test1",
                LocalDate.of(1895, 12, 28), Duration.ofMinutes(130));
        Film film2 = new Film(5, "name2", "test2",
                LocalDate.of(2024, 12, 28), Duration.ofMinutes(140));
        Film film3 = new Film(7, "name3", "test3",
                LocalDate.of(1894, 12, 28), Duration.ofMinutes(130));
        assertTrue(filmController.checkValidity(film1));
        assertTrue(filmController.checkValidity(film2));
        assertFalse(filmController.checkValidity(film3));

    }
}
