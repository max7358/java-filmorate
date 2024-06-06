package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;
    private static final int DEFAULT_MAX_COUNT = 10;

    public FilmService(FilmStorage filmStorage, UserService userService) {
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
        Film film = filmStorage.findById(filmId);
        userService.getUserById(userId);
        film.getLikes().add(userId);
    }

    public Film getFilmById(Long id) {
        return filmStorage.findById(id);
    }

    public void deleteLike(Long filmId, Long userId) {
        Film film = filmStorage.findById(filmId);
        userService.getUserById(userId);
        film.getLikes().remove(userId);
    }

    public List<Film> getPopular(Integer count) {
        List<Film> allFilms = filmStorage.findAll();
        allFilms.sort(Comparator.comparingInt((Film f) -> f.getLikes().size()).reversed());
        if (count == null) {
            count = DEFAULT_MAX_COUNT;
        }
        return allFilms.stream().limit(count).toList();
    }
}
