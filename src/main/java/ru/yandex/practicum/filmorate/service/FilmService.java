package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;


    public Set<Integer> addLike(int id, int userId) {
        Film film = filmStorage.getFilmById(id);
        film.addLike(userId);
        filmStorage.update(film);
        return film.getLikes();
    }

    public Set<Integer> deleteLike(int id, int userId) {

        return filmStorage.getFilmById(id).deleteLike(userId);
    }

    public List<Film> popularFilms(Integer count) {
        List<Film> films = filmStorage.getFilms();
        return films.stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }

    public List<Genre> getGenres() {
        return filmStorage.getGenres();
    }

    public Genre getGenreById(int id) {
        return filmStorage.getGenreById(id);
    }

    public List<Mpa> getAgeRatings() {
        return filmStorage.getAgeRatings();
    }

    public Mpa getAgeRatingById(int id) {
        return filmStorage.getAgeRatingById(id);
    }


}
