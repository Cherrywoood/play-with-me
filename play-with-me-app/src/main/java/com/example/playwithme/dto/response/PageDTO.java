package com.example.playwithme.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDTO<T> {
    private List<T> content;
    private Integer currentPage;
    private Integer totalPages;
    private Integer size;
    private Long totalElements;
    private Boolean hasNext;
}
