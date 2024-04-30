package com.example.work4.domain.image.entity;


import com.example.work4.domain.board.entity.Board;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    private String url;


    @Builder
    public Image(String fileName,String url){
        this.fileName=fileName;
        this.url =url;
    }

}
