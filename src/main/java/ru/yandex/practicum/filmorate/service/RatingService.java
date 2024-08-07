package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.RatingStorage;

import java.util.List;

@Service
public class RatingService {
    private final RatingStorage ratingStorage;

    public RatingService(RatingStorage ratingStorage) {
        this.ratingStorage = ratingStorage;
    }

    public MPA findById(long id) {
        return ratingStorage.findById(id);
    }

    public List<MPA> getAll() {
        return ratingStorage.findAll();
    }
}
