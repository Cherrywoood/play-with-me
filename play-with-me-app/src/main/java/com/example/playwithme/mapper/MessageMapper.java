package com.example.playwithme.mapper;

import com.example.playwithme.dto.request.MessageRequestDto;
import com.example.playwithme.dto.response.MessageResponseDto;
import com.example.playwithme.model.Message;
import com.example.playwithme.service.ChatService;
import com.example.playwithme.service.ProfileService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class MessageMapper {
    @Autowired
    protected ProfileService profileService;
    @Autowired
    protected ChatService chatService;

    @Mapping(target = "sender", expression = "java(profileService.findById(dto.getSenderId()))")
    @Mapping(target = "chat", expression = "java(chatService.findById(dto.getChatId()))")
    @Mapping(target = "dateCreated", expression = "java(new java.util.Date())")
    public abstract Message messageDtoToMessage(MessageRequestDto dto);

    @Mapping(target = "chatId", source = "chat.id")
    @Mapping(target = "chatName", source = "chat.name")
    @Mapping(target = "senderId", source = "sender.id")
    @Mapping(target = "senderName", source = "sender.name")
    public abstract MessageResponseDto messageToMessageDto(Message message);
}
