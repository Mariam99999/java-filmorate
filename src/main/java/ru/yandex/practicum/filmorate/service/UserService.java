package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final InMemoryUserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public void addFriend(int id, int friendId) {
        User user1 = userStorage.getUserById(id);
        User user2 = userStorage.getUserById(friendId);
        user1.addFriend(friendId);
        user2.addFriend(id);

    }

    public void deleteFriend(int id, int friedId) {
        userStorage.getUserById(id).deleteFriend(friedId);
        userStorage.getUserById(friedId).deleteFriend(id);

    }

    public List<User> getFriends(int id) {
        Set<Integer> friends = userStorage.getUserById(id).getFriends();
        return friends.stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());

    }


    public List<User> getGeneralFriends(int id, int otherId) {
        Set<Integer> friends1 = userStorage.getUserById(id).getFriends();
        Set<Integer> friends2 = userStorage.getUserById(otherId).getFriends();
        if (friends1 == null || friends2 == null) return List.of();
        List<Integer> ids = friends1.stream().filter(friends2::contains).collect(Collectors.toList());
        return userStorage.getUsers().stream().filter(u -> ids.contains(u.getId())).collect(Collectors.toList());
    }

}
