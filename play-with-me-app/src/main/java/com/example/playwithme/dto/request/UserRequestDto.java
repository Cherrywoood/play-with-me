package com.example.playwithme.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDto {
    @NotBlank(message = "cannot be null, empty or whitespace")
    @Size(min = 5, max = 15, message = "must be between 5 and 15 characters")
    private String username;

    @NotBlank(message = "cannot be null, empty or whitespace")
    @Size(min = 5, message = "must be more than 5 characters")
    private String password;
}
