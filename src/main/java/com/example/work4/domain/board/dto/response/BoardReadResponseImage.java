package com.example.work4.domain.board.dto.response;

import com.example.work4.domain.image.entity.Image;
import lombok.Builder;

import java.util.List;

@Builder
public record BoardReadResponseImage(String writer, String title, String content, String date, List<byte[]> imagesByte) {
}
