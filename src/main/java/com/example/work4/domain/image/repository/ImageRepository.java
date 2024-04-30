package com.example.work4.domain.image.repository;

import com.example.work4.domain.board.entity.Board;
import com.example.work4.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image,Long> {

    List<Image> findByBoard(Optional<Board> board);
}
