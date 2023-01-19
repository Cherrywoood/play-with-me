package com.example.playwithme.mapper;

import com.example.playwithme.dto.response.PageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PageMapper<T> {
    public PageDTO<T> mapToDto(Page<T> page) {
        return new PageDTO<>(
                page.getContent(),
                page.getNumber(),
                page.getTotalPages(),
                page.getSize(),
                page.getTotalElements(),
                page.hasNext()
        );
    }

}
