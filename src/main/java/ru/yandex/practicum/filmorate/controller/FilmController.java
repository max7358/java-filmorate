package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    public FilmController() {
        this.filmService = new FilmService(new InMemoryFilmStorage());
    }

    @GetMapping()
    public List<Film> getFilms() {
        return filmService.getAll();
    }

    @PostMapping()
    public Film createFilm(@RequestBody
                               @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING) Film film) {
        return filmService.create(film);
    }

    @PutMapping()
    public Film updateFilm(@RequestBody Film film) {
        return filmService.update(film);
    }
}
