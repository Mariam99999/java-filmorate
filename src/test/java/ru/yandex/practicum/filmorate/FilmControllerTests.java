package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class FilmControllerTests {

    @Test
    public void checkValidity() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();
            Film film1 = new Film(1, "name", "test1",
                    LocalDate.of(1895, 12, 28), 120.0);
            Film film2 = new Film(2, "", "test1",
                    LocalDate.of(1895, 12, 28), 120.0);
            String someLongText = "Behind me, field and meadow sleeping,\n" +
                    "I leave in deep, prophetic night,\n" +
                    "Within whose dread and holy keeping\n" +
                    "The better soul awakes to light.\n" +
                    "The wild desires no longer win us,\n" +
                    "The deeds of passion cease to chain;\n" +
                    "The love of Man revives within us,\n" +
                    "The love of God revives again.\n" +
                    "Be still, thou poodle; make not such racket and riot!\n" +
                    "Why at the threshold wilt snuffing be?\n" +
                    "Behind the stove repose thee in quiet!\n" +
                    "My softest cushion I give to thee.\n" +
                    "As thou, up yonder, with running and leaping\n" +
                    "Amused us hast, on the mountain's crest,\n" +
                    "So now I take thee into my keeping,\n" +
                    "A welcome, but also a silent, guest.";

            Film film3 = new Film(3, "name3", someLongText,
                    LocalDate.of(1895, 12, 28), 120.0);
            Film film4 = new Film(4, "name3", "description",
                    null, 120.0);
            Film film5 = new Film(5, "name3", someLongText,
                    LocalDate.of(1895, 12, 28), -120.0);
            assertTrue(validator.validate(film1).isEmpty());
            assertFalse(validator.validate(film2).isEmpty());
            assertFalse(validator.validate(film3).isEmpty());
            assertFalse(validator.validate(film4).isEmpty());
            assertFalse(validator.validate(film5).isEmpty());

            Film filmAnnotationRealiseDate = new Film(0, "name", "test1",
                    LocalDate.of(1893, 12, 28), 120.0);
            assertFalse(validator.validate(filmAnnotationRealiseDate).isEmpty());
        }
    }


//    @Test
//    public void checkValidDate() {
//
//        FilmController filmController = new FilmController();
//        Film film1 = new Film(0, "name", "test1",
//                LocalDate.of(1895, 12, 28), 120.0);
//        Film film2 = new Film(5, "name2", "test2",
//                LocalDate.of(1895, 12, 27), 120.0);
//        Film film3 = new Film(7, "name3", "test3",
//                LocalDate.of(1895, 12, 26), 120.0);
//        assertEquals(film1, filmController.addFilm(film1));
//        assertEquals(film2, filmController.addFilm(film2));
//        assertThrows(ValidationException.class, new Executable() {
//            @Override
//            public void execute() {
//                filmController.addFilm(film3);
//            }
//        });
//    }
}
