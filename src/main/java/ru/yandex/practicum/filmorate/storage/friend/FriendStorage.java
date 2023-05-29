package ru.yandex.practicum.filmorate.storage.friend;

import java.util.Set;

public interface FriendStorage {
    Set<Integer> addFriend(int userId, int friendId);
    Set<Integer> getFriends(int userId);
    Set<Integer> deleteFriend(int userId, int friendId);
}
