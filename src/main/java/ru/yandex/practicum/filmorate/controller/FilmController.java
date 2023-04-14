package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private static int id = 0;
    private final Map<Integer, Film> films = new HashMap<>();
    private static final LocalDate START_DATE = LocalDate.of(1895, 12, 27);
    private static final String INVALID_DATE_FILM = "Film can't start before 1895.02.27";
    private static final String WRONG_ID_MESSAGE = "Wrong id";

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        checkDate(film);
        film.setId(++id);
        films.put(film.getId(), film);
        log.info("film added");
        return film;

    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        checkDate(film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("film updated");
            return film;
        }
        log.error(WRONG_ID_MESSAGE);
        throw new ValidationException(WRONG_ID_MESSAGE);
    }

    @GetMapping
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    private void checkDate(Film film) {
        if (film.getReleaseDate().isBefore(START_DATE)) {
            log.error(INVALID_DATE_FILM);
            throw new ValidationException(INVALID_DATE_FILM);
        }
    }
}
