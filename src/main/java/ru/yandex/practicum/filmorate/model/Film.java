package ru.yandex.practicum.filmorate.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.annotation.RealiseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
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
    @JsonIgnore
    private final Set<Integer> likes = new HashSet<>();
    @NonNull
    private Set<Genre> genres;
    @NonNull
    private AgeRating ageRating;

    public Set<Integer> addLike(Integer userId) {
        likes.add(userId);
        return likes;
    }

    public Set<Integer> deleteLike(Integer userId) {
        if (likes.remove(userId)) {
            return likes;
        }
        throw new NullPointerException("Wrong id");
    }
}
