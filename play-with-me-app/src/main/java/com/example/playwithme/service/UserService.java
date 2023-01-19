package com.example.playwithme.service;

import com.example.playwithme.exception.EntityNotFoundException;
import com.example.playwithme.model.User;
import com.example.playwithme.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {
    private static final String EXC_MES_ID = "user not found by id %s";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format(EXC_MES_ID, id)));
    }

    public User updateById(UUID id, Map<String, Object> fields) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format(EXC_MES_ID, id)));
        fields.forEach((key, value) -> {
            switch (key) {
                case "password": {
                    user.setPassword(passwordEncoder.encode((String) value));
                    break;
                }
                case "username": {
                    user.setUsername((String) value);
                    break;
                }
                default: {
                    throw new IllegalArgumentException("this field cannot be updated " + key);
                }
            }
        });
        return userRepository.save(user);
    }

    public void deleteById(UUID id) {
        userRepository.delete(
                userRepository.findById(id).orElseThrow(() ->
                        new EntityNotFoundException(String.format(EXC_MES_ID, id)))
        );
    }
}
