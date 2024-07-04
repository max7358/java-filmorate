package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage extends CommonStorage<Film> {
    void addLike(Long filmId, Long userId);

    void deleteLike(Long userId);

    List<Film> getPopular(Integer count);
}
