package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final String WRONG_LOGIN_MESSAGE = "Login can't contain blank space";
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    public User addUser(User user) {
        checkValidLogin(user);
        generateName(user);
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        checkValidLogin(user);
        generateName(user);
        return userStorage.updateUser(user);
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public Set<Integer> addFriend(int userId, int friendId) {
        return friendStorage.addFriend(userId, friendId);

    }

    public void deleteFriend(int userId, int friedId) {
        friendStorage.deleteFriend(userId, friedId);
    }

    public List<User> getFriends(int userId) {
        Set<Integer> friends = friendStorage.getFriends(userId);
        return friends.stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }


    public List<User> getGeneralFriends(int id, int otherId) {
        Set<Integer> friends1 = friendStorage.getFriends(id);
        Set<Integer> friends2 = friendStorage.getFriends(otherId);
        if (friends1 == null || friends2 == null) return List.of();
        List<Integer> ids = friends1.stream().filter(friends2::contains).collect(Collectors.toList());
        return userStorage.getUsers().stream().filter(u -> ids.contains(u.getId())).collect(Collectors.toList());
    }

    private void checkValidLogin(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException(WRONG_LOGIN_MESSAGE);
        }

    }

    private void generateName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

}
