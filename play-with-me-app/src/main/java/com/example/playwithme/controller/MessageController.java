package com.example.playwithme.controller;

import com.example.playwithme.dto.request.MessageRequestDto;
import com.example.playwithme.dto.response.MessageResponseDto;
import com.example.playwithme.mapper.MessageMapper;
import com.example.playwithme.model.Message;
import com.example.playwithme.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;
    private final MessageMapper messageMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponseDto create(@Valid @RequestBody MessageRequestDto dto, Principal principal) {
        return messageMapper.messageToMessageDto(messageService.create(dto, principal));
    }

    @GetMapping
    public ResponseEntity<List<MessageResponseDto>> findAllByChat(@RequestParam UUID chatId, Principal principal) {
        List<Message> messages = messageService.findAllByChat(chatId, principal);
        if (messages.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(messages.stream().map(messageMapper::messageToMessageDto).collect(Collectors.toList()),
                    HttpStatus.OK);
    }

}
