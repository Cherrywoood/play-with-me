package com.example.playwithme.controller;

import com.example.playwithme.dto.request.SearchRequestRequestDto;
import com.example.playwithme.dto.response.PageDTO;
import com.example.playwithme.dto.response.SearchRequestAuthorResponseDto;
import com.example.playwithme.dto.response.SearchRequestOtherUsersResponseDto;
import com.example.playwithme.exception.ForbiddenException;
import com.example.playwithme.mapper.PageMapper;
import com.example.playwithme.mapper.SearchRequestMapper;
import com.example.playwithme.model.Profile;
import com.example.playwithme.model.SearchRequest;
import com.example.playwithme.service.ProfileService;
import com.example.playwithme.service.SearchRequestService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Validated
@RestController
@Slf4j
@RequestMapping("/search-requests")
public class SearchRequestController {
    private final SearchRequestService searchRequestService;
    private final ProfileService profileService;
    private final SearchRequestMapper searchRequestMapper;
    private final PageMapper<SearchRequestOtherUsersResponseDto> pageForOtherUsers;
    private final PageMapper<SearchRequestAuthorResponseDto> pageForAuthor;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SearchRequestAuthorResponseDto createRequest(@Valid @RequestBody SearchRequestRequestDto dto,
                                                        Principal principal) {
        Profile profile = profileService.findByUserId(UUID.fromString(principal.getName()));
        if (!profile.getId().equals(dto.getAuthorId())) {
            throw new ForbiddenException("Forbidden");
        }
        return searchRequestMapper
                .searchRequestToSearchRequestAuthorDto(searchRequestService.createRequest(dto));

    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SearchRequestOtherUsersResponseDto findById(@PathVariable UUID id, Principal principal) {
        SearchRequest searchRequest = searchRequestService.findById(id);
        if (searchRequest.getAuthor().getUser().getId().equals(UUID.fromString(principal.getName()))) {
            throw new ForbiddenException("Forbidden");
        }
        return searchRequestMapper.searchRequestToSearchRequestOtherUsersDto(searchRequest);
    }

    @GetMapping
    public ResponseEntity<PageDTO<SearchRequestOtherUsersResponseDto>> findAll(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "must not be less than 0")
            int page,
            @RequestParam(defaultValue = "5")
            @Max(value = 10, message = "must not be more than 10")
            @Min(value = 1, message = "must not be less than 1")
            int size,
            @RequestParam(required = false) String gameName,
            Principal principal) {

        Page<SearchRequest> searchRequestPage = searchRequestService
                .findAll(gameName, PageRequest.of(page, size), principal);
        if (searchRequestPage.getContent().isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(pageForOtherUsers.mapToDto(
                searchRequestPage.map(searchRequestMapper::searchRequestToSearchRequestOtherUsersDto)), HttpStatus.OK);
    }

    @GetMapping("/new")
    public ResponseEntity<PageDTO<SearchRequestOtherUsersResponseDto>> findAllOrderByDateCreatedDesc(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "must not be less than 0") int page,
            @RequestParam(defaultValue = "5")
            @Max(value = 10, message = "must not be more than 10")
            @Min(value = 1, message = "must not be less than 1") int size,
            Principal principal) {

        Page<SearchRequest> searchRequestPage = searchRequestService
                .findAllOrderByDateCreatedDesc(PageRequest.of(page, size), principal);
        if (searchRequestPage.getContent().isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(pageForOtherUsers.mapToDto(
                searchRequestPage.map(searchRequestMapper::searchRequestToSearchRequestOtherUsersDto)), HttpStatus.OK);
    }


    @GetMapping("/author")
    public ResponseEntity<PageDTO<SearchRequestAuthorResponseDto>> getAllUserSearchRequest(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "must not be less than 0") int page,
            @RequestParam(defaultValue = "5")
            @Max(value = 10, message = "must not be more than 10")
            @Min(value = 1, message = "must not be less than 1") int size,
            Principal principal) {

        Page<SearchRequest> searchRequestPage = searchRequestService
                .findAllUsersSearchRequest(principal, PageRequest.of(page, size));
        if (searchRequestPage.getContent().isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(pageForAuthor.mapToDto(searchRequestPage.map(
                searchRequestMapper::searchRequestToSearchRequestAuthorDto)),
                HttpStatus.OK);
    }

    @GetMapping("/author/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public SearchRequestAuthorResponseDto getUserSearchRequestById(@PathVariable UUID requestId,
                                                                   Principal principal) {
        return searchRequestMapper.searchRequestToSearchRequestAuthorDto(
                searchRequestService.findByIdAndAuthor(requestId, principal));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable UUID id, Principal principal) {
        searchRequestService.deleteById(id, principal);
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    public SearchRequestAuthorResponseDto changeStatusSearchRequest(@PathVariable UUID id,
                                                                    @RequestBody Boolean status,
                                                                    Principal principal) {
        return searchRequestMapper.searchRequestToSearchRequestAuthorDto(
                searchRequestService.changeStatus(id, status, principal)
        );
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SearchRequestAuthorResponseDto updateSearchRequest(@PathVariable UUID id,
                                                              @RequestBody Map<String, Object> fields,
                                                              Principal principal) {
        return searchRequestMapper.searchRequestToSearchRequestAuthorDto(
                searchRequestService.updateById(id, fields, principal)
        );
    }

    @PutMapping("/{id}/following")
    @ResponseStatus(HttpStatus.OK)
    public void follow(@PathVariable UUID id, Principal principal) {
        searchRequestService.follow(id, principal);
    }

    @DeleteMapping("/{id}/unfollowing")
    @ResponseStatus(HttpStatus.OK)
    public void unfollow(@PathVariable UUID id, Principal principal) {
        searchRequestService.unfollow(id, principal);
    }

    @GetMapping("/followings")
    public ResponseEntity<PageDTO<SearchRequestOtherUsersResponseDto>> findAllFollowings(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "must not be less than 0")
            int page,
            @RequestParam(defaultValue = "5")
            @Max(value = 10, message = "must not be more than 10")
            @Min(value = 1, message = "must not be less than 1")
            int size,
            Principal principal) {

        Page<SearchRequest> searchRequestPage = searchRequestService
                .findAllFollowings(principal, PageRequest.of(page, size));
        if (searchRequestPage.getContent().isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(pageForOtherUsers.mapToDto(
                searchRequestPage.map(searchRequestMapper::searchRequestToSearchRequestOtherUsersDto)), HttpStatus.OK);
    }


}


