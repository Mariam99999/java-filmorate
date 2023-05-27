package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.AgeRating;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@Primary
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film addFilm(Film film) {
        String query = "INSERT INTO PUBLIC.FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION)" + "VALUES (?, ?, ?, ?);";
        jdbcTemplate.update(query, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration());
        return film;
    }

    @Override
    public Film update(Film film) {

        String query = "UPDATE PUBLIC.FILMS" +
                " SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?  WHERE ID = ?;";
        jdbcTemplate.update(query, film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getId());
        return film;
    }

    @Override
    public List<Film> getFilms() {
        String genreQuery = "SELECT fg.FILM_ID, g.TITLE FROM PUBLIC.FILM_GENRE AS fg" +
                " LEFT JOIN GENRE as g on fg.GENRE_ID = g.GENRE_ID";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(genreQuery);

        Map<Integer, Set<Genre>> genresMap = makeGenresMap(sqlRowSet);


        String query = " SELECT f.ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE" +
                ",f.DURATION, r.TITLE  FROM PUBLIC.FILMS as f" +
                " left join PUBLIC.FILM_RATING as fr on f.ID = fr.FILM_ID " +
                "left join PUBLIC.RATING as r on fr.RATING_ID = r.RATING_ID";
        List<Film> films = jdbcTemplate.query(query, (rs, row) -> makeFilm(rs, genresMap));
        return films;
    }



    @Override
    public Film getFilmById(int id) {
        String genreQuery = "SELECT fg.FILM_ID, g.TITLE FROM PUBLIC.FILM_GENRE AS fg" +
                " LEFT JOIN GENRE as g on fg.GENRE_ID = g.GENRE_ID " +
                "WHERE fg.FILM_ID = ?;";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(genreQuery,id);
        Map<Integer, Set<Genre>> genresMap = makeGenresMap(sqlRowSet);
        String filmQuery =" SELECT f.ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE," +
                "f.DURATION, r.TITLE  FROM PUBLIC.FILMS as f " +
                "left join PUBLIC.FILM_RATING as fr on f.ID = fr.FILM_ID " +
                "left join PUBLIC.RATING as r on fr.RATING_ID = r.RATING_ID " +
                "WHERE f.ID = ?;";
        List<Film> films = jdbcTemplate.query(filmQuery, (rs, row) -> makeFilm(rs, genresMap),id);

        return films.get(0);
    }

    private Film makeFilm(ResultSet rs, Map<Integer, Set<Genre>> genresMap) throws SQLException {
        int filmId = rs.getInt("ID");
        return new Film(filmId, rs.getString("NAME"), rs.getString("DESCRIPTION"),
                rs.getDate("RELEASE_DATE").toLocalDate(), rs.getDouble("DURATION"),
                genresMap.get(filmId), getAgeRatingEnumFromString(rs.getString("TITLE")));
    }
    private AgeRating getAgeRatingEnumFromString(String s){
        AgeRating ageRating;
        switch (s){
            case "G":
               ageRating = AgeRating.G;
               break;
            case "PG":
               ageRating = AgeRating.PG;
               break;
            case "PG_13":
                ageRating = AgeRating.PG_13;
                break;
            case "R":
                ageRating = AgeRating.R;
                break;
            case "NC_17":
                ageRating = AgeRating.NC_17;
                break;
            default: ageRating = AgeRating.UNKNOWN;
        }
        return ageRating;
    }
    private Map<Integer, Set<Genre>> makeGenresMap(SqlRowSet sqlRowSet) {
        Map<Integer, Set<Genre>> genresMap = new HashMap<>();
        while (sqlRowSet.next()) {
            Genre genre;
            int id = sqlRowSet.getInt("FILM_ID");
            switch (sqlRowSet.getString("TITLE")) {
                case "COMEDY":
                    genre = Genre.COMEDY;
                    break;
                case "ACTION":
                    genre = Genre.ACTION;
                    break;
                case "THRILLER":
                    genre = Genre.THRILLER;
                    break;
                case "DRAMA":
                    genre = Genre.DRAMA;
                    break;
                case "MULTIPLICATION":
                    genre = Genre.MULTIPLICATION;
                    break;
                case "DOCUMENTARY":
                    genre = Genre.DOCUMENTARY;
                    break;
                default:
                    genre = Genre.UNKNOWN;
            }
            Set<Genre> genreSet;
            if (genresMap.containsKey(id)) {
                genreSet = genresMap.get(id);
            } else {
                genreSet = new HashSet<>();
            }
            genreSet.add(genre);
            genresMap.put(id, genreSet);
        }
        return genresMap;
    }
}
