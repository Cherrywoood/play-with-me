package com.example.playwithme.service;

import com.example.playwithme.dto.request.SearchRequestRequestDto;
import com.example.playwithme.exception.EntityExistsException;
import com.example.playwithme.exception.EntityNotFoundException;
import com.example.playwithme.exception.ForbiddenException;
import com.example.playwithme.mapper.SearchRequestMapper;
import com.example.playwithme.model.Profile;
import com.example.playwithme.model.SearchRequest;
import com.example.playwithme.repository.SearchRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class SearchRequestService {
    private static final String EXC_MES_ID = "search request not found by id %s";
    private static final String EXC_MES_AUTHOR_FOLLOW = "author can't follow own request";
    private static final String EXC_MES_AUTHOR_UNFOLLOW = "author can't unfollow own request";
    private static final String EXC_MES_USER_FOLLOW = "user is already followed";
    private static final String EXC_MES_USER_UNFOLLOW = "user is already unfollowed";
    private static final String EXC_MES_FORBIDDEN = "user can't change or see not his own request";
    private final SearchRequestRepository searchRequestRepository;
    private final SearchRequestMapper searchRequestMapper;
    private final GameService gameService;
    private final ProfileService profileService;

    public SearchRequest createRequest(SearchRequestRequestDto dto) {
        return searchRequestRepository.save(searchRequestMapper.searchRequestDtoToSearchRequest(dto));
    }

    public SearchRequest findById(UUID id) {
        return searchRequestRepository.findByIdAndStatusIsTrue(id).orElseThrow(() ->
                new EntityNotFoundException(String.format(EXC_MES_ID, id)));
    }

    public SearchRequest findByIdAndAuthor(UUID id, Principal principal) {
        Profile profile = profileService.findByUserId(UUID.fromString(principal.getName()));
        SearchRequest searchRequest = searchRequestRepository.findByIdAndAuthorId(id, profile.getId()).orElseThrow(() ->
                new EntityNotFoundException(String.format(EXC_MES_ID, id)));
        checkAuthorize(principal, searchRequest);
        return searchRequest;
    }

    public Page<SearchRequest> findAll(String gameName, Pageable pageable, Principal principal) {
        Profile profile = profileService.findByUserId(UUID.fromString(principal.getName()));
        if (gameName != null) {
            return searchRequestRepository
                    .findAllByGameNameAndStatusIsTrueAndAuthorIdIsNot(gameName, profile.getId(), pageable);
        } else {
            return searchRequestRepository.findAllByStatusIsTrueAndAuthorIdIsNot(profile.getId(), pageable);
        }
    }

    public Page<SearchRequest> findAllOrderByDateCreatedDesc(Pageable pageable, Principal principal) {
        Profile profile = profileService.findByUserId(UUID.fromString(principal.getName()));
        return searchRequestRepository
                .findAllByStatusIsTrueAndAuthorIdIsNotOrderByDateCreatedDesc(profile.getId(), pageable);
    }

    public Page<SearchRequest> findAllUsersSearchRequest(Principal principal, Pageable pageable) {
        Profile profile = profileService.findByUserId(UUID.fromString(principal.getName()));
        return searchRequestRepository.findAllByAuthorId(profile.getId(), pageable);
    }

    public void deleteById(UUID id, Principal principal) {
        SearchRequest searchRequest = searchRequestRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format(EXC_MES_ID, id)));

        checkAuthorize(principal, searchRequest);

        searchRequestRepository.delete(searchRequest);
    }

    public SearchRequest changeStatus(UUID id, Boolean status, Principal principal) {
        SearchRequest searchRequest = searchRequestRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format(EXC_MES_ID, id)));

        checkAuthorize(principal, searchRequest);

        searchRequest.setStatus(status);
        return searchRequestRepository.save(searchRequest);
    }

    public SearchRequest updateById(UUID id, Map<String, Object> fields, Principal principal) {
        SearchRequest searchRequest = searchRequestRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format(EXC_MES_ID, id)));

        checkAuthorize(principal, searchRequest);

        fields.forEach((key, value) -> {
            switch (key) {
                case "title": {
                    searchRequest.setTitle((String) value);
                    break;
                }
                case "game": {
                    searchRequest.setGame(gameService.findById(UUID.fromString((String) value)));
                    break;
                }
                case "content": {
                    searchRequest.setContent((String) value);
                    break;
                }
                default: {
                    throw new IllegalArgumentException("this field cannot be updated " + key);
                }
            }
        });
        return searchRequestRepository.save(searchRequest);
    }

    @Transactional
    public void follow(UUID id, Principal principal) {
        UUID userId = UUID.fromString(principal.getName());

        SearchRequest searchRequest = searchRequestRepository.findByIdAndStatusIsTrue(id).orElseThrow(() ->
                new EntityNotFoundException(String.format(EXC_MES_ID, id)));
        if (searchRequest.getAuthor().getUser().getId().equals(userId)) {
            throw new IllegalArgumentException(EXC_MES_AUTHOR_FOLLOW);
        }

        Profile follower = profileService.findByUserId(userId);

        if (searchRequest.getFollowers().contains(follower)) {
            throw new EntityExistsException(EXC_MES_USER_FOLLOW);
        }
        searchRequest.getFollowers().add(follower);
        searchRequestRepository.save(searchRequest);
    }

    @Transactional
    public void unfollow(UUID id, Principal principal) {
        UUID userId = UUID.fromString(principal.getName());

        SearchRequest searchRequest = searchRequestRepository.findByIdAndStatusIsTrue(id).orElseThrow(() ->
                new EntityNotFoundException(String.format(EXC_MES_ID, id)));
        if (searchRequest.getAuthor().getUser().getId().equals(userId)) {
            throw new IllegalArgumentException(EXC_MES_AUTHOR_UNFOLLOW);
        }

        Profile follower = profileService.findByUserId(userId);

        if (!searchRequest.getFollowers().contains(follower)) {
            throw new EntityExistsException(EXC_MES_USER_UNFOLLOW);
        }
        searchRequest.getFollowers().remove(follower);
        searchRequestRepository.save(searchRequest);
    }

    public Page<SearchRequest> findAllFollowings(Principal principal, Pageable pageable) {
        Profile profile = profileService.findByUserId(UUID.fromString(principal.getName()));
        return searchRequestRepository.findAllByStatusIsTrueAndFollowersContains(profile, pageable);
    }

    private void checkAuthorize(Principal principal, SearchRequest searchRequest) {
        if (!searchRequest.getAuthor().getUser().getId().equals(UUID.fromString(principal.getName()))) {
            throw new ForbiddenException(EXC_MES_FORBIDDEN);
        }
    }


}
