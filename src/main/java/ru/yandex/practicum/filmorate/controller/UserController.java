package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public List<User> getUsers() {
        log.debug("GET all users");
        return userService.getAll();
    }

    @PostMapping()
    public User createUser(@Valid @RequestBody User user) {
        log.debug("POST user {}", user);
        return userService.create(user);
    }

    @PutMapping()
    public User updateUser(@Valid @RequestBody User user) {
        log.debug("PUT user {}", user);
        return userService.update(user);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        log.debug("GET User with id: {}", id);
        return userService.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.debug("PUT user {} add friend {}", id, friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.debug("DELETE user {} delete friend {}", id, friendId);
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
        log.debug("GET friends {}", id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getFriendsCommon(@PathVariable Long id, @PathVariable Long otherId) {
        log.debug("GET common friends {} otherId {}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }
}
