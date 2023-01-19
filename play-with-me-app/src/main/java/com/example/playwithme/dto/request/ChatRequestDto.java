package com.example.playwithme.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRequestDto {
    @Size(max = 50, message = "must be less 16 characters")
    @NotBlank(message = "cannot be null, empty or whitespace")
    private String name;

    @NotNull(message = "cannot be null")
    @NotEmpty(message = "cannot be empty")
    private List<UUID> membersId;

    @NotNull(message = "cannot be null")
    private UUID searchRequestId;
}
