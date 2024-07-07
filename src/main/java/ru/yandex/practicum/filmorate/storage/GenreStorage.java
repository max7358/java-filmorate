package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Set;

public interface GenreStorage extends CommonStorage<Genre> {
    void insertFilmGenre(Long filmId, Long genreId);

    Set<Genre> findGenresByFilmId(Long id);

    void deleteFilmGenres(Long id);
}
