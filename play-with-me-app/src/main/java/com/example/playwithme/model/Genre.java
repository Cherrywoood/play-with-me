package com.example.playwithme.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "genre")
@Entity
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "name")
    @NotBlank(message = "cannot be null, empty or whitespace")
    private String name;

    @ManyToMany(mappedBy = "genres")
    @Fetch(FetchMode.SUBSELECT)
    @ToString.Exclude
    private List<Game> games;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Genre genre = (Genre) o;
        return id != null && Objects.equals(id, genre.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
