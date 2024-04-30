package com.example.work4.domain.board.dto.response;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
public record BoardReadResponse(String writer, String title, String content, String date) {
}
