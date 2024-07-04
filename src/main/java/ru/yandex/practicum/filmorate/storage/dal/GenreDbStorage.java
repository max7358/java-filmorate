package ru.yandex.practicum.filmorate.storage.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
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

    @Override
    public Genre create(Genre genre) {
        return null;
    }

    @Override
    public Genre update(Genre genre) {
        return null;
    }

    @Override
    public List<Genre> findAll() {
        return List.of();
    }

    @Override
    public Genre findById(Long id) {
        return findOne(FIND_BY_ID_QUERY, id).orElseThrow(() -> new BadRequestException("Genre with id:" + id + " not found"));
    }

    @Override
    public void insertFilmGenre(Long filmId, Long genreId) {
        insert(INSERT_FILM_GENRES_QUERY, filmId, genreId);
    }

    @Override
    public Set<Genre> findGenresByFilmId(Long id) {
        List<Genre> genreList = jdbc.query(FIND_GENRES_BY_FILM_ID_QUERY, (rs, row) ->
                new Genre(rs.getLong("genre_id"), rs.getString("name")), id);
        return new HashSet<>(genreList);
    }

    @Override
    public void deleteFilmGenres(Long id) {
        delete(DELETE_GENRES_BY_FILM_ID_QUERY, id);
    }
}
