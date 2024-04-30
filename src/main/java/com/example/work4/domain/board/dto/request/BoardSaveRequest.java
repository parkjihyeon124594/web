package com.example.work4.domain.board.dto.request;

import lombok.Builder;

@Builder
public record BoardSaveRequest(String writer, String title, String content) {

}
