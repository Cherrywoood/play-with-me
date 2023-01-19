package com.example.playwithme.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchRequestAuthorResponseDto {
    private UUID id;
    private String title;
    private GameResponseDto game;
    private String content;
    private Boolean status;
    private Date dateCreated;
    private List<UUID> followersId;
    private List<String> followersName;
}
