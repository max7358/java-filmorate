package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@SpringBootTest
class UserValidationTests {

	private User user;
	private static Validator validator;

	@BeforeAll
	public static void init() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

	@BeforeEach
	public void beforeEach() {
		user = new User();
	}

	@Test
	void emailProperValidationTest() {
		user.setName("name");
		user.setEmail("email");
		user.setLogin("login");
		user.setBirthday(LocalDate.of(1990, 1, 1));
		Set<ConstraintViolation<User>> validate = validator.validate(user);
		List<String> errorMessages = validate.stream().map(ConstraintViolation::getMessage).toList();
		Assertions.assertEquals(1, errorMessages.size());
		Assertions.assertEquals("must be a well-formed email address", errorMessages.getFirst());
	}

	@Test
	void emailBlankProperValidationTest() {
		user.setName("name");
		user.setEmail("");
		user.setLogin("login");
		user.setBirthday(LocalDate.of(1990, 1, 1));
		Set<ConstraintViolation<User>> validate = validator.validate(user);
		List<String> errorMessages = validate.stream().map(ConstraintViolation::getMessage).toList();
		Assertions.assertEquals(1, errorMessages.size());
		Assertions.assertEquals("must not be blank", errorMessages.getFirst());
	}

	@Test
	void loginValidationTest() {
		user.setName("name");
		user.setEmail("email@bb.mail");
		user.setLogin("login b");
		user.setBirthday(LocalDate.of(1990, 1, 1));
		Set<ConstraintViolation<User>> validate = validator.validate(user);
		List<String> errorMessages = validate.stream().map(ConstraintViolation::getMessage).toList();
		Assertions.assertEquals(1, errorMessages.size());
		Assertions.assertEquals("Invalid login: cant be empty or contain whitespaces", errorMessages.getFirst());
	}

	@Test
	void birthdayValidationTest() {
		user.setName("name");
		user.setEmail("email@bb.mail");
		user.setLogin("login");
		user.setBirthday(LocalDate.of(2990, 1, 1));
		Set<ConstraintViolation<User>> validate = validator.validate(user);
		List<String> errorMessages = validate.stream().map(ConstraintViolation::getMessage).toList();
		Assertions.assertEquals(1, errorMessages.size());
		Assertions.assertEquals("must be a past date", errorMessages.getFirst());
	}
}
