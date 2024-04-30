package com.example.work4.domain.board.entity;


import com.example.work4.domain.board.dto.request.BoardUpdateRequest;
import com.example.work4.domain.image.entity.Image;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.apache.tomcat.jni.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Board {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(name="title")
    private String title;
    @Column(name="content")
    private String content;
    @Column(name="writer")
    private String writer;
    @Column(name="date")
    private String date;

    @OneToMany(mappedBy = "board",cascade = CascadeType.ALL)
    @JsonIgnore // JSON 직렬화 과정에서 무시
    private List<Image> images = new ArrayList<>();

    @Builder
    public Board(String writer,String title,String content,String date){
        this.writer=writer;
        this.title=title;
        this.content=content;
        this.date=date;
    }

    public void addImageList(Image image){
        images.add(image);
        image.setBoard(this);
    }

    public void update(BoardUpdateRequest request){

        this.writer=request.writer();
        this.content= request.content();
        this.title = request.title();

    }



}
