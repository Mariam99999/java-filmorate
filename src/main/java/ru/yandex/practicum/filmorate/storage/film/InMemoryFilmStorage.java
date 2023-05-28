package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static final String WRONG_ID_MESSAGE = "Wrong id";
    private static int id = 0;
    private final Map<Integer, Film> films = new HashMap<>();
    private final List<Genre> genres = new ArrayList<>();

    @Override
    public Film addFilm(Film film) {
        film.setId(++id);
        films.put(film.getId(), film);
        log.info("film added");
        return film;
    }

    @Override
    public Film update(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("film updated");
            return film;
        }
        throw new RuntimeException(WRONG_ID_MESSAGE);
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(int id) {
        if (films.containsKey(id)) return films.get(id);
        throw new NullPointerException("Wrong id");
    }

    @Override
    public List<Genre> getGenres() {
        return genres;
    }

    @Override
    public Genre getGenreById(int id) {
        return genres.stream().filter(g -> g.getId() == id).collect(Collectors.toList()).get(0);
    }

    @Override
    public List<Mpa> getAgeRatings() {
        return List.of();
    }

    @Override
    public Mpa getAgeRatingById(int id) {
        return new Mpa(1, "COMEDY");
    }
}
