package com.example.playwithme.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchRequestRequestDto {
    @NotBlank(message = "cannot be null, empty or whitespace")
    @Size(max = 100, message = "must be less 100 characters")
    private String title;

    @NotNull(message = "cannot be null")
    private UUID authorId;

    @NotNull(message = "cannot be null")
    private UUID gameId;

    @NotBlank(message = "cannot be null, empty or whitespace")
    private String content;
}
