package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserControllerTests {


    @Test
    public void checkValidity() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();
            UserController userController = new UserController(new InMemoryUserStorage());
            User user1 = new User(1, "test@mail", "login1", "name",
                    LocalDate.of(2023, 4, 12));
            User user2 = new User(2, "mail", "login1", "name",
                    LocalDate.of(2023, 4, 12));
            User user3 = new User(3, "test@mail", "login1", "name",
                    LocalDate.now().plusDays(1));

            assertTrue(validator.validate(user1).isEmpty());
            assertFalse(validator.validate(user2).isEmpty());
            assertFalse(validator.validate(user3).isEmpty());
        }
    }

    @Test
    public void checkValidLogin() {
        UserController userController = new UserController(new InMemoryUserStorage());
        User user1 = new User(1, "test@mail", "login1", "name",
                LocalDate.of(2023, 4, 12));
        User user2 = new User(2, "test2@mail", "lo gin2", "name",
                LocalDate.of(2023, 4, 12));
        assertEquals(user1, userController.addUser(user1));
        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() {
                userController.addUser(user2);
            }
        });
    }

    @Test
    public void generateName() {
        UserController userController = new UserController(new InMemoryUserStorage());
        User user1 = userController.addUser(new User(3, "test@mail", "login1", null,
                LocalDate.of(2023, 4, 12)));

        assertEquals("login1", user1.getName());
    }
}
