package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
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

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(query, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setDouble(4, film.getDuration());
            return stmt;
        }, keyHolder);

        film.setId(keyHolder.getKey().intValue());

        if (film.getGenres() != null) insertGenres(film);
        if (film.getLikes() != null) insertLikes(film);
        if (film.getMpa() != null) insertRating(film);
        return film;
    }

    @Override
    public Film update(Film film) {
        getFilmById(film.getId());

        String filmQuery = "UPDATE PUBLIC.FILMS" + " SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?  WHERE ID = ?;";

        jdbcTemplate.update(filmQuery, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getId());

        if (film.getMpa() != null) updateRating(film);
        updateGenres(film);
        updateLikes(film);
        return film;
    }


    @Override
    public List<Film> getFilms() {
        String genreQuery = "SELECT fg.FILM_ID, g.GENRE_ID, g.TITLE FROM PUBLIC.FILM_GENRE AS fg" + " LEFT JOIN GENRE as g on fg.GENRE_ID = g.GENRE_ID";
        SqlRowSet genreSet = jdbcTemplate.queryForRowSet(genreQuery);

        Map<Integer, Set<Genre>> genresMap = makeGenresMap(genreSet);

        String likesQuery = "Select * from PUBLIC.FILM_LIKES";
        SqlRowSet likesSet = jdbcTemplate.queryForRowSet(likesQuery);
        Map<Integer, Set<Integer>> likesMap = makeLikesMap(likesSet);


        String query = " SELECT f.ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE" + ",f.DURATION, r.RATING_ID, r.TITLE  FROM PUBLIC.FILMS as f" + " left join PUBLIC.FILM_RATING as fr on f.ID = fr.FILM_ID " + "left join PUBLIC.RATING as r on fr.RATING_ID = r.RATING_ID";
        List<Film> films = jdbcTemplate.query(query, (rs, row) -> makeFilm(rs, genresMap, likesMap));
        return films;
    }


    @Override
    public Film getFilmById(int id) {
        String genreQuery = "SELECT fg.FILM_ID, g.GENRE_ID, g.TITLE FROM PUBLIC.FILM_GENRE AS fg" + " LEFT JOIN GENRE" +
                " as g on fg.GENRE_ID = g.GENRE_ID " + "WHERE fg.FILM_ID = ?;";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(genreQuery, id);
        Map<Integer, Set<Genre>> genresMap = makeGenresMap(sqlRowSet);

        String likesQuery = "Select * from PUBLIC.FILM_LIKES WHERE FILM_ID = ?";
        SqlRowSet likesSet = jdbcTemplate.queryForRowSet(likesQuery, id);
        Map<Integer, Set<Integer>> likesMap = makeLikesMap(likesSet);

        String filmQuery = " SELECT f.ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE," + "f.DURATION, r.RATING_ID, r.TITLE  FROM PUBLIC.FILMS as f " + "left join PUBLIC.FILM_RATING as fr on f.ID = fr.FILM_ID " + "left join PUBLIC.RATING as r on fr.RATING_ID = r.RATING_ID " + "WHERE f.ID = ?;";
        List<Film> films = jdbcTemplate.query(filmQuery, (rs, row) -> makeFilm(rs, genresMap, likesMap), id);

        if (films.isEmpty()) throw new NullPointerException("FILM DOES NOT EXIST");
        return films.get(0);
    }

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
        if (genres.isEmpty()) throw new NullPointerException("GENRE DOES NOT EXIST");
        return genres.get(0);
    }

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
        if (ageRatings.isEmpty()) throw new NullPointerException("GENRE DOES NOT EXIST");
        return ageRatings.get(0);
    }

    private Film makeFilm(ResultSet rs, Map<Integer, Set<Genre>> genresMap, Map<Integer, Set<Integer>> likesMap) throws SQLException {
        int filmId = rs.getInt("ID");
        Film film = new Film(filmId, rs.getString("NAME"), rs.getString("DESCRIPTION"), rs.getDate("RELEASE_DATE").toLocalDate(), rs.getDouble("DURATION"), getMpa(rs.getInt("RATING_ID"), rs.getString("TITLE")));
        if (likesMap.containsKey(filmId)) {
            for (int userId : likesMap.get(filmId)) {
                film.addLike(userId);
            }
        }
        if (genresMap.containsKey(filmId)) {
            for (Genre genre : genresMap.get(filmId)) {
                film.addGenre(genre);
            }
        }
        return film;
    }

    private Mpa getMpa(int id, String s) {
        if (s == null) return null;
        return new Mpa(id, s);
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

    private void updateRating(Film film) {

        String ratingUpdateQuery = "UPDATE PUBLIC.FILM_RATING" + " SET RATING_ID = ? WHERE FILM_ID = ?;";

        jdbcTemplate.update(ratingUpdateQuery, film.getMpa().getId(), film.getId());
    }

    private Map<Integer, Set<Integer>> makeLikesMap(SqlRowSet likesRowSet) {
        Map<Integer, Set<Integer>> likesMap = new HashMap<>();
        while (likesRowSet.next()) {
            int userId = likesRowSet.getInt("USER_ID");
            int filmId = likesRowSet.getInt("FILM_ID");
            Set<Integer> likesSet;
            if (likesMap.containsKey(filmId)) {
                likesSet = likesMap.get(filmId);
            } else {
                likesSet = new HashSet<>();
            }
            likesSet.add(userId);
            likesMap.put(filmId, likesSet);
        }
        return likesMap;
    }

    private void updateLikes(Film film) {

        String queryDeleteLikes = "DELETE from PUBLIC.FILM_LIKES where FILM_ID = ?";
        jdbcTemplate.update(queryDeleteLikes, film.getId());

        String queryAddNewLikes = "INSERT INTO PUBLIC.FILM_LIKES (FILM_ID, USER_ID)" + "VALUES (?, ?);";
        for (Integer userId : film.getLikes()) {
            jdbcTemplate.update(queryAddNewLikes, film.getId(), userId);
        }
    }

    private void insertGenres(Film film) {
        String query = "INSERT INTO PUBLIC.FILM_GENRE (FILM_ID, GENRE_ID)" + "VALUES (?,?);";
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(query, film.getId(), genre.getId());
        }
    }

    private void insertRating(Film film) {
        String query = "INSERT INTO PUBLIC.FILM_RATING (FILM_ID, RATING_ID)" + "VALUES (?,?);";
        jdbcTemplate.update(query, film.getId(), film.getMpa().getId());
    }

    private void insertLikes(Film film) {
        String query = "INSERT INTO PUBLIC.FILM_LIKES (FILM_ID, USER_ID )" + "VALUES (?,?);";
        for (int userId : film.getLikes()) {
            jdbcTemplate.update(query, film.getId(), userId);
        }
    }
}
