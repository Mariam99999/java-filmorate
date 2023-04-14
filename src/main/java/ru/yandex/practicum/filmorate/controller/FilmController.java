package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

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

    public boolean checkValidity(Film film) {
        return film.getDescription().length() <= 200 && film.getDuration().isPositive();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        if (checkValidity(film)) {
            film.setId(++id);
            films.put(film.getId(), film);
            log.info("film added");
            return film;
        }
        log.error("invalid data");
        throw new ValidationException();
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if (checkValidity(film)) {
            if (films.containsKey(film.getId())) {
                films.put(film.getId(), film);
                log.info("film updated");
                return film;
            }
        }
        log.error("invalid data");
        throw new ValidationException();
    }

    @GetMapping
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }


}
