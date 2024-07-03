package ru.yandex.practicum.filmorate.storage.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.RatingStorage;

import java.util.List;
import java.util.Optional;

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
        Optional<MPA> mpa = findOne(FIND_BY_ID_QUERY, id);
        return mpa.orElseThrow(() -> new NotFoundException("Rating with id:" + id + " not found"));
    }
}
