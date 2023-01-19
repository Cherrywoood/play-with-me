package com.example.playwithme.service;

import com.example.playwithme.model.Genre;
import com.example.playwithme.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreService {
    private final GenreRepository genreRepository;

    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    public List<Genre> findAllByNames(List<String> names) {
        return genreRepository.findAllByNameIn(names);
    }
}
