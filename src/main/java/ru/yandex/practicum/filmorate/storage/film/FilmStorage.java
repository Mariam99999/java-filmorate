package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface FilmStorage {
    Film addFilm(Film film);

    Film update(Film film);

    List<Film> getFilms();

    Film getFilmById(int id);

    List<Genre> getGenres();

    Genre getGenreById(int id);

    List<Mpa> getAgeRatings();

    Mpa getAgeRatingById(int id);
}
