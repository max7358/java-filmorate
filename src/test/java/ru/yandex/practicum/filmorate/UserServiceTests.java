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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.dal.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.dal.mappers.UserMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {UserDbStorage.class, UserMapper.class, UserService.class})
class UserServiceTests {
    @Autowired
    UserService userService;
    private User firstUser;
    private User secondUser;

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
    }

    @Test
    void testCreateUser() {
        User user = userService.create(firstUser);
        userService.getUserById(user.getId());
        Assertions.assertThat(firstUser).isEqualTo(user);
    }

    @Test
    void testUpdateUser() {
        User user = userService.create(firstUser);
        User updateUser = User.builder()
                .id(firstUser.getId())
                .name("updateN")
                .login("updateL")
                .email("update@email.com")
                .birthday(LocalDate.of(2011, 12, 30))
                .friends(Set.of())
                .build();
        userService.update(updateUser);
        user = userService.getUserById(user.getId());
        Assertions.assertThat(updateUser).isEqualTo(user);
    }

    @Test
    void testUpdateUserNotExist() {
        User updateUser = User.builder()
                .id(9999999L)
                .name("updateN")
                .login("updateL")
                .email("update@email.com")
                .birthday(LocalDate.of(2011, 12, 30))
                .friends(Set.of())
                .build();
        assertThrows(NotFoundException.class, () -> userService.update(updateUser));
    }

    @Test
    void testFindUserByIdNotExist() {
        assertThrows(NotFoundException.class, () -> userService.getUserById(99999L));
    }

    @Test
    void testGetAllUsers() {
        userService.create(firstUser);
        userService.create(secondUser);
        List<User> users = userService.getAll();
        Assertions.assertThat(users).isNotEmpty().hasSize(2).contains(firstUser, secondUser);
    }

    @Test
    void testGetAllUsersZero() {
        List<User> users = userService.getAll();
        Assertions.assertThat(users).isEmpty();
    }

    @Test
    void testGetFriendsZero() {
        firstUser = userService.create(firstUser);
        Assertions.assertThat(userService.getFriends(firstUser.getId())).isEmpty();
    }

    @Test
    void testGetCommonFriendsZero(){
        firstUser = userService.create(firstUser);
        secondUser = userService.create(secondUser);
        Assertions.assertThat(userService.getCommonFriends(firstUser.getId(), secondUser.getId())).isEmpty();
    }

    @Test
    void testGetCommonFriendsNotExist(){
        firstUser = userService.create(firstUser);
        secondUser = userService.create(secondUser);
        assertThrows(NotFoundException.class, () ->userService.getCommonFriends(11111L, 222222L));
    }

}
