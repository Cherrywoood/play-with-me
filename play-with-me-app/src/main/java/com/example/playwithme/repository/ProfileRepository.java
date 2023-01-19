package com.example.playwithme.repository;

import com.example.playwithme.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, UUID> {

    Boolean existsByUserId(UUID id);

    Optional<Profile> findByUserId(UUID userId);
}
