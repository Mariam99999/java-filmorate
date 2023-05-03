package ru.yandex.practicum.filmorate.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.RealiseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
public class Film {
    private int id;
    @NotBlank
    private final String name;
    @NotBlank
    @Size(max = 200)
    private final String description;
    @NotNull
    @RealiseDate
    private final LocalDate releaseDate;
    @NotNull
    @Positive
    private final Double duration;
    private Set<Integer> likes;

    public Set<Integer> addLike (Integer userId){
        likes.add(userId);
        return likes;
    }
    public Set<Integer> deleteLike(Integer userId){
        likes.remove(userId);
        return likes;
    }
}
