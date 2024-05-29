package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController() {
        this.userService = new UserService(new InMemoryUserStorage());
    }

    @GetMapping()
    public List<User> getUsers() {
        return userService.getAll();
    }

    @PostMapping()
    public User createUser(@RequestBody
                           @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING) User user) {
        return userService.create(user);
    }

    @PutMapping()
    public User updateUser(@RequestBody User user) {
        return userService.update(user);
    }
}
