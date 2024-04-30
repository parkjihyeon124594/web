package com.example.work4.domain.image.service;


import com.example.work4.domain.board.entity.Board;
import com.example.work4.domain.image.entity.Image;
import com.example.work4.domain.image.repository.ImageRepository;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
@Getter
@Slf4j
public class ImageService {

    private final ImageRepository imageRepository;

    public List<Image> saveImage(List<MultipartFile> multipartFiles) throws IOException {
        List<Image> images = new ArrayList<>();

        for(MultipartFile m : multipartFiles){
            m.transferTo(new File("/Users/parkjihyeon/Desktop/imageFolder/"+m.getOriginalFilename()));

            Image image= Image.builder()
                    .fileName(m.getOriginalFilename())
                    .url("/Users/parkjihyeon/Desktop/imageFolder/"+m.getOriginalFilename())
                    .build();

            images.add(image);
        }

        return images;
    }

    public List<Image> getImageList(Optional<Board> board){
        List<Image> image = imageRepository.findByBoard(board);

        return image;
    }

    public byte[] downloadImage(Image image) throws IOException{
        return Files.readAllBytes(new File(image.getUrl()).toPath());
    }

}
