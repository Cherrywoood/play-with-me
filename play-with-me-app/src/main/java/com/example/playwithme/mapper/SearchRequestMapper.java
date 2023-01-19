package com.example.playwithme.mapper;

import com.example.playwithme.dto.request.SearchRequestRequestDto;
import com.example.playwithme.dto.response.SearchRequestAuthorResponseDto;
import com.example.playwithme.dto.response.SearchRequestOtherUsersResponseDto;
import com.example.playwithme.model.Profile;
import com.example.playwithme.model.SearchRequest;
import com.example.playwithme.service.GameService;
import com.example.playwithme.service.ProfileService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class SearchRequestMapper {
    @Autowired
    protected GameMapper gameMapper;
    @Autowired
    protected ProfileService profileService;
    @Autowired
    protected GameService gameService;

    @Mapping(target = "author", expression = "java(profileService.findById(dto.getAuthorId()))")
    @Mapping(target = "game", expression = "java(gameService.findById(dto.getGameId()))")
    @Mapping(target = "dateCreated", expression = "java(new java.util.Date())")
    @Mapping(target = "status", constant = "true")
    public abstract SearchRequest searchRequestDtoToSearchRequest(SearchRequestRequestDto dto);


    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "authorName", source = "author.name")
    public abstract SearchRequestOtherUsersResponseDto
    searchRequestToSearchRequestOtherUsersDto(SearchRequest searchRequest);

    public SearchRequestAuthorResponseDto searchRequestToSearchRequestAuthorDto(SearchRequest searchRequest) {
        if (searchRequest == null) {
            return null;
        }

        List<UUID> followersId = new ArrayList<>();
        List<String> followersName = new ArrayList<>();
        List<Profile> followers = searchRequest.getFollowers();
        if (followers != null) {
            followers.forEach(profile -> {
                followersId.add(profile.getId());
                followersName.add(profile.getName());
            });
        }
        return SearchRequestAuthorResponseDto.builder()
                .id(searchRequest.getId())
                .title(searchRequest.getTitle())
                .game(gameMapper.gameToGameDto(searchRequest.getGame()))
                .content(searchRequest.getContent())
                .status(searchRequest.getStatus())
                .dateCreated(searchRequest.getDateCreated())
                .followersId(followersId)
                .followersName(followersName)
                .build();
    }

}
