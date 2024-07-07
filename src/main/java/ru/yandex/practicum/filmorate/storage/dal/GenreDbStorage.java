package ru.yandex.practicum.filmorate.storage.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.NotImplementedException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@Slf4j
public class GenreDbStorage extends BaseRepository<Genre> implements GenreStorage {
    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    private static final String INSERT_FILM_GENRES_QUERY = "INSERT INTO films_genres(film_id, genre_id)" +
            "VALUES (?, ?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genres WHERE id = ?";
    private static final String DELETE_GENRES_BY_FILM_ID_QUERY = "DELETE FROM films_genres WHERE film_id = ?";
    private static final String FIND_GENRES_BY_FILM_ID_QUERY = "SELECT films_genres.genre_id,genres.name FROM films_genres " +
            "INNER JOIN genres on films_genres.genre_id = genres.id  WHERE film_id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM genres ORDER BY id ASC";
    private static final String NOT_IMPLEMENTED_MESSAGE = "Not implemented yet";

    @Override
    public Genre create(Genre genre) {
        throw new NotImplementedException(NOT_IMPLEMENTED_MESSAGE);
    }

    @Override
    public Genre update(Genre genre) {
        throw new NotImplementedException(NOT_IMPLEMENTED_MESSAGE);
    }

    @Override
    public List<Genre> findAll() {
        List<Genre> genres = findMany(FIND_ALL_QUERY);
        log.debug("Found genres: {}", genres);
        return genres;
    }

    @Override
    public Genre findById(Long id) {
        Genre genre = findOne(FIND_BY_ID_QUERY, id).orElseThrow(() -> new NotFoundException("Genre with id:" + id + " not found"));
        log.debug("Found genre: {}", genre);
        return genre;
    }

    @Override
    public void insertFilmGenre(Long filmId, Long genreId) {
        insert(INSERT_FILM_GENRES_QUERY, filmId, genreId);
        log.debug("Inserted film: {} genre: {}", filmId, genreId);
    }

    @Override
    public Set<Genre> findGenresByFilmId(Long id) {
        List<Genre> genreList = jdbc.query(FIND_GENRES_BY_FILM_ID_QUERY, (rs, row) ->
                new Genre(rs.getLong("genre_id"), rs.getString("name")), id);
        log.debug("Found genres: {}", genreList);
        return new HashSet<>(genreList);
    }

    @Override
    public void deleteFilmGenres(Long id) {
        delete(DELETE_GENRES_BY_FILM_ID_QUERY, id);
        log.debug("Deleted genres from film: {}", id);
    }
}
