package com.example.playwithme.dto.response;

import com.example.playwithme.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileMainInfoResponseDto {
    private UUID id;
    private UserResponseDto user;
    private String name;
    private Gender gender;
    private Date dateOfBirth;
}
