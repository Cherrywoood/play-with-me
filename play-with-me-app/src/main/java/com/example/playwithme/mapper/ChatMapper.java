package com.example.playwithme.mapper;

import com.example.playwithme.dto.request.ChatRequestDto;
import com.example.playwithme.dto.response.ChatResponseDto;
import com.example.playwithme.model.Chat;
import com.example.playwithme.model.Profile;
import com.example.playwithme.service.ProfileService;
import com.example.playwithme.service.SearchRequestService;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class ChatMapper {
    @Autowired
    protected ProfileService profileService;
    @Autowired
    protected SearchRequestService searchRequestService;

    public Chat chatDtoToChat(ChatRequestDto dto, UUID adminId) {
        if (dto == null) {
            return null;
        }

        List<Profile> members = new ArrayList<>();
        List<UUID> membersId = dto.getMembersId();

        if (membersId != null) {
            membersId.forEach(profile -> members.add(profileService.findById(profile)));
        }

        return Chat.builder()
                .name(dto.getName())
                .admin(profileService.findById(adminId))
                .searchRequest(searchRequestService.findById(dto.getSearchRequestId()))
                .members(members)
                .dateCreated(new Date())
                .build();
    }

    public ChatResponseDto chatToChatDto(Chat chat) {
        if (chat == null) {
            return null;
        }

        List<UUID> membersId = new ArrayList<>();
        List<String> membersName = new ArrayList<>();
        List<Profile> members = chat.getMembers();
        if (members != null) {
            members.forEach(profile -> {
                membersId.add(profile.getId());
                membersName.add(profile.getName());
            });
        }

        return ChatResponseDto.builder()
                .id(chat.getId())
                .name(chat.getName())
                .adminId(chat.getAdmin().getId())
                .adminName(chat.getAdmin().getName())
                .dateCreated(chat.getDateCreated())
                .membersId(membersId)
                .membersName(membersName)
                .build();
    }
}
