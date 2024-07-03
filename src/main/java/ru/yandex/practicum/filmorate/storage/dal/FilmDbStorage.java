package ru.yandex.practicum.filmorate.storage.dal;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.RatingService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Repository
@Qualifier("filmDbStorage")
public class FilmDbStorage extends BaseRepository<Film> implements FilmStorage {

    private final RatingService ratingService;
    private final GenreService genreService;

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper, RatingService ratingService, GenreService genreService) {
        super(jdbc, mapper);
        this.ratingService = ratingService;
        this.genreService = genreService;
    }

    private static final String INSERT_QUERY = "INSERT INTO films(name, description, release_date, duration, rating_id)" +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM films";

    @Override
    public Film create(Film film) {
        long id = insert(INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId()
        );
        film.setId(id);
        return film;
    }

    @Override
    public Film update(Film film) {
        return null;
    }

    @Override
    public List<Film> findAll() {
        //TODO
        List<Film> films = findMany(FIND_ALL_QUERY);
        return films;
    }

    @Override
    public Film findById(Long id) {
        Film film = findOne(FIND_BY_ID_QUERY, id).orElseThrow(() -> new NotFoundException("Film with id:" + id + " not found"));
        film.setMpa(ratingService.findById(film.getMpa().getId()));
        return film;
    }
}
