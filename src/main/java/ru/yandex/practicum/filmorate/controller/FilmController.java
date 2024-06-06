package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping()
    public List<Film> getFilms() {
        log.info("GET all films");
        return filmService.getAll();
    }

    @PostMapping()
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("POST film: {}", film);
        return filmService.create(film);
    }

    @PutMapping()
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("PUT film: {}", film);
        return filmService.update(film);
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable Long id) {
        log.info("GET Film with id: {}", id);
        return filmService.getFilmById(id);
    }

    @PutMapping("{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("PUT like film id: {}, user id: {}", id, userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("DELETE like film id: {}, user id: {}", id, userId);
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(required = false) Integer count) {
        log.info("GET popular films with count: {}", count);
        return filmService.getPopular(count);
    }
}
