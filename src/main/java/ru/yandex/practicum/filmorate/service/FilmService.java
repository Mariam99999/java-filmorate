package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final InMemoryFilmStorage filmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Set<Integer> addLike(int id, int userId) {

        return filmStorage.getFilmById(id).addLike(userId);
    }

    public Set<Integer> deleteLike(int id , int userId) {

        return filmStorage.getFilmById(id).deleteLike(userId);
    }

    public List<Film> popularFilms(Integer count) {
        List<Film> films = filmStorage.getFilms();
        if (count == null ) count = 10;
        return films.stream()
                .sorted(Comparator.comparingInt(f -> f.getLikes().size()))
                .skip(count)
                .collect(Collectors.toList());
    }
    public Film getFilm (Integer id){
        return filmStorage.getFilmById(id);
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



}
