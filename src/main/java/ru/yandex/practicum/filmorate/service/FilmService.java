package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.rating.RatingStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final GenreStorage genreStorage;
    private final RatingStorage ratingStorage;
    private final LikeStorage likeStorage;
    private final UserStorage userStorage;


    public Set<Integer> addLike(int filmId, int userId) {
        filmStorage.getFilmById(filmId);
        userStorage.getUserById(userId);
        return likeStorage.addLike(filmId, userId);
    }

    public Set<Integer> deleteLike(int filmId, int userId) {
        filmStorage.getFilmById(filmId);
        userStorage.getUserById(userId);
        return likeStorage.deleteLike(filmId, userId);
    }

    public List<Film> popularFilms(Integer count) {
        return filmStorage.getPopularFilms(count);
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
        return genreStorage.getGenres();
    }

    public Genre getGenreById(int id) {
        return genreStorage.getGenreById(id);
    }

    public List<Mpa> getAgeRatings() {
        return ratingStorage.getAgeRatings();
    }

    public Mpa getAgeRatingById(int id) {
        return ratingStorage.getAgeRatingById(id);
    }

}
