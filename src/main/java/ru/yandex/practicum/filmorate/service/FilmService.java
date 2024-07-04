package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;
    private static final int DEFAULT_MAX_COUNT = 10;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public List<Film> getAll() {
        return filmStorage.findAll();
    }

    public void addLike(Long filmId, Long userId) {
        filmStorage.findById(filmId);
        userService.getUserById(userId);
        filmStorage.addLike(filmId, userId);
    }

    public Film getFilmById(Long id) {
        return filmStorage.findById(id);
    }

    public void deleteLike(Long filmId, Long userId) {
        filmStorage.findById(filmId);
        userService.getUserById(userId);
        filmStorage.deleteLike(userId);
    }

    public List<Film> getPopular(Integer count) {
        if (count == null) {
            count = DEFAULT_MAX_COUNT;
        }
        return filmStorage.getPopular(count);
    }
}
