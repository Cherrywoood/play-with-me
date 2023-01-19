package com.example.playwithme.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "game")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "name")
    @NotBlank(message = "cannot be null, empty or whitespace")
    private String name;

    @ManyToMany
    @JoinTable(name = "genre_game",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    @Fetch(FetchMode.SUBSELECT)
    @NotNull(message = "cannot be null")
    @ToString.Exclude
    private List<Genre> genres;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Game game = (Game) o;
        return id != null && Objects.equals(id, game.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
