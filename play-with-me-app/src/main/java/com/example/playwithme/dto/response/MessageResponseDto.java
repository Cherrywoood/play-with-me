package com.example.playwithme.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponseDto {
    private UUID id;
    private UUID chatId;
    private String chatName;
    private UUID senderId;
    private String senderName;
    private String content;
    private Date dateCreated;
}
