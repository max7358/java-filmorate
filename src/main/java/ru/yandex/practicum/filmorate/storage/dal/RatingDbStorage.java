package ru.yandex.practicum.filmorate.storage.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.NotImplementedException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.RatingStorage;

import java.util.List;

@Repository
public class RatingDbStorage extends BaseRepository<MPA> implements RatingStorage {
    public RatingDbStorage(JdbcTemplate jdbc, RowMapper<MPA> mapper) {
        super(jdbc, mapper);
    }

    private static final String FIND_BY_ID_QUERY = "SELECT * FROM ratings WHERE id = ?";
    private static final String NOT_IMPLEMENTED_MESSAGE = "Not implemented yet";
    private static final String FIND_ALL_QUERY = "SELECT * FROM ratings ORDER BY id ASC";

    @Override
    public MPA create(MPA mpa) {
        throw new NotImplementedException(NOT_IMPLEMENTED_MESSAGE);
    }

    @Override
    public MPA update(MPA mpa) {
        throw new NotImplementedException(NOT_IMPLEMENTED_MESSAGE);
    }

    @Override
    public List<MPA> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public MPA findById(Long id) {
        return findOne(FIND_BY_ID_QUERY, id).orElseThrow(() -> new NotFoundException("Rating with id:" + id + " not found"));
    }
}
