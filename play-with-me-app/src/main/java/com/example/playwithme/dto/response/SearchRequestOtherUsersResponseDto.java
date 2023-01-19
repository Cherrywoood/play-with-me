package com.example.playwithme.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchRequestOtherUsersResponseDto {
    private UUID id;
    private String title;
    private UUID authorId;
    private String authorName;
    private GameResponseDto game;
    private String content;
    private Date dateCreated;
}
