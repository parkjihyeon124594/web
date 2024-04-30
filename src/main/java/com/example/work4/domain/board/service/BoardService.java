package com.example.work4.domain.board.service;

import com.example.work4.domain.board.dto.request.BoardSaveRequest;
import com.example.work4.domain.board.dto.request.BoardUpdateRequest;
import com.example.work4.domain.board.dto.response.BoardReadResponse;
import com.example.work4.domain.board.dto.response.BoardReadResponseImage;
import com.example.work4.domain.board.entity.Board;
import com.example.work4.domain.board.exception.BoardErrorCode;
import com.example.work4.domain.board.repository.BoardRepository;
import com.example.work4.domain.image.entity.Image;
import com.example.work4.domain.image.repository.ImageRepository;
import com.example.work4.domain.image.service.ImageService;
import com.example.work4.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final ImageService imageService;
    LocalDateTime currentTime = LocalDateTime.now(); // 현재 시간을 가져옴

    /**
     * Create
     */
    @Transactional
    public Long saveBoard(BoardSaveRequest request,List<Image> images){
        Board board= Board.builder()
                .writer(request.writer())
                .content(request.content())
                .title(request.title())
                .date(String.valueOf(currentTime))
                .build();

        for(Image i:images){
            board.addImageList(i);
        }
        boardRepository.save(board);

        return board.getId();

    }

    /**
     * READ
     */
    public BoardReadResponse readBoard(Long boardId){
        Board board=boardRepository.findById(boardId)
                .orElseThrow(() -> new GlobalException(BoardErrorCode.BOARD_NOT_FOUND));

        return BoardReadResponse.builder()
                .writer(board.getWriter())
                .title(board.getTitle())
                .content(board.getContent())
                .date(String.valueOf(currentTime))
                .build();
    }

    public BoardReadResponseImage readBoardImage(Long boardId) throws IOException {
        Board board=boardRepository.findById(boardId)
                .orElseThrow(() -> new GlobalException(BoardErrorCode.BOARD_NOT_FOUND));

        List<Image> imageList = imageService.getImageList(Optional.ofNullable(board));
        List<byte[]> imagesByte = new ArrayList<>();

        for(int i=0;i<imageList.size();i++){
            byte[] imageData = imageService.downloadImage(imageList.get(i));
            imagesByte.add(imageData);
        }

        return BoardReadResponseImage.builder()
                .writer(board.getWriter())
                .content(board.getContent())
                .title(board.getTitle())
                .date(String.valueOf(currentTime))
                .imagesByte(imagesByte)
                .build();
    }

    public List<Board> findAll(){
        List<Board> all = boardRepository.findAll();
        return all;
    }


    public Optional<Board> getBoard(Long boardId){
        return boardRepository.findById(boardId);
    }

   /* public BoardReadResponseAll mapToBoardReadResponseAll(Board board) {
        return BoardReadResponseAll.builder()
                .writer(board.getWriter())
                .tite(board.getTitle())
                .date(board.getDate())
                .build();
    }
    public List<BoardReadResponseAll> findAllRead() {
        List<Board> boards = findAll();
        List<BoardReadResponseAll> boardReadResponseAllList = new ArrayList<>();

        for (Board board : boards) {
            BoardReadResponseAll boardReadResponseAll = mapToBoardReadResponseAll(board);
            boardReadResponseAllList.add(boardReadResponseAll);
        }
        return boardReadResponseAllList;
    }*/

    /**
     * UPDATE
     */
    @Transactional
    public Long updateBoard(BoardUpdateRequest request,Long boardId){
        Board board=boardRepository.findById(boardId)
                .orElseThrow(()->new GlobalException(BoardErrorCode.BOARD_NOT_FOUND));

        board.update(request);

        return board.getId();
    }

    /**
     * DELETE
     */
    @Transactional
    public void deleteBoard(Long boardId){
        Board board=boardRepository.findById(boardId)
                .orElseThrow(()->new GlobalException(BoardErrorCode.BOARD_NOT_FOUND));

        boardRepository.delete(board);
    }

}
