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
    private static final String UPDATE_QUERY = "UPDATE films SET name = ?, description = ?, release_date = ?," +
            "duration = ?, rating_id = ? WHERE id = ?";

    @Override
    public Film create(Film film) {
        long id = insertOneKey(INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId()
        );
        film.setId(id);
        film.setMpa(ratingService.findById(film.getMpa().getId()));
        if (film.getGenres() != null) {
            film.getGenres().forEach(genre -> genre.setName(genreService.getGenreById(genre.getId()).getName()));
            genreService.addFilmGenres(film);
        }

        return film;
    }

    @Override
    public Film update(Film film) {
        update(UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        film.setMpa(ratingService.findById(film.getMpa().getId()));
        genreService.deleteFilmGenres(film.getId());
        if (film.getGenres() != null) {
            film.getGenres().forEach(genre -> genre.setName(genreService.getGenreById(genre.getId()).getName()));
            genreService.addFilmGenres(film);
        }
        return film;
    }

    @Override
    public List<Film> findAll() {
        List<Film> films = findMany(FIND_ALL_QUERY);
        films.forEach(film -> {
            film.setMpa(ratingService.findById(film.getMpa().getId()));
            film.setGenres(genreService.getGenresByFilmId(film.getId()));
        });
        return films;
    }

    @Override
    public Film findById(Long id) {
        Film film = findOne(FIND_BY_ID_QUERY, id).orElseThrow(() -> new NotFoundException("Film with id:" + id + " not found"));
        film.setMpa(ratingService.findById(film.getMpa().getId()));
        film.setGenres(genreService.getGenresByFilmId(film.getId()));
        return film;
    }
}
