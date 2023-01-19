package com.example.playwithme.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Date;
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
@Table(name = "search_request")
public class SearchRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "title")
    @NotBlank(message = "cannot be null, empty or whitespace")
    @Size(max = 100, message = "must be less 100 characters")
    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author", referencedColumnName = "id")
    @NotNull(message = "cannot be null")
    private Profile author;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id", referencedColumnName = "id")
    @NotNull(message = "cannot be null")
    private Game game;

    @Column(name = "content")
    @NotBlank(message = "cannot be null, empty or whitespace")
    private String content;

    @Column(name = "status")
    @NotNull(message = "cannot be null")
    private Boolean status;

    @Column(name = "date_created")
    @NotNull(message = "cannot be null")
    private Date dateCreated;

    @ManyToMany
    @JoinTable(
            name = "follower",
            joinColumns = @JoinColumn(name = "search_request_id"),
            inverseJoinColumns = @JoinColumn(name = "profile_id")
    )
    @Fetch(FetchMode.SUBSELECT)
    @ToString.Exclude
    private List<Profile> followers;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SearchRequest that = (SearchRequest) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
