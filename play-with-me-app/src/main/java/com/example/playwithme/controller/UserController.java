package com.example.playwithme.controller;

import com.example.playwithme.dto.response.UserResponseDto;
import com.example.playwithme.mapper.UserMapper;
import com.example.playwithme.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("#id == principal")
    public UserResponseDto updateById(@PathVariable UUID id,
                                      @RequestBody Map<String, Object> fields) {
        return userMapper.userToUserDto(userService.updateById(id, fields));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("#id == principal")
    public void deleteById(@PathVariable UUID id) {
        userService.deleteById(id);
    }
}
