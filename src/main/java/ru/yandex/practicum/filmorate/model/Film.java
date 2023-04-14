package ru.yandex.practicum.filmorate.model;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Film {
    private int id;
    @NotNull
    @NotBlank
    private final String name;
    @NotNull
    @NotBlank
    private final String description;
    @NotNull
    private final LocalDate releaseDate;
    @NotNull
    private final Duration duration;

}
