package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public List<User> getAll() {
        return userStorage.findAll();
    }

    public void addFriend(Long userId, Long friendId) {
        userStorage.findById(userId);
        User friend = userStorage.findById(friendId);
        boolean status = false;
        if (friend.getFriends().contains(userId)) {
            status = true;
            userStorage.updateFriend(friendId, userId, status);

        }
        userStorage.addFriend(userId, friendId, status);
    }

    public List<User> getFriends(Long id) {
        userStorage.findById(id);
        return userStorage.getFriends(id);
    }

    public User getUserById(Long id) {
        return userStorage.findById(id);
    }

    public void deleteFriend(Long userId, Long friendId) {
        userStorage.findById(userId);
        User friend = userStorage.findById(friendId);
        userStorage.deleteFriend(userId, friendId);
        if (friend.getFriends().contains(userId)) {
            userStorage.updateFriend(friendId, userId, false);
        }
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        Set<Long> userFriends = userStorage.findById(userId).getFriends();
        Set<Long> otherFriends = userStorage.findById(otherId).getFriends();
        Set<Long> result = new HashSet<>(userFriends);
        result.retainAll(otherFriends);
        List<User> commonFriends = new ArrayList<>();
        result.forEach(friendId -> commonFriends.add(userStorage.findById(friendId)));
        return commonFriends;
    }
}
