package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {


    private static final String WRONG_ID_MESSAGE = "Wrong id";
    private static int id = 0;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User addUser(User user) {
        user.setId(++id);
        users.put(user.getId(), user);
        log.info("user added");
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("user added");
            return user;
        }
        throw new NullPointerException(WRONG_ID_MESSAGE);
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(int id) {
        if (users.containsKey(id)) return users.get(id);
        throw new NullPointerException(WRONG_ID_MESSAGE);
    }


}
