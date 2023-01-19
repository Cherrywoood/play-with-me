package com.example.playwithme.controller;

import com.example.playwithme.dto.response.GenreResponseDto;
import com.example.playwithme.mapper.GenreMapper;
import com.example.playwithme.model.Genre;
import com.example.playwithme.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;
    private final GenreMapper genreMapper;

    @GetMapping
    public ResponseEntity<List<GenreResponseDto>> findAll() {
        List<Genre> genres = genreService.findAll();
        if (genres.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(genres.stream().map(genreMapper::genreToGenreDto).collect(Collectors.toList()),
                HttpStatus.OK);
    }
}
