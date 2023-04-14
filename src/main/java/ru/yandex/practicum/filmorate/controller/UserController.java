package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j

public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private static int id = 0;
    private static final String WRONG_LOGIN_MESSAGE = "Login can't contain blank space";
    private static final String WRONG_ID_MESSAGE = "Wrong id";


    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        checkValidLogin(user);
        generateName(user);
        user.setId(++id);
        users.put(user.getId(), user);
        log.info("user added");
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        checkValidLogin(user);
        if (users.containsKey(user.getId())) {
            generateName(user);
            users.put(user.getId(), user);
            log.info("user added");
            return user;
        }
        log.error(WRONG_ID_MESSAGE);
        throw new ValidationException(WRONG_ID_MESSAGE);
    }

    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    private void checkValidLogin(User user) {
        if (user.getLogin().contains(" ")) {
            log.error(WRONG_LOGIN_MESSAGE);
            throw new ValidationException(WRONG_LOGIN_MESSAGE);
        }
    }

    private void generateName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

}
