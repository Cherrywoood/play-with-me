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
@Table(name = "chat")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "name")
    @Size(max = 50, message = "must be more 50 characters")
    @NotBlank(message = "cannot be null, empty or whitespace")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin", referencedColumnName = "id")
    @NotNull(message = "cannot be null")
    @ToString.Exclude
    private Profile admin;

    @Column(name = "date_created")
    @NotNull(message = "cannot be null")
    private Date dateCreated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "search_request_id", referencedColumnName = "id")
    @NotNull(message = "cannot be null")
    @ToString.Exclude
    private SearchRequest searchRequest;

    @ManyToMany
    @JoinTable(name = "member",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "profile_id"))
    @Fetch(FetchMode.SUBSELECT)
    @ToString.Exclude
    private List<Profile> members;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Chat chat = (Chat) o;
        return id != null && Objects.equals(id, chat.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
