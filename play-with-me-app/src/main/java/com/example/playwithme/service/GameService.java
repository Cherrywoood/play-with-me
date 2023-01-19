package com.example.playwithme.service;

import com.example.playwithme.exception.EntityNotFoundException;
import com.example.playwithme.model.Game;
import com.example.playwithme.model.Genre;
import com.example.playwithme.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class GameService {
    private final GameRepository gameRepository;
    private final GenreService genreService;
    private static final String EXC_MES_ID = "game not found by id %s";

    public Game findById(UUID id) {
        return gameRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format(EXC_MES_ID, id)));
    }

    public Page<Game> findAllOrFindAllByName(String name, Pageable pageable) {
        if (name != null) return findAllByName(name, pageable);
        else return findAll(pageable);
    }

    public Page<Game> findAllByName(String name, Pageable pageable) {
        return gameRepository.findAllByNameContains(name, pageable);
    }

    public Page<Game> findAll(Pageable pageable) {
        return gameRepository.findAll(pageable);
    }

    public Page<Game> findAllByGenre(List<String> genreName, Pageable pageable) {
        List<Genre> genres = genreService.findAllByNames(genreName);
        List<Game> games = gameRepository.findAll().stream()
                .filter(game -> new HashSet<>(game.getGenres()).containsAll(genres)).collect(Collectors.toList());

        int start = Math.min((int) pageable.getOffset(), games.size());
        int end = Math.min((start + pageable.getPageSize()), games.size());
        return new PageImpl<>(games.subList(start, end), pageable, games.size());
    }
}
