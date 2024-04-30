package com.example.work4.domain.board.controller;


import com.example.work4.domain.board.dto.request.BoardSaveRequest;
import com.example.work4.domain.board.dto.request.BoardUpdateRequest;
import com.example.work4.domain.board.dto.response.BoardReadResponseImage;
import com.example.work4.domain.board.entity.Board;
import com.example.work4.domain.board.service.BoardService;
import com.example.work4.domain.image.entity.Image;
import com.example.work4.domain.image.service.ImageService;
import com.example.work4.global.util.ApiUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;
    private final ImageService imageService;



    /*
        파일(이미지)와 데이터를 보낼 때, @RequestBody로 받으면 안되는 이유는

        @RequestBody는 Json으로 들어오는 바디 데이터를 파싱해주지만
        Http Header에 명시해준 데이터 타입은 multipart-form date이다.
        (multipart form data : 서버에 이미지를 전송할 때 쓰는 content-type)

        서버에서 multipart-form data Content-type을 받을 때는
        @RequestBody가 아닌 @RequestPart 애노테이션을 사용해 주어야 한다

     */
    /*
    @RequestParam
        name-value 쌍의 form 필드와 함께 사용된다.
    @RequestPart
        JSON, XML등을 포함하는 복잡한 내용의 Part와 함께 사용된다.
     */

    /**
     * 게시글 생성
     * @param boardSaveRequest
     * @param images
     * @return
     * @throws IOException
     */

    @PostMapping()
    public ResponseEntity<ApiUtil.ApiSuccessResult<Long>> createBoard(
            @RequestPart(value="boardSaveRequest") BoardSaveRequest boardSaveRequest,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) throws IOException {

        List<Image> listImage = imageService.saveImage(images);
        Long saveId = boardService.saveBoard(boardSaveRequest,listImage);

        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.CREATED,saveId));
    }


    /**
     * 게시글 수정
     * @param boardUpdateRequest
     * @param id
     * @return 수정된 게시글 id
     */

    @PutMapping("/{boardId}")
    public ResponseEntity<ApiUtil.ApiSuccessResult<Long>> updateBoard(
            @RequestBody BoardUpdateRequest boardUpdateRequest,
            @PathVariable("boardId") Long id
            )
    {
        Long updateId = boardService.updateBoard(boardUpdateRequest,id);

        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.OK,updateId));

    }

    /**
     *  게시글 조회
     * @param boardId
     * @return 조회된 게시글 내용
     */

/*    @GetMapping("/{boardId}")
    public ResponseEntity<ApiUtil.ApiSuccessResult<BoardReadResponse>> getBoard(
            @PathVariable("boardId") Long boardId
    ){
        BoardReadResponse boardReadResponse = boardService.readBoard(boardId);

        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.OK,boardReadResponse));
    }*/

    @GetMapping("/{boardId}")
    public ResponseEntity<ApiUtil.ApiSuccessResult<BoardReadResponseImage>> getBoard(
            @PathVariable("boardId") Long boardId
    ) throws IOException {
        BoardReadResponseImage boardReadResponseImage = boardService.readBoardImage(boardId);
        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.OK, boardReadResponseImage));
    }

    //이미지 테스트용

    @GetMapping("/{boardId}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable("boardId") Long boardId) throws IOException {
        Optional<Board> board = boardService.getBoard(boardId);
        List<Image> imageList = imageService.getImageList(board);
        byte[] imageData = imageService.downloadImage(imageList.get(0));

        // Set appropriate content type based on image format (replace with logic to determine type)
        String contentType = "image/png";  // Replace with actual content type retrieval logic

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(contentType))
                .body(imageData);
    }

  /*  @GetMapping("/{boardId}")
    public ResponseEntity<ApiUtil.ApiSuccessResult<BoardReadResponse>> getBoard(
            @PathVariable("boardId") Long boardId
            )
    {
        BoardReadResponse boardReadResponse = boardService.readBoard(boardId);

        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.OK,boardReadResponse));
    }*/

    // ResponseDtd => 게시판 타입으로 리스트로 => findAll
    /*@GetMapping("/all")
    public ResponseEntity<ApiUtil.ApiSuccessResult<List<BoardReadResponseAll>>> allBoard(){

        List<BoardReadResponseAll> allBoard = boardService.findAllRead();
        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.OK,allBoard));
    }*/


   @GetMapping("/all")
   public List<Board> allBoard() {

       List<Board> allBoard = boardService.findAll();
       return allBoard;
   }


       /**
        * 게시글 삭제
        * @param boardId
        * @return
        */

    @DeleteMapping("/{boardId}")
    public ResponseEntity<ApiUtil.ApiSuccessResult<?>> delte(
            @PathVariable("boardId") Long boardId
    ){
        boardService.deleteBoard(boardId);
        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.OK));
    }


}
