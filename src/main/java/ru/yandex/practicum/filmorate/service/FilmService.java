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

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final GenreStorage genreStorage;
    private final RatingStorage ratingStorage;
    private final LikeStorage likeStorage;


    public Set<Integer> addLike(int filmId, int userId) {
        return likeStorage.addLike(filmId, userId);
    }

    public Set<Integer> deleteLike(int filmId, int userId) {

        return likeStorage.deleteLike(filmId, userId);
    }

    public List<Film> popularFilms(Integer count) {
        List<Film> films = filmStorage.getFilms();
        return films.stream()
                .sorted((f1, f2) -> Integer.compare(likeStorage.getFilmLikes(f2.getId()).size(),
                        likeStorage.getFilmLikes(f1.getId()).size()))
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
