package ru.yandex.practicum.filmorate.storage.friend;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class FriendDbStorage implements FriendStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;

    @Override
    public Set<Integer> addFriend(int userId, int friendId) {
        userStorage.getUserById(userId);
        getFriends(friendId);
        String query = "INSERT INTO PUBLIC.USER_FRIENDS (USER_ID, FRIEND_ID, STATUS_ID) VALUES (?, ?,1);";
        jdbcTemplate.update(query, userId, friendId);
        return getFriends(userId);
    }

    public Set<Integer> getFriends(int userId) {
        userStorage.getUserById(userId);
        String query = "Select FRIEND_ID From PUBLIC.USER_FRIENDS Where USER_ID = ?;";
        return new HashSet<>(jdbcTemplate.query(query, (rs, row) -> rs.getInt("FRIEND_ID"), userId));
    }

    @Override
    public Set<Integer> deleteFriend(int userId, int friendId) {
        userStorage.getUserById(userId);
        getFriends(friendId);
        String query = "DELETE from PUBLIC.USER_FRIENDS where USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(query, userId, friendId);
        return getFriends(userId);
    }
}
