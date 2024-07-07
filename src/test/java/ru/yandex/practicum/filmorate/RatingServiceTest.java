package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.RatingService;
import ru.yandex.practicum.filmorate.storage.dal.RatingDbStorage;
import ru.yandex.practicum.filmorate.storage.dal.mappers.RatingMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {RatingService.class, RatingDbStorage.class, RatingMapper.class})
class RatingServiceTest {
    @Autowired
    private final RatingService ratingService;

    @Test
    void getRating() {
        MPA mpa = ratingService.findById(1L);
        Assertions.assertThat(mpa.getName()).isEqualTo("G");
    }

    @Test
    void getAllGenre() {
        List<MPA> ratings = ratingService.getAll();
        Assertions.assertThat(ratings).isNotEmpty().hasSize(5);
    }

    @Test
    void getGenreNotExist() {
        assertThrows(NotFoundException.class, () -> ratingService.findById(11111111111L));
    }
}
