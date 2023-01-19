package com.example.playwithme.dto.request;

import com.example.playwithme.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequestDto {
    @NotNull(message = "shouldn't be null")
    private UUID userId;

    @Size(max = 16, message = "must be less 16 characters")
    @NotBlank(message = "cannot be null, empty or whitespace")
    private String name;

    @NotNull(message = "cannot be null")
    private Gender gender;

    @NotNull(message = "cannot be null")
    private Date dateOfBirth;
}
