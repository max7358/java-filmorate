package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;
import java.util.Set;

@Service
public class GenreService {
    private GenreStorage genreStorage;

    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Genre getGenreById(Long id) {
        return genreStorage.findById(id);
    }

    public void addFilmGenres(Film film) {
        film.getGenres().forEach(genre -> genreStorage.insertFilmGenre(film.getId(), genre.getId()));
    }

    public Set<Genre> getGenresByFilmId(Long id) {
        return genreStorage.findGenresByFilmId(id);
    }

    public void deleteFilmGenres(Long id) {
        genreStorage.deleteFilmGenres(id);
    }

    public List<Genre> getAll() {
        return genreStorage.findAll();
    }
}
