package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@SpringBootTest
@AutoConfigureTestDatabase
class FilmValidationTests {

    private static Validator validator;

    @BeforeAll
    public static void init() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void nameValidationTest() {
        Film film = Film.builder().name("").description("description").duration(10)
                .releaseDate(LocalDate.of(2023, 10, 10)).build();
        Set<ConstraintViolation<Film>> validate = validator.validate(film);
        List<String> errorMessages = validate.stream().map(ConstraintViolation::getMessage).toList();
        Assertions.assertTrue(errorMessages.contains("must not be blank"));
    }

    @Test
    void descriptionValidationTest() {
        Film film = Film.builder().name("film").description("Description X".repeat(20)).duration(10)
                .releaseDate(LocalDate.of(2023, 10, 10)).build();
        Set<ConstraintViolation<Film>> validate = validator.validate(film);
        List<String> errorMessages = validate.stream().map(ConstraintViolation::getMessage).toList();
        Assertions.assertTrue(errorMessages.contains("size must be between 0 and 200"));
    }

    @Test
    void releaseDateValidationTest() {
        Film film = Film.builder().name("film").description("description").duration(10)
                .releaseDate(LocalDate.of(1723, 10, 10)).build();
        Set<ConstraintViolation<Film>> validate = validator.validate(film);
        List<String> errorMessages = validate.stream().map(ConstraintViolation::getMessage).toList();
        Assertions.assertTrue(errorMessages.contains("Invalid date: must be after 1895-12-27"));
    }

    @Test
    void durationValidationTest() {
        Film film = Film.builder().name("film").description("description").duration(0)
                .releaseDate(LocalDate.of(2023, 10, 10)).build();
        Set<ConstraintViolation<Film>> validate = validator.validate(film);
        List<String> errorMessages = validate.stream().map(ConstraintViolation::getMessage).toList();
        Assertions.assertTrue(errorMessages.contains("must be greater than 0"));
    }
}
