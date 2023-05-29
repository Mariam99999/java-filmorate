package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;


    @Override
    public Film addFilm(Film film) {
        String query = "INSERT INTO PUBLIC.FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION,RATING_ID)"
                + "VALUES (?,?,?,?,?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(query, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setDouble(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);

        film.setId(keyHolder.getKey().intValue());
        if (film.getGenres() != null) insertGenres(film);
        return film;
    }

    @Override
    public Film update(Film film) {
        getFilmById(film.getId());

        String filmQuery = "UPDATE PUBLIC.FILMS" + " SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, " +
                "RATING_ID = ?  WHERE ID = ?;";

        jdbcTemplate.update(filmQuery, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());

        updateGenres(film);
        return film;
    }


    @Override
    public List<Film> getFilms() {
        String genreQuery = "SELECT fg.FILM_ID, g.GENRE_ID, g.TITLE FROM PUBLIC.FILM_GENRE AS fg" + " LEFT JOIN GENRE as g on fg.GENRE_ID = g.GENRE_ID";
        SqlRowSet genreSet = jdbcTemplate.queryForRowSet(genreQuery);

        Map<Integer, Set<Genre>> genresMap = makeGenresMap(genreSet);

        String query = " SELECT f.ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE" + ",f.DURATION, r.RATING_ID, r.TITLE  " +
                "FROM PUBLIC.FILMS as f" + " left join PUBLIC.RATING as r on f.RATING_ID = r.RATING_ID";
        return jdbcTemplate.query(query, (rs, row) -> makeFilm(rs, genresMap));
    }


    @Override
    public Film getFilmById(int id) {
        String genreQuery = "SELECT fg.FILM_ID, g.GENRE_ID, g.TITLE FROM PUBLIC.FILM_GENRE AS fg" + " LEFT JOIN GENRE" +
                " as g on fg.GENRE_ID = g.GENRE_ID " + "WHERE fg.FILM_ID = ?;";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(genreQuery, id);
        Map<Integer, Set<Genre>> genresMap = makeGenresMap(sqlRowSet);


        String filmQuery = " SELECT f.ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE," + "f.DURATION, r.RATING_ID, r.TITLE" +
                "  FROM PUBLIC.FILMS as f " + "left join PUBLIC.RATING as r on f.RATING_ID = r.RATING_ID " + "WHERE f.ID = ?;";
        List<Film> films = jdbcTemplate.query(filmQuery, (rs, row) -> makeFilm(rs, genresMap), id);

        if (films.isEmpty()) throw new EntityNotFoundException("FILM DOES NOT EXIST");
        return films.get(0);
    }


    private Film makeFilm(ResultSet rs, Map<Integer, Set<Genre>> genresMap) throws SQLException {
        int filmId = rs.getInt("ID");
        Film film = new Film(filmId, rs.getString("NAME"), rs.getString("DESCRIPTION"),
                rs.getDate("RELEASE_DATE").toLocalDate(), rs.getDouble("DURATION"), new Mpa(rs.getInt("RATING_ID"),
                rs.getString("TITLE")));

        if (genresMap.containsKey(filmId)) {
            for (Genre genre : genresMap.get(filmId)) {
                film.addGenre(genre);
            }
        }
        return film;
    }


    private Map<Integer, Set<Genre>> makeGenresMap(SqlRowSet sqlRowSet) {
        Map<Integer, Set<Genre>> genresMap = new HashMap<>();
        while (sqlRowSet.next()) {
            Genre genre = new Genre(sqlRowSet.getInt("GENRE_ID"), sqlRowSet.getString("TITLE"));
            int id = sqlRowSet.getInt("FILM_ID");
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

    private void updateGenres(Film film) {
        String queryDeleteGenres = "DELETE from FILM_GENRE where FILM_ID = ?";
        jdbcTemplate.update(queryDeleteGenres, film.getId());
        insertGenres(film);
    }


    private void insertGenres(Film film) {
        String query = "INSERT INTO PUBLIC.FILM_GENRE (FILM_ID, GENRE_ID)" + "VALUES (?,?);";
        List<Genre> genres = new ArrayList<>(film.getGenres());
        jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, film.getId());
                ps.setInt(2, genres.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return genres.size();
            }
        });

    }

}
