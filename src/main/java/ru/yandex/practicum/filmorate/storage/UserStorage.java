package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage extends CommonStorage<User> {
    void addFriend(Long userId, Long friendId, boolean status);

    void deleteFriend(Long userId, Long friendId);

    List<User> getFriends(Long id);

    void updateFriend(Long userId, Long friendId, boolean status);
}
