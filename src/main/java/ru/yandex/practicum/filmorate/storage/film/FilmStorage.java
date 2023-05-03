package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    public Film addFilm (Film film);
    public Film update( Film film);
    public List<Film> getFilms();
    public Film getFilmById (int id);
}
