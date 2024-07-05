package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
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
import java.util.Optional;
import java.util.Set;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {UserDbStorage.class, UserMapper.class, UserService.class, FilmDbStorage.class, FilmMapper.class,
        GenreDbStorage.class, GenreMapper.class, GenreService.class, RatingDbStorage.class, RatingMapper.class, RatingService.class})
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;

    private User firstUser;
    private User secondUser;
    private User thirdUser;
    private Film firstFilm;
    private Film secondFilm;
    @Autowired
    private UserService userService;

    @BeforeEach
    public void beforeEach() {
        firstUser = User.builder()
                .name("userOne")
                .login("user1")
                .email("someone1@email.com")
                .birthday(LocalDate.of(2000, 1, 1))
                .friends(Set.of())
                .build();

        secondUser = User.builder()
                .name("userTwo")
                .login("user2")
                .email("someone2@email.com")
                .birthday(LocalDate.of(2001, 2, 3))
                .friends(Set.of())
                .build();

        thirdUser = User.builder()
                .id(firstUser.getId())
                .name("userThree")
                .login("update3")
                .email("sm3@email.com")
                .birthday(LocalDate.of(2001, 12, 30))
                .friends(Set.of())
                .build();

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
    void testCreateAndFindUserById() {
        userStorage.create(firstUser);
        Optional<User> userOptional = Optional.ofNullable(userStorage.findById(firstUser.getId()));
        Assertions.assertThat(userOptional).isPresent().hasValue(firstUser);
    }

    @Test
    void testGetAllUsers() {
        userStorage.create(firstUser);
        userStorage.create(secondUser);
        List<User> users = userStorage.findAll();
        Assertions.assertThat(users).isNotEmpty().hasSize(2).contains(firstUser, secondUser);
    }

    @Test
    void testUpdateUser() {
        userStorage.create(firstUser);
        User updateUser = User.builder()
                .id(firstUser.getId())
                .name("updateN")
                .login("updateL")
                .email("update@email.com")
                .birthday(LocalDate.of(2011, 12, 30))
                .friends(Set.of())
                .build();
        userStorage.update(updateUser);
        Optional<User> userOptional = Optional.ofNullable(userStorage.findById(updateUser.getId()));
        Assertions.assertThat(userOptional).isPresent().hasValue(updateUser);
    }

    @Test
    void testCreateAndFindFilmById() {
        filmStorage.create(firstFilm);
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.findById(firstFilm.getId()));
        Assertions.assertThat(filmOptional).isPresent().hasValue(firstFilm);
    }

    @Test
    void testGetAllFilms() {
        filmStorage.create(firstFilm);
        filmStorage.create(secondFilm);
        List<Film> films = filmStorage.findAll();
        Assertions.assertThat(films).isNotEmpty().hasSize(2).contains(firstFilm, secondFilm);
    }

    @Test
    void testUpdateFilm() {
        filmStorage.create(firstFilm);
        Film updateFilm = Film.builder().id(firstFilm.getId())
                .name("FilmU")
                .description("DescriptionU")
                .releaseDate(LocalDate.of(1951, 5, 20))
                .duration(61)
                .mpa(new MPA(2L, "PG"))
                .genres(Set.of(new Genre(1L, "Комедия")))
                .likes(Set.of())
                .build();
        filmStorage.update(updateFilm);
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.findById(updateFilm.getId()));
        Assertions.assertThat(filmOptional).isPresent().hasValue(updateFilm);
    }

    @Test
    void testAddLike() {
        firstUser = userStorage.create(firstUser);
        firstFilm = filmStorage.create(firstFilm);
        filmStorage.addLike(firstFilm.getId(), firstUser.getId());

        firstFilm = filmStorage.findById(firstFilm.getId());
        Assertions.assertThat(firstFilm.getLikes()).hasSize(1);
        Assertions.assertThat(firstFilm.getLikes()).contains(firstUser.getId());
    }

    @Test
    void testDeleteLike() {
        firstUser = userStorage.create(firstUser);
        secondUser = userStorage.create(secondUser);
        firstFilm = filmStorage.create(firstFilm);

        filmStorage.addLike(firstFilm.getId(), firstUser.getId());
        filmStorage.addLike(firstFilm.getId(), secondUser.getId());
        firstFilm = filmStorage.findById(firstFilm.getId());
        Assertions.assertThat(firstFilm.getLikes()).hasSize(2);

        filmStorage.deleteLike(firstUser.getId());
        firstFilm = filmStorage.findById(firstFilm.getId());
        Assertions.assertThat(firstFilm.getLikes()).hasSize(1);
        Assertions.assertThat(firstFilm.getLikes()).contains(secondUser.getId());
    }

    @Test
    void testGetPopular() {
        firstUser = userStorage.create(firstUser);
        secondUser = userStorage.create(secondUser);
        firstFilm = filmStorage.create(firstFilm);
        secondFilm = filmStorage.create(secondFilm);

        filmStorage.addLike(firstFilm.getId(), firstUser.getId());
        filmStorage.addLike(secondFilm.getId(), firstUser.getId());
        filmStorage.addLike(secondFilm.getId(), secondUser.getId());
        firstFilm = filmStorage.findById(firstFilm.getId());
        secondFilm = filmStorage.findById(secondFilm.getId());

        List<Film> popular = filmStorage.getPopular(10);
        Assertions.assertThat(popular).isNotEmpty().hasSize(2);
        Assertions.assertThat(popular.getFirst()).isEqualTo(secondFilm);
        Assertions.assertThat(popular.getLast()).isEqualTo(firstFilm);
    }

    @Test
    void testAddFriend() {
        firstUser = userStorage.create(firstUser);
        secondUser = userStorage.create(secondUser);

        userService.addFriend(firstUser.getId(), secondUser.getId());
        Assertions.assertThat(userService.getFriends(firstUser.getId())).hasSize(1);
        Assertions.assertThat(userService.getFriends(firstUser.getId())).contains(secondUser);
        Assertions.assertThat(userService.getFriends(secondUser.getId())).isEmpty();
    }

    @Test
    void testDeleteFriend() {
        firstUser = userStorage.create(firstUser);
        secondUser = userStorage.create(secondUser);

        userService.addFriend(firstUser.getId(), secondUser.getId());
        userService.deleteFriend(firstUser.getId(), secondUser.getId());
        Assertions.assertThat(userService.getFriends(firstUser.getId())).isEmpty();
    }

    @Test
    void testGetFriends() {
        firstUser = userStorage.create(firstUser);
        secondUser = userStorage.create(secondUser);
        thirdUser = userStorage.create(thirdUser);

        userService.addFriend(firstUser.getId(), secondUser.getId());
        userService.addFriend(firstUser.getId(), thirdUser.getId());
        Assertions.assertThat(userService.getFriends(firstUser.getId())).hasSize(2);
        Assertions.assertThat(userService.getFriends(firstUser.getId())).contains(secondUser,thirdUser);
    }

    @Test
    void testGetCommonFriends() {
        firstUser = userStorage.create(firstUser);
        secondUser = userStorage.create(secondUser);
        thirdUser = userStorage.create(thirdUser);

        userService.addFriend(firstUser.getId(), secondUser.getId());
        userService.addFriend(firstUser.getId(), thirdUser.getId());
        userService.addFriend(secondUser.getId(), firstUser.getId());
        userService.addFriend(secondUser.getId(), thirdUser.getId());

        Assertions.assertThat(userService.getCommonFriends(firstUser.getId(), secondUser.getId())).hasSize(1);
        Assertions.assertThat(userService.getCommonFriends(firstUser.getId(), secondUser.getId()))
                .contains(thirdUser);
    }
}