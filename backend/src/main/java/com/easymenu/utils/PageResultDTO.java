package com.easymenu.utils;

import java.util.List;

public record PageResultDTO<T>(List<T> content,
                               int page,
                               int size,
                               long totalElements,
                               int totalPages,
                               boolean isFirst,
                               boolean isLast) {

    public static <T> PageResultDTO<T> result(org.springframework.data.domain.Page<T> page) {
        return new PageResultDTO<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }
}
