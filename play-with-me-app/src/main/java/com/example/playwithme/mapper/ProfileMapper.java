package com.example.playwithme.mapper;

import com.example.playwithme.dto.request.ProfileRequestDto;
import com.example.playwithme.dto.response.ProfileMainInfoResponseDto;
import com.example.playwithme.model.Profile;
import com.example.playwithme.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ProfileMapper {
    @Autowired
    protected UserService userService;

    @Mapping(target = "user", expression = "java(userService.findById(dto.getUserId()))")
    public abstract Profile profileDtoToProfile(ProfileRequestDto dto);

    public abstract ProfileMainInfoResponseDto profileToProfileMainInfoDto(Profile profile);
}
