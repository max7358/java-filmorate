package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    @Getter
    private final HashMap<Long, User> users = new HashMap<>();
    private Long id = 1L;

    @Override
    public User create(User user) {
        user.setId(id++);
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
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

    @Override
    public User findById(Long id) {
        return Optional.ofNullable(users.get(id)).orElseThrow(() -> new NotFoundException("User with id:" + id + " not found"));
    }
}
