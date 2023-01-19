package com.example.playwithme.mapper;

import com.example.playwithme.dto.response.GenreResponseDto;
import com.example.playwithme.model.Genre;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GenreMapper {
    GenreResponseDto genreToGenreDto(Genre genre);
}
