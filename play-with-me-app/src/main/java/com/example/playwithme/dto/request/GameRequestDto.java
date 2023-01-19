package com.example.playwithme.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameRequestDto {
    @NotBlank(message = "cannot be null, empty or whitespace")
    private String name;

    @NotBlank(message = "cannot be null, empty or whitespace")
    private List<String> genresNames;
}
