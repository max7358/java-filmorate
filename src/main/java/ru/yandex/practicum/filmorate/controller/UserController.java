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
}
