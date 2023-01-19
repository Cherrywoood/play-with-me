package com.example.playwithme.controller;

import com.example.playwithme.dto.response.GameResponseDto;
import com.example.playwithme.dto.response.PageDTO;
import com.example.playwithme.mapper.GameMapper;
import com.example.playwithme.mapper.PageMapper;
import com.example.playwithme.model.Game;
import com.example.playwithme.service.GameService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/games")
public class GameController {
    private final GameService gameService;
    private final GameMapper gameMapper;
    private final PageMapper<GameResponseDto> pageMapper;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GameResponseDto findById(@PathVariable UUID id) {
        return gameMapper.gameToGameDto(gameService.findById(id));
    }

    @GetMapping
    public ResponseEntity<PageDTO<GameResponseDto>> findAll(@RequestParam(defaultValue = "0")
                                                            @Min(value = 0, message = "must not be less than 0") int page,
                                                            @RequestParam(defaultValue = "5")
                                                            @Max(value = 10, message = "must not be more than 10")
                                                            @Min(value = 1, message = "must not be less than 1") int size,
                                                            @RequestParam(required = false) String name) {
        Page<Game> gamePage = gameService.findAllOrFindAllByName(name, PageRequest.of(page, size));
        if (gamePage.getContent().isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(pageMapper.mapToDto(
                gamePage.map(gameMapper::gameToGameDto)), HttpStatus.OK);
    }

    @GetMapping("/genre")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PageDTO<GameResponseDto>> findAllByGenre(@RequestParam(defaultValue = "0")
                                                                   @Min(value = 0, message = "must not be less than 0") int page,
                                                                   @RequestParam(defaultValue = "5")
                                                                   @Max(value = 10, message = "must not be more than 10")
                                                                   @Min(value = 1, message = "must not be less than 1") int size,
                                                                   @RequestParam List<String> genreNames) {
        Page<Game> gamePage = gameService.findAllByGenre(genreNames, PageRequest.of(page, size));
        if (gamePage.getContent().isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(pageMapper.mapToDto(
                gamePage.map(gameMapper::gameToGameDto)), HttpStatus.OK);
    }


}
