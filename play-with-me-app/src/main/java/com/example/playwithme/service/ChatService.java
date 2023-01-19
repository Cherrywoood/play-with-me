package com.example.playwithme.service;

import com.example.playwithme.dto.request.ChatRequestDto;
import com.example.playwithme.exception.EntityNotFoundException;
import com.example.playwithme.exception.ForbiddenException;
import com.example.playwithme.mapper.ChatMapper;
import com.example.playwithme.model.Chat;
import com.example.playwithme.model.Profile;
import com.example.playwithme.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatService {
    private static final String EXC_MES_ID = "chat not found by id %s";
    private static final String EXC_MES_MEMBER = "user not follow on request and cant be added to the chat";
    private static final String EXC_MES_MEMBER_EXISTS = "user is already a member of this chat";
    private static final String EXC_MES_MEMBER_NOT_EXISTS = "user is not a member of this chat";
    private static final String EXC_MES_DELETE_ADMIN = "admin can't delete yourself";
    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;
    private final ProfileService profileService;

    @Transactional
    public Chat create(ChatRequestDto dto, Principal principal) {
        Profile profile = profileService.findByUserId(UUID.fromString(principal.getName()));
        Chat chat = chatMapper.chatDtoToChat(dto, profile.getId());
        if (!chat.getSearchRequest().getAuthor().equals(profile)) {
            throw new ForbiddenException("Forbidden");
        }
        List<Profile> followers = chat.getSearchRequest().getFollowers();
        chat.getMembers().forEach(member -> {
            if (!followers.contains(member)) {
                throw new IllegalArgumentException(EXC_MES_MEMBER);
            }
        });
        chat.getMembers().add(chat.getAdmin());
        return chatRepository.save(chat);
    }

    public Chat findById(UUID id) {
        return chatRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format(EXC_MES_ID, id)));
    }

    public List<Chat> findAllOrFindAllByName(UUID memberId, String name) {
        if (name != null) return findAllByName(memberId, name);
        else return findAll(memberId);
    }

    public List<Chat> findAll(UUID memberId) {
        return profileService.findByUserId(memberId).getChats();
    }

    public List<Chat> findAllByName(UUID memberId, String name) {
        return profileService.findByUserId(memberId).getChats().stream()
                .filter(chat -> chat.getName().contains(name)).collect(Collectors.toList());
    }

    public Chat updateName(UUID id, String name, Principal principal) {
        Chat chat = chatRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format(EXC_MES_ID, id)));
        if (!chat.getAdmin().getUser().getId().equals(UUID.fromString(principal.getName()))) {
            throw new ForbiddenException("Forbidden");
        }
        chat.setName(name);
        return chatRepository.save(chat);
    }

    public void deleteById(UUID id, Principal principal) {
        Chat chat = chatRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format(EXC_MES_ID, id)));
        if (!chat.getAdmin().getUser().getId().equals(UUID.fromString(principal.getName()))) {
            throw new ForbiddenException("Forbidden");
        }
        chatRepository.delete(chat);
    }

    @Transactional
    public void deleteMemberById(UUID id, UUID memberId, Principal principal) {
        Chat chat = chatRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format(EXC_MES_ID, id)));
        Profile member = profileService.findById(memberId);
        if (!member.getUser().getId().equals(UUID.fromString(principal.getName())) &&
                !chat.getAdmin().getUser().getId().equals(UUID.fromString(principal.getName()))) {
            throw new ForbiddenException("Forbidden");
        }
        if (chat.getAdmin().equals(member)) {
            throw new IllegalArgumentException(EXC_MES_DELETE_ADMIN);
        }

        List<Profile> members = chat.getMembers();
        if (!members.contains(member)) {
            throw new IllegalArgumentException(EXC_MES_MEMBER_NOT_EXISTS);
        }
        members.remove(member);
        chatRepository.save(chat);
    }

    @Transactional
    public void addMemberById(UUID id, UUID memberId, Principal principal) {
        Chat chat = chatRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format(EXC_MES_ID, id)));
        if (!chat.getAdmin().getUser().getId().equals(UUID.fromString(principal.getName()))) {
            throw new ForbiddenException("Forbidden");
        }

        Profile profile = profileService.findById(memberId);
        List<Profile> followers = chat.getSearchRequest().getFollowers();

        if (!followers.contains(profile)) {
            throw new IllegalArgumentException(EXC_MES_MEMBER);
        }

        List<Profile> members = chat.getMembers();
        if (members.contains(profile)) {
            throw new IllegalArgumentException(EXC_MES_MEMBER_EXISTS);
        }

        members.add(profile);
        chatRepository.save(chat);
    }
}
