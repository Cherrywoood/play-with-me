package com.example.playwithme.service;

import com.example.playwithme.dto.request.ProfileRequestDto;
import com.example.playwithme.enums.Gender;
import com.example.playwithme.exception.EntityExistsException;
import com.example.playwithme.exception.EntityNotFoundException;
import com.example.playwithme.exception.ForbiddenException;
import com.example.playwithme.mapper.ProfileMapper;
import com.example.playwithme.model.Profile;
import com.example.playwithme.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProfileService {
    private static final String EXC_MES_ID = "profile not found by id %s";
    private static final String EXC_EXIST = "profile with this user exists";
    private static final String EXC_USER = "profile with user not found";
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    public Profile findById(UUID id) {
        return profileRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format(EXC_MES_ID, id)));
    }

    public Profile findByUserId(UUID userId) {
        return profileRepository.findByUserId(userId).orElseThrow(() ->
                new EntityNotFoundException(EXC_USER));
    }

    public Profile create(ProfileRequestDto dto) {
        if (Boolean.FALSE.equals(profileRepository.existsByUserId(dto.getUserId()))) {
            return profileRepository.save(profileMapper.profileDtoToProfile(dto));
        } else throw new EntityExistsException(EXC_EXIST);
    }

    public Profile updateById(UUID id, Map<String, Object> fields, Principal principal) {
        Profile profile = profileRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format(EXC_MES_ID, id)));

        if (!profile.getUser().getId().equals(UUID.fromString(principal.getName()))) {
            throw new ForbiddenException("Forbidden");
        }
        fields.forEach((key, value) -> {
            switch (key) {
                case "name": {
                    profile.setName((String) value);
                    break;
                }
                case "gender": {
                    profile.setGender(Gender.valueOf((String) value));
                    break;
                }
                default: {
                    throw new IllegalArgumentException("this field cannot be updated " + key);
                }
            }
        });
        return profileRepository.save(profile);
    }
}
