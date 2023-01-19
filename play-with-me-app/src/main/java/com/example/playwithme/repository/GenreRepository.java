package com.example.playwithme.repository;

import com.example.playwithme.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface GenreRepository extends JpaRepository<Genre, UUID> {
    List<Genre> findAllByNameIn(Collection<String> name);
}
