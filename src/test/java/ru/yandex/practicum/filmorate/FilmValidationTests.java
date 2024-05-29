package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@SpringBootTest
class FilmValidationTests {

	private Film film;
	private static Validator validator;

	@BeforeAll
	public static void init() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

	@BeforeEach
	public void beforeEach() {
		film = new Film();
	}

	@Test
	void nameValidationTest() {
		film.setName("");
		film.setDescription("123");
		film.setDuration(10);
		film.setReleaseDate(LocalDate.of(2023, 10, 10));
		Set<ConstraintViolation<Film>> validate = validator.validate(film);
		List<String> errorMessages = validate.stream().map(ConstraintViolation::getMessage).toList();
		Assertions.assertEquals(1, errorMessages.size());
		Assertions.assertEquals("must not be blank", errorMessages.getFirst());
	}

	@Test
	void descriptionValidationTest() {
		film.setName("name");
		film.setDescription("Description X".repeat(20));
		film.setDuration(10);
		film.setReleaseDate(LocalDate.of(2023, 10, 10));
		Set<ConstraintViolation<Film>> validate = validator.validate(film);
		List<String> errorMessages = validate.stream().map(ConstraintViolation::getMessage).toList();
		Assertions.assertEquals(1, errorMessages.size());
		Assertions.assertEquals("size must be between 0 and 200", errorMessages.getFirst());
	}

	@Test
	void releaseDateValidationTest() {
		film.setName("name");
		film.setDescription("123");
		film.setDuration(10);
		film.setReleaseDate(LocalDate.of(1723, 10, 10));
		Set<ConstraintViolation<Film>> validate = validator.validate(film);
		List<String> errorMessages = validate.stream().map(ConstraintViolation::getMessage).toList();
		Assertions.assertEquals(1, errorMessages.size());
		Assertions.assertEquals("Invalid date: must be after 1895-12-27", errorMessages.getFirst());
	}

	@Test
	void durationValidationTest() {
		film.setName("name");
		film.setDescription("123");
		film.setDuration(-1);
		film.setReleaseDate(LocalDate.of(2023, 10, 10));
		Set<ConstraintViolation<Film>> validate = validator.validate(film);
		List<String> errorMessages = validate.stream().map(ConstraintViolation::getMessage).toList();
		Assertions.assertEquals(1, errorMessages.size());
		Assertions.assertEquals("must be greater than 0", errorMessages.getFirst());
	}
}
