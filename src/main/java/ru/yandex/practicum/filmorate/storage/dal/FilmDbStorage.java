package ru.yandex.practicum.filmorate.storage.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.RatingService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.HashSet;
import java.util.List;

@Repository
@Qualifier("filmDbStorage")
@Slf4j
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
    private static final String INSERT_LIKE_QUERY = "INSERT INTO films_likes(film_id, user_id)" +
            "VALUES (?, ?)";
    private static final String DELETE_LIKE_BY_USER_ID_QUERY = "DELETE FROM films_likes WHERE user_id = ?";
    private static final String FIND_LIKES_BY_FILM_ID = "SELECT user_id FROM films_likes WHERE film_id = ?";
    private static final String FIND_POPULAR_QUERY = "SELECT id, name, description, release_date, duration, rating_id " +
            "FROM films LEFT JOIN films_likes ON films.id = films_likes.film_id " +
            "GROUP BY films.id ORDER BY COUNT(films_likes.user_id) DESC LIMIT ?";

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
            //postman tests logic: missing gener - 404, film with missing gener - 400
            try {
                film.getGenres().forEach(genre -> genre.setName(genreService.getGenreById(genre.getId()).getName()));
            } catch (NotFoundException e) {
                throw new BadRequestException(e.getMessage());
            }
            genreService.addFilmGenres(film);
        }
        log.debug("Film created: {}", film);
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
        log.debug("Film updated: {}", film);
        return film;
    }

    @Override
    public List<Film> findAll() {
        List<Film> films = findMany(FIND_ALL_QUERY);
        films.forEach(film -> {
            film.setMpa(ratingService.findById(film.getMpa().getId()));
            film.setGenres(genreService.getGenresByFilmId(film.getId()));
            film.setLikes(new HashSet<>(getLikes(film.getId())));
        });
        log.debug("Films found: {}", films);
        return films;
    }

    @Override
    public Film findById(Long id) {
        Film film = findOne(FIND_BY_ID_QUERY, id).orElseThrow(() -> new NotFoundException("Film with id:" + id + " not found"));
        film.setMpa(ratingService.findById(film.getMpa().getId()));
        film.setGenres(genreService.getGenresByFilmId(film.getId()));
        film.setLikes(new HashSet<>(getLikes(id)));
        log.debug("Film found: {}", film);
        return film;
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        insert(INSERT_LIKE_QUERY, filmId, userId);
        log.debug("like added to film: {}, from user {}", filmId, userId);
    }

    @Override
    public void deleteLike(Long userId) {
        delete(DELETE_LIKE_BY_USER_ID_QUERY, userId);
        log.debug("like deleted from user: {}", userId);
    }

    @Override
    public List<Film> getPopular(Integer count) {
        List<Film> films = findMany(FIND_POPULAR_QUERY, count);
        films.forEach(film -> {
            film.setMpa(ratingService.findById(film.getMpa().getId()));
            film.setGenres(genreService.getGenresByFilmId(film.getId()));
            film.setLikes(new HashSet<>(getLikes(film.getId())));
        });
        log.debug("Popular films found: {}", films);
        return films;
    }

    public List<Long> getLikes(Long filmId) {
        return jdbc.query(FIND_LIKES_BY_FILM_ID, (rs, rowNum) -> rs.getLong("user_id"), filmId);
    }

}
