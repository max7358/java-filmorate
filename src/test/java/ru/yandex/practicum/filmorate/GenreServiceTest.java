package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.storage.dal.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.dal.mappers.GenreMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {GenreService.class, GenreDbStorage.class, GenreMapper.class})
class GenreServiceTest {
    @Autowired
    private final GenreService genreService;

    @Test
    void getGenre() {
        Genre genre = genreService.getGenreById(1L);
        Assertions.assertThat(genre.getName()).isEqualTo("Комедия");
    }

    @Test
    void getAllGenre() {
        List<Genre> genres = genreService.getAll();
        Assertions.assertThat(genres).isNotEmpty().hasSize(6);
    }

    @Test
    void getGenreNotExist() {
        assertThrows(NotFoundException.class, () -> genreService.getGenreById(111111111L));
    }
}
