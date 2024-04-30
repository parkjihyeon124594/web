package com.example.work4.domain.board.dto.response;

import lombok.Builder;

@Builder
public record BoardReadResponseAll(String writer,String tite,String date) {
}
