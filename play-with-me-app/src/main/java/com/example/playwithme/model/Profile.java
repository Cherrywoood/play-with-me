package com.example.playwithme.model;

import com.example.playwithme.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.sql.Date;
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
@Slf4j
@Table(name = "profile")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull(message = "shouldn't be null")
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "name")
    @Size(max = 16, message = "must be less 16 characters")
    @NotBlank(message = "cannot be null, empty or whitespace")
    private String name;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    @NotNull(message = "cannot be null")
    private Gender gender;

    @Column(name = "date_of_birth")
    @NotNull(message = "cannot be null")
    private Date dateOfBirth;

    @ManyToMany(mappedBy = "followers")
    @Fetch(FetchMode.SUBSELECT)
    @ToString.Exclude
    private List<SearchRequest> requests;

    @ManyToMany(mappedBy = "members")
    @Fetch(FetchMode.SUBSELECT)
    @ToString.Exclude
    private List<Chat> chats;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Profile profile = (Profile) o;
        return id != null && Objects.equals(id, profile.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
