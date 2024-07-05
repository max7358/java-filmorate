package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@SpringBootTest
@AutoConfigureTestDatabase
class UserValidationTests {

    private static Validator validator;

    @BeforeAll
    public static void init() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void emailProperValidationTest() {
        User user = User.builder().name("name").email("email").login("login")
                .birthday(LocalDate.of(1990, 1, 1)).build();
        Set<ConstraintViolation<User>> validate = validator.validate(user);
        List<String> errorMessages = validate.stream().map(ConstraintViolation::getMessage).toList();
        Assertions.assertTrue(errorMessages.contains("must be a well-formed email address"));
    }

    @Test
    void emailBlankProperValidationTest() {
        User user = User.builder().name("name").email("").login("login")
                .birthday(LocalDate.of(1990, 1, 1)).build();
        Set<ConstraintViolation<User>> validate = validator.validate(user);
        List<String> errorMessages = validate.stream().map(ConstraintViolation::getMessage).toList();
        Assertions.assertTrue(errorMessages.contains("must not be blank"));
    }

    @Test
    void loginValidationTest() {
        User user = User.builder().name("name").email("email@bb.mail").login("login b")
                .birthday(LocalDate.of(1990, 1, 1)).build();
        Set<ConstraintViolation<User>> validate = validator.validate(user);
        List<String> errorMessages = validate.stream().map(ConstraintViolation::getMessage).toList();
        Assertions.assertTrue(errorMessages.contains("Invalid login: cant be empty or contain whitespaces"));
    }

    @Test
    void birthdayValidationTest() {
        User user = User.builder().name("name").email("email@bb.mail").login("login")
                .birthday(LocalDate.of(2990, 1, 1)).build();
        Set<ConstraintViolation<User>> validate = validator.validate(user);
        List<String> errorMessages = validate.stream().map(ConstraintViolation::getMessage).toList();
        Assertions.assertTrue(errorMessages.contains("must be a past date"));
    }
}
