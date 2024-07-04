package ru.yandex.practicum.filmorate.storage.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.RatingStorage;

import java.util.List;

@Repository
public class RatingDbStorage extends BaseRepository<MPA> implements RatingStorage {
    public RatingDbStorage(JdbcTemplate jdbc, RowMapper<MPA> mapper) {
        super(jdbc, mapper);
    }

    private static final String FIND_BY_ID_QUERY = "SELECT * FROM ratings WHERE id = ?";

    @Override
    public MPA create(MPA mpa) {
        return null;
    }

    @Override
    public MPA update(MPA mpa) {
        return null;
    }

    @Override
    public List<MPA> findAll() {
        return List.of();
    }

    @Override
    public MPA findById(Long id) {
        return findOne(FIND_BY_ID_QUERY, id).orElseThrow(() -> new BadRequestException("Rating with id:" + id + " not found"));
    }
}
