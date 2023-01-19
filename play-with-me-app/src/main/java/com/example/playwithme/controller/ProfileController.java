package com.example.playwithme.controller;

import com.example.playwithme.dto.request.ProfileRequestDto;
import com.example.playwithme.dto.response.ProfileMainInfoResponseDto;
import com.example.playwithme.mapper.ProfileMapper;
import com.example.playwithme.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/profiles")
public class ProfileController {
    private final ProfileService profileService;
    private final ProfileMapper profileMapper;


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProfileMainInfoResponseDto getProfileMainInfo(@PathVariable UUID id) {
        return profileMapper.profileToProfileMainInfoDto(profileService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("#dto.userId == principal")
    public ProfileMainInfoResponseDto save(@Valid @RequestBody ProfileRequestDto dto) {
        return profileMapper.profileToProfileMainInfoDto(profileService.create(dto));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProfileMainInfoResponseDto updateProfileInfo(@PathVariable UUID id,
                                                        @RequestBody Map<String, Object> fields,
                                                        Principal principal) {
        return profileMapper.profileToProfileMainInfoDto(profileService.updateById(id, fields, principal));
    }

}
