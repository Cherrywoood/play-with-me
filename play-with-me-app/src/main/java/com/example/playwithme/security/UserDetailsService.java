package com.example.playwithme.security;

import com.example.playwithme.exception.EntityNotFoundException;
import com.example.playwithme.model.User;
import com.example.playwithme.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.UUID;


@RequiredArgsConstructor
@Slf4j
@Service
public class UserDetailsService {

    private static final String EXC_MES_ID = "user not found by id %s";
    private final UserRepository userRepository;

    public UserDetails loadUserById(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format(EXC_MES_ID, id)));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities("USER")
                .build();
    }
}
