package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@RequiredArgsConstructor
@Primary
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private static final String WRONG_ID_MESSAGE = "Wrong id";


    @Override
    public User addUser(User user) {

        String query = "INSERT INTO PUBLIC.USERS (NAME, LOGIN, EMAIL, BIRTHDAY)" + "VALUES (?, ?," +
                " ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(query, new String[]{"id"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getEmail());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
//       jdbcTemplate.update(query, user.getName(), user.getLogin(), user.getEmail(), user.getBirthday());
        user.setId(keyHolder.getKey().intValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        String query = "UPDATE PUBLIC.USERS" +
                " SET NAME = ?, LOGIN = ?, EMAIL = ?, BIRTHDAY= ?  WHERE ID = ?;";
        int i = jdbcTemplate.update(query, user.getName(), user.getLogin(),
                user.getEmail(), user.getBirthday(), user.getId());
        if (i == 0) throw new NullPointerException(WRONG_ID_MESSAGE);
        updateUserFriends(user);
        return user;

    }


    @Override
    public List<User> getUsers() {
        String friendsQuery = "SELECT * FROM PUBLIC.USER_FRIENDS";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(friendsQuery);
        Map<Integer, Set<Integer>> friendsMap = makeFriendsMap(sqlRowSet);

        String query = " SELECT *  FROM PUBLIC.USERS";
        return jdbcTemplate.query(query, (rs, row) -> makeUser(rs, friendsMap));
    }

    @Override
    public User getUserById(int id) {
        String friendsQuery = "SELECT * FROM PUBLIC.USER_FRIENDS WHERE USER_ID = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(friendsQuery, id);
        Map<Integer, Set<Integer>> friendsMap = makeFriendsMap(sqlRowSet);


        String userQuery = " SELECT *  FROM PUBLIC.USERS WHERE ID = ?";
        List<User> users = jdbcTemplate.query(userQuery, (rs, row) -> makeUser(rs, friendsMap), id);
        if (users.isEmpty()) throw new NullPointerException(WRONG_ID_MESSAGE);
        return users.get(0);
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

    private User makeUser(ResultSet rs, Map<Integer, Set<Integer>> friendsMap) throws SQLException {
        int id = rs.getInt("ID");
        Set<Integer> friends = friendsMap.get(id);
        User user = new User(id, rs.getString("EMAIL"), rs.getString("LOGIN"), rs.getString("NAME"),
                rs.getDate("BIRTHDAY").toLocalDate());
        if (friends != null) {
            for (int friendId : friends) {
                user.addFriend(friendId);
            }
        }
        return user;

    }

    private void updateUserFriends(User user) {
        String queryDeleteOldFriends = "DELETE from PUBLIC.USER_FRIENDS where USER_ID = ?";
        jdbcTemplate.update(queryDeleteOldFriends, user.getId());

        String queryAddNewFriends = "INSERT INTO PUBLIC.USER_FRIENDS  (USER_ID, FRIEND_ID, STATUS_ID)" +
                "VALUES (?, ?, 1);";
        for (int friendId : user.getFriends()) {
            jdbcTemplate.update(queryAddNewFriends, user.getId(), friendId);
        }
    }
}
