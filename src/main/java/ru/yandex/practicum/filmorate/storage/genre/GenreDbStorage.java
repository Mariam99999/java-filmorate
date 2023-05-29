package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage{
    private final JdbcTemplate jdbcTemplate;
    @Override
    public List<Genre> getGenres() {
        String genreQuery = "SELECT * FROM PUBLIC.GENRE";
        return jdbcTemplate.query(genreQuery, (rs, row) -> new Genre(rs.getInt("GENRE_ID"), rs.getString(
                "TITLE")));
    }

    @Override
    public Genre getGenreById(int id) {
        String genreQuery = "SELECT * FROM PUBLIC.GENRE WHERE GENRE_ID = ?";
        List<Genre> genres = jdbcTemplate.query(genreQuery, (rs, row) -> new Genre(rs.getInt("GENRE_ID"), rs.getString(
                "TITLE")), id);
        if (genres.isEmpty()) throw new EntityNotFoundException("GENRE DOES NOT EXIST");
        return genres.get(0);
    }
}
