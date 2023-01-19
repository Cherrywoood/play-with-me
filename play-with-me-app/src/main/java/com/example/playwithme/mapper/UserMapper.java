package com.example.playwithme.mapper;

import com.example.playwithme.dto.request.UserRequestDto;
import com.example.playwithme.dto.response.UserResponseDto;
import com.example.playwithme.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User userDtoToUser(UserRequestDto dto);

    UserResponseDto userToUserDto(User user);
}
