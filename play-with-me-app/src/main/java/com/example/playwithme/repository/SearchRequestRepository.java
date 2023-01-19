package com.example.playwithme.repository;

import com.example.playwithme.model.Profile;
import com.example.playwithme.model.SearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SearchRequestRepository extends JpaRepository<SearchRequest, UUID> {

    Page<SearchRequest> findAllByGameNameAndStatusIsTrueAndAuthorIdIsNot(String gameName, UUID authorId, Pageable pageable);

    Page<SearchRequest> findAllByStatusIsTrueAndAuthorIdIsNot(UUID authorId, Pageable pageable);

    Page<SearchRequest> findAllByStatusIsTrueAndAuthorIdIsNotOrderByDateCreatedDesc(UUID authorId, Pageable pageable);

    Page<SearchRequest> findAllByAuthorId(UUID id, Pageable pageable);

    Optional<SearchRequest> findByIdAndAuthorId(UUID id, UUID authorId);

    Optional<SearchRequest> findByIdAndStatusIsTrue(UUID id);

    Page<SearchRequest> findAllByStatusIsTrueAndFollowersContains(Profile profile, Pageable pageable);
}
