package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private int id;
    @NotBlank
    @Email
    private String email;
    @UserLoginValidation
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
}
