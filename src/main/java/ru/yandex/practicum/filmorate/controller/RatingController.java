package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
public class RatingController {
    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @GetMapping
    List<MPA> getRatings() {
        log.info("getRatings");
        return ratingService.getAll();
    }

    @GetMapping("/{id}")
    MPA getRating(@PathVariable long id) {
        log.info("getRating {}", id);
        return ratingService.findById(id);
    }
}
