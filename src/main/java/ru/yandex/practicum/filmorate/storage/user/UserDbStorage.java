package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@RequiredArgsConstructor
@Primary
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User addUser(User user) {
        String query = "INSERT INTO PUBLIC.USERS (NAME, LOGIN, EMAIL, BIRTHDAY)" + "VALUES (?, ?, ?, ?);";
        jdbcTemplate.update(query, user.getName(), user.getLogin(), user.getEmail(), user.getBirthday());
        return user;
    }

    @Override
    public User updateUser(User user) {
        String query = "UPDATE PUBLIC.USERS" +
                " SET NAME = ?, LOGIN = ?, EMAIL = ?, BIRTHDAY= ?  WHERE ID = ?;";
        jdbcTemplate.update(query, user.getName(), user.getLogin(),
                user.getEmail(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public List<User> getUsers() {
        String friendsQuery = "SELECT * FROM PUBLIC.USER_FRIENDS WHERE STATUS_ID = 2";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(friendsQuery);
        Map<Integer, Set<Integer>> friendsMap = makeFriendsMap(sqlRowSet);




        String query = " SELECT f.ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE" +
                ",f.DURATION, r.TITLE  FROM PUBLIC.FILMS as f" +
                " left join PUBLIC.FILM_RATING as fr on f.ID = fr.FILM_ID " +
                "left join PUBLIC.RATING as r on fr.RATING_ID = r.RATING_ID";
        List<Film> films = jdbcTemplate.query(query, (rs, row) -> makeFilm(rs, genresMap));
        return films;
    }

    @Override
    public User getUserById(int id) {
        return null;
    }

    private Map<Integer, Set<Integer>> makeFriendsMap(SqlRowSet sqlRowSet) {
        Map<Integer, Set<Integer>> friendsMap = new HashMap<>();
        while (sqlRowSet.next()) {
            int userId = sqlRowSet.getInt("USER_ID");
            Set<Integer> friendSet;
            if (friendsMap.containsKey(userId)) {
                friendSet = friendsMap.get(userId);
            } else {
                friendSet = new HashSet<>();
            }
            friendSet.add(sqlRowSet.getInt("FRIEND_ID"));
            friendsMap.put(userId, friendSet);
        }
        return friendsMap;
    }
}
