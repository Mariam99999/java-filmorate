package ru.yandex.practicum.filmorate.storage.rating;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RatingDbStorage implements RatingStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getAgeRatings() {
        String genreQuery = "SELECT * FROM PUBLIC.RATING";
        return jdbcTemplate.query(genreQuery, (rs, row) -> new Mpa(rs.getInt("RATING_ID"), rs.getString("TITLE")));
    }

    @Override
    public Mpa getAgeRatingById(int id) {
        String genreQuery = "SELECT * FROM PUBLIC.RATING WHERE RATING_ID = ?";
        List<Mpa> ageRatings = jdbcTemplate.query(genreQuery, (rs, row) -> new Mpa(rs.getInt("RATING_ID"),
                rs.getString("TITLE")), id);
        if (ageRatings.isEmpty()) throw new EntityNotFoundException("GENRE DOES NOT EXIST");
        return ageRatings.get(0);
    }
}
