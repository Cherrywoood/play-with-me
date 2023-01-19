package com.example.playwithme.controller;

import com.example.playwithme.dto.request.ChatRequestDto;
import com.example.playwithme.dto.response.ChatResponseDto;
import com.example.playwithme.exception.ForbiddenException;
import com.example.playwithme.mapper.ChatMapper;
import com.example.playwithme.model.Chat;
import com.example.playwithme.model.Profile;
import com.example.playwithme.service.ChatService;
import com.example.playwithme.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@Validated
@Slf4j
@RequestMapping("/chats")
public class ChatController {
    private final ChatService chatService;
    private final ProfileService profileService;
    private final ChatMapper chatMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ChatResponseDto create(@Valid @RequestBody ChatRequestDto dto, Principal principal) {
        return chatMapper.chatToChatDto(chatService.create(dto, principal));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ChatResponseDto findById(@PathVariable UUID id, Principal principal) {
        Chat chat = chatService.findById(id);
        Profile profile = profileService.findByUserId(UUID.fromString(principal.getName()));
        if (!chat.getMembers().contains(profile)) {
            throw new ForbiddenException("Forbidden");
        }
        return chatMapper.chatToChatDto(chat);
    }

    @GetMapping
    public ResponseEntity<List<ChatResponseDto>> findAll(@RequestParam(required = false) String name,
                                                         Principal principal) {
        List<Chat> chats = chatService.findAllOrFindAllByName(UUID.fromString(principal.getName()), name);
        if (chats.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(chats.stream().map(chatMapper::chatToChatDto).collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ChatResponseDto updateName(@PathVariable UUID id, @RequestBody String name, Principal principal) {
        return chatMapper.chatToChatDto(chatService.updateName(id, name, principal));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable UUID id, Principal principal) {
        chatService.deleteById(id, principal);
    }

    @DeleteMapping("/{id}/members/{memberId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMemberById(@PathVariable UUID id, @PathVariable UUID memberId, Principal principal) {
        chatService.deleteMemberById(id, memberId, principal);
    }

    @PutMapping("/{id}/members/{memberId}")
    @ResponseStatus(HttpStatus.OK)
    public void addMemberById(@PathVariable UUID id, @PathVariable UUID memberId, Principal principal) {
        chatService.addMemberById(id, memberId, principal);
    }
}
