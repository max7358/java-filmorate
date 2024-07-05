package ru.yandex.practicum.filmorate.storage.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@Qualifier("userDbStorage")
@Slf4j
public class UserDbStorage extends BaseRepository<User> implements UserStorage {
    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    private static final String INSERT_QUERY = "INSERT INTO users(email, login, name, birthday)" +
            "VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET email = ?, login = ?, name = ?," +
            "birthday = ? WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String INSERT_FRIEND_QUERY = "INSERT INTO friends (user_id, friend_id, status) VALUES (?, ?, ?)";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
    private static final String FIND_FRIENDS_QUERY = "SELECT friend_id as id, email, login, name, birthday FROM friends " +
            "INNER JOIN users ON friends.friend_id = users.id WHERE friends.user_id = ?";
    private static final String UPDATE_FRIEND_STATUS = "UPDATE friends SET status = ? WHERE user_id = ? AND friend_id = ?";


    @Override
    public User create(User user) {
        long id = insertOneKey(INSERT_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        user.setId(id);
        log.debug("Created user: {}", user);
        return user;
    }

    @Override
    public User update(User user) {
        update(UPDATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        log.debug("Updated user: {}", user);
        return user;
    }

    @Override
    public List<User> findAll() {
        List<User> users = findMany(FIND_ALL_QUERY);
        users.forEach(this::fillFriends);
        log.debug("Found users: {}", users);
        return users;
    }

    @Override
    public User findById(Long id) {
        User user = findOne(FIND_BY_ID_QUERY, id).orElseThrow(() -> new NotFoundException("User with id:" + id + " not found"));
        fillFriends(user);
        log.debug("Found user: {}", user);
        return user;
    }

    @Override
    public void addFriend(Long userId, Long friendId, boolean status) {
        insert(INSERT_FRIEND_QUERY, userId, friendId, status);
        log.debug("Added friend: {} to user: {}", friendId, userId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        delete(DELETE_FRIEND_QUERY, userId, friendId);
        log.debug("Deleted friend: {} from user: {}", friendId, userId);
    }

    @Override
    public List<User> getFriends(Long id) {
        List<User> friends = findMany(FIND_FRIENDS_QUERY, id);
        log.debug("Found friends: {}", friends);
        return friends;
    }

    @Override
    public void updateFriend(Long userId, Long friendId, boolean status) {
        update(UPDATE_FRIEND_STATUS, status, userId, friendId);
        log.debug("Updated friend: {} to user: {}", friendId, userId);
    }

    private void fillFriends(User user) {
        user.setFriends(getFriends(user.getId()).stream().map(User::getId).collect(Collectors.toSet()));
    }
}
