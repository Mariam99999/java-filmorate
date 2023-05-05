package ru.yandex.practicum.filmorate.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Data
public class User {
    @NotBlank
    @Email
    private final String email;
    @NotBlank
    private final String login;
    @NotNull
    @PastOrPresent
    private final LocalDate birthday;
    @JsonIgnore
    private final Set<Integer> friends = new HashSet<>();
    private int id;
    private String name;

    public Set<Integer> addFriend(Integer friendId) {

        friends.add(friendId);
        return friends;
    }

    public Set<Integer> deleteFriend(Integer friendId) {
        friends.remove(friendId);
        return friends;
    }

}
