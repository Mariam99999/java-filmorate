package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final String WRONG_LOGIN_MESSAGE = "Login can't contain blank space";
    private final UserStorage userStorage;

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
