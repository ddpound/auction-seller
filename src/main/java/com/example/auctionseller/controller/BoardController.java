package com.example.auctionseller.controller;


import com.example.auctionseller.sellercommon.SellerReturnTokenUsername;
import com.example.auctionseller.service.BoardService;
import com.example.auctionseller.service.ProductService;
import com.example.auctionseller.service.ShoppingMallService;
import com.example.modulecommon.makefile.MakeFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Log4j2
@RequiredArgsConstructor
@RequestMapping(value = "seller")
@RestController
public class BoardController {

    private final ShoppingMallService shoppingMallService;

    private final BoardService boardService;

    private final ProductService productService;

    private final SellerReturnTokenUsername sellerReturnTokenUsername;

    private final MakeFile makeFile;

    @PostMapping(value = "save-board/{modify}")
    public ResponseEntity saveBoard(@RequestParam("title") String title,
                                    @RequestParam("content") String content,
                                    @RequestParam(value="thumbnail", required=false) MultipartFile thumbnail,
                                    @RequestParam(value = "category", required = false) int categoryId,
                                    @RequestParam(value = "boardId" ,required = false) Integer boardId,
                                    @PathVariable(value = "modify" , required = false) boolean modify,
                                    HttpServletRequest request) {

        int resultNum = boardService.saveBoard(title,content,thumbnail,categoryId,request,modify,boardId);

        if(resultNum == 1 ){

            if(modify){
                log.info("success modify");
            }else {
                log.info("success save");
            }
            return new ResponseEntity<>("OK", HttpStatus.OK);
        }else{
            if(resultNum == -3 ){
                return new ResponseEntity<>("You used more than 10 pictures", HttpStatus.OK);
            }

            if(resultNum == -4 || resultNum == -5 ){
                return new ResponseEntity<>("Picture Error", HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>("Server Or Client Request Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @DeleteMapping(value = "delete-seller-board/{id}")
    public ResponseEntity deleteSellerBoard(@PathVariable("id")int boardId,
                                            HttpServletRequest request){

        int resultNum = boardService.deleteSellerBoard(boardId,request);

        if(resultNum ==1 ){
            return new ResponseEntity("success delete", HttpStatus.OK);

        }
        if(resultNum == -2 ){
            return new ResponseEntity("fail delete", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity("fail delete", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(value = "get-category-list")
    public ResponseEntity getCategory(HttpServletRequest request){

        return new ResponseEntity<>(boardService.getBoardCategoryList(request), HttpStatus.OK);
    }

    @PostMapping(value = "save-category")
    public ResponseEntity saveCategory(HttpServletRequest request,
                                       @RequestParam(value = "categoryName") String categoryName){

        int resultNum = boardService.saveBoardCategory(request,categoryName);

        if(resultNum ==1){
            return new ResponseEntity<>("success save category", HttpStatus.OK);
        }else if(resultNum == -2){
            return new ResponseEntity<>("blank words", HttpStatus.BAD_REQUEST);
        }
        else{
            return new ResponseEntity<>("fail save category", HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping(value = "modify-category")
    public ResponseEntity modifyCategory(HttpServletRequest request,
                                         String categoryName){

        int resultNum = boardService.modifyBoardCategory(request,categoryName);

        if(resultNum ==1){
            return new ResponseEntity<>("success modify category", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("fail modify category", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "save-reply")
    public ResponseEntity<String> saveReply(HttpServletRequest request,
                                            @RequestParam(value="content", required=false)String content,
                                            @RequestParam(value="userId", required=false)int userId,
                                            @RequestParam(value="nickName", required=false)String nickName,
                                            @RequestParam(value="userPicture", required=false)String userPicture,
                                            @RequestParam(value="boardId", required=false)int boardId){

        int resultNum = boardService.saveReply(content,userId,nickName,userPicture,boardId,request);

        if(resultNum ==1 ){
            return new ResponseEntity<>("success save reply", HttpStatus.OK);
        }else {
            return new ResponseEntity<>("fail save reply", HttpStatus.BAD_REQUEST);
        }



    }

    @DeleteMapping(value = "delete-reply/{id}")
    public ResponseEntity<String> deleteReply(@PathVariable(value = "id") Integer id,
                                              HttpServletRequest request){

        int resultNum = boardService.deleteReply(id, request);

        if(resultNum ==1 ){
            return new ResponseEntity<>("success save reply", HttpStatus.OK);
        }else {
            return new ResponseEntity<>("fail save reply", HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping(value = "save-reply/of-reply")
    public ResponseEntity<String> saveReplyOfReply(HttpServletRequest request,
                                                   @RequestParam(value="content", required=false)String content,
                                                   @RequestParam(value="userId", required=false)int userId,
                                                   @RequestParam(value="nickName", required=false)String nickName,
                                                   @RequestParam(value="userPicture", required=false)String userPicture,
                                                   @RequestParam(value="replyId", required=false)int replyId,
                                                   @RequestParam(value="boardId", required=false)int boardId){
        System.out.println("작동확인");
        int resultNum = boardService.saveReplyOfReply(content,userId,nickName,userPicture,boardId,replyId,request);

        if(resultNum ==1 ){
            return new ResponseEntity<>("success save reply", HttpStatus.OK);
        }else {
            return new ResponseEntity<>("fail save reply", HttpStatus.BAD_REQUEST);
        }



    }

}
