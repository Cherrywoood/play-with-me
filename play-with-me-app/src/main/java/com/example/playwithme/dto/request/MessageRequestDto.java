package com.example.playwithme.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageRequestDto {
    @NotNull(message = "cannot be null")
    private UUID chatId;

    @NotNull(message = "cannot be null")
    private UUID senderId;

    @NotBlank(message = "cannot be null, empty or whitespace")
    private String content;
}
