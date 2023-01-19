package com.example.playwithme.service;

import com.example.playwithme.dto.request.MessageRequestDto;
import com.example.playwithme.exception.ForbiddenException;
import com.example.playwithme.mapper.MessageMapper;
import com.example.playwithme.model.Chat;
import com.example.playwithme.model.Message;
import com.example.playwithme.model.Profile;
import com.example.playwithme.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MessageService {
    private static final String EXC_SENDER = "only chat members can send messages";
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final ChatService chatService;
    private final ProfileService profileService;

    public Message create(MessageRequestDto dto, Principal principal) {
        Message message = messageMapper.messageDtoToMessage(dto);
        if (!message.getSender().getUser().getId().equals(UUID.fromString(principal.getName()))) {
            throw new ForbiddenException("Forbidden");
        }
        if (!message.getChat().getMembers().contains(message.getSender())) {
            throw new IllegalArgumentException(EXC_SENDER);
        }
        return messageRepository.save(message);
    }

    public List<Message> findAllByChat(UUID chatId, Principal principal) {
        Chat chat = chatService.findById(chatId);
        Profile profile = profileService.findByUserId(UUID.fromString(principal.getName()));
        if (!chat.getMembers().contains(profile)) {
            throw new ForbiddenException("Forbidden");
        }
        return messageRepository.findAllByChat(chat);
    }
}
