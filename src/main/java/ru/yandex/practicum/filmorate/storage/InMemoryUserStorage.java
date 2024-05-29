package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Integer, User> users = new HashMap<>();
    private int id = 1;

    @Override
    public User create(User user) {
        user.setId(id++);
        users.put(user.getId(), user);
        log.info("User created: {}", user);
        return user;
    }

    @Override
    public User update(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Film updated: {}", user);
            return user;
        } else {
            throw new NotFoundException("User with id:" + user.getId() + " not found");
        }

    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }
}
