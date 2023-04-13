package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j

public class UserController {
    Map<Integer, User> users = new HashMap<>();
    private static int id = 0;

    public boolean checkValidity(User user) {
        return !user.getLogin().contains(" ")
                && !user.getBirthday().isAfter(LocalDate.now());
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        if (checkValidity(user)) {
            if (user.getName() == null || user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            user.setId(++id);
            users.put(user.getId(), user);
            log.info("user added");
            return user;
        }
        log.error("invalid data");
        throw new ValidationException();
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (checkValidity(user)) {
            if (user.getName() == null || user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            if (users.containsKey(user.getId())) {
                users.put(user.getId(), user);
                log.info("user added");
                return user;
            }
        }
        log.error("invalid data");
        throw new ValidationException();
    }

    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

}
