package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UserControllerTests {
    @Test
    public void checkValidity() {
        UserController userController = new UserController();
        User user1 = new User(1, "test@mail", "name33", "name", LocalDate.of(2023, 4, 12));
        User user2 = new User(2, "test2@mail", "name233", "name", LocalDate.now());
        User user3 = new User(3, "test3@mail", "name333", "name", LocalDate.of(2024, 4, 12));
        User user4 = new User(4, "test3@mail", "name333", "", LocalDate.of(2021, 4, 12));
        assertTrue(userController.checkValidity(user1));
        assertTrue(userController.checkValidity(user2));
        assertFalse(userController.checkValidity(user3));
        assertTrue(userController.checkValidity(user4));
    }
}
