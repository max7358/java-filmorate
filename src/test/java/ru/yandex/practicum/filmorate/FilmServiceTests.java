package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.RatingService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.dal.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.dal.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.dal.RatingDbStorage;
import ru.yandex.practicum.filmorate.storage.dal.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.dal.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.storage.dal.mappers.GenreMapper;
import ru.yandex.practicum.filmorate.storage.dal.mappers.RatingMapper;
import ru.yandex.practicum.filmorate.storage.dal.mappers.UserMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {FilmDbStorage.class, FilmMapper.class, FilmService.class, RatingService.class,
        RatingDbStorage.class, RatingMapper.class, GenreMapper.class, GenreService.class, GenreDbStorage.class,
        UserService.class, UserDbStorage.class, UserMapper.class})
class FilmServiceTests {

    @Autowired
    private FilmService filmService;
    private Film firstFilm;
    private Film secondFilm;

    @BeforeEach
    public void beforeEach() {
        firstFilm = Film.builder()
                .name("Film1")
                .description("Description1")
                .releaseDate(LocalDate.of(1950, 5, 20))
                .duration(60)
                .mpa(new MPA(1L, "G"))
                .genres(Set.of(new Genre(1L, "Комедия"), new Genre(2L, "Драма")))
                .likes(Set.of())
                .build();

        secondFilm = Film.builder()
                .name("Film2")
                .description("Description2")
                .releaseDate(LocalDate.of(1960, 6, 2))
                .duration(70)
                .mpa(new MPA(2L, "PG"))
                .genres(Set.of(new Genre(1L, "Комедия"), new Genre(2L, "Драма")))
                .likes(Set.of())
                .build();
    }

    @Test
    void testCreateFilm() {
        Film film = filmService.create(firstFilm);
        Assertions.assertThat(firstFilm).isEqualTo(film);
    }

    @Test
    void testUpdateFilm() {
        filmService.create(firstFilm);
        Film updateFilm = Film.builder().id(firstFilm.getId())
                .name("FilmU")
                .description("DescriptionU")
                .releaseDate(LocalDate.of(1951, 5, 20))
                .duration(61)
                .mpa(new MPA(2L, "PG"))
                .genres(Set.of(new Genre(1L, "Комедия")))
                .likes(Set.of())
                .build();
        Film film = filmService.update(updateFilm);
        Assertions.assertThat(updateFilm).isEqualTo(film);
    }

    @Test
    void testUpdateFilmNotExist() {
        Film updateFilm = Film.builder().id(99999L)
                .name("FilmU")
                .description("DescriptionU")
                .releaseDate(LocalDate.of(1951, 5, 20))
                .duration(61)
                .mpa(new MPA(2L, "PG"))
                .genres(Set.of(new Genre(1L, "Комедия")))
                .likes(Set.of())
                .build();
        assertThrows(NotFoundException.class, () -> filmService.update(updateFilm));
    }

    @Test
    void testFindFilmByIdNotExist() {
        assertThrows(NotFoundException.class, () -> filmService.getFilmById(99999L));
    }

    @Test
    void testGetAllFilms() {
        filmService.create(firstFilm);
        filmService.create(secondFilm);
        List<Film> films = filmService.getAll();
        Assertions.assertThat(films).isNotEmpty().hasSize(2).contains(firstFilm, secondFilm);
    }

    @Test
    void testGetAllFilmsZero() {
        List<Film> films = filmService.getAll();
        Assertions.assertThat(films).isEmpty();
    }

    @Test
    void testAddLikeNotExist() {
        assertThrows(NotFoundException.class, () -> filmService.addLike(111111L, 22222L));
    }

    @Test
    void createFilmNoGenre() {
        Film film = Film.builder()
                .name("Film1")
                .description("Description1")
                .releaseDate(LocalDate.of(1950, 5, 20))
                .duration(60)
                .mpa(new MPA(1L, "G"))
                .genres(null)
                .likes(Set.of())
                .build();
        Film film1 = filmService.create(film);
        Assertions.assertThat(film1).isEqualTo(film);
    }
}
