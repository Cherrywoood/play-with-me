package com.example.playwithme.mapper;

import com.example.playwithme.dto.request.GameRequestDto;
import com.example.playwithme.dto.response.GameResponseDto;
import com.example.playwithme.model.Game;
import com.example.playwithme.service.GenreService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper(componentModel = "spring")
public abstract class GameMapper {
    @Autowired
    protected GenreService genreService;

    @Mapping(target = "genres", expression = "java(genreService.findAllByNames(dto.getGenresNames()))")
    public abstract Game gameDtoToGame(GameRequestDto dto);

    public abstract GameResponseDto gameToGameDto(Game game);
}
