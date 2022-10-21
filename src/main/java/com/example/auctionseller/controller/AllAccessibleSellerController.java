package com.example.auctionseller.controller;


import com.example.auctionseller.model.ShoppingMallModel;
import com.example.auctionseller.service.ShoppingMallService;
import com.example.auctionseller.service.ShoppingMallServiceAll;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RequestMapping(value = "auth")
@RestController
public class AllAccessibleSellerController {

    private final ShoppingMallService shoppingMallService;

    private final ShoppingMallServiceAll shoppingMallServiceAll;

    // 모든 쇼핑몰 찾기
    @GetMapping(value = "find-all-shopping-mall")
    public ResponseEntity<List<ShoppingMallModel>> findAllShoppingMallList(){
        return new ResponseEntity<>(shoppingMallService.findAllShoppingMallList(), HttpStatus.OK);
    }

    // 모든 채팅 룸 찾기

    // 해당 쇼핑 몰 보기
    // 여러개를 요청해야함
    // 1. 해당 쇼핑몰 정보, 2. 해당쇼핑몰이 가진 제품들 전부다 나열
    // 해당 쇼핑몰을 알면
    @GetMapping(value = "show-shoppingmall")
    public ResponseEntity showShoppingMall(@RequestParam("id")int shoppingMallId){

        return new ResponseEntity(shoppingMallServiceAll.findShoppingMall(shoppingMallId), HttpStatus.OK);
    }

    @GetMapping(value = "show-shoppingmall/find-product-all/{id}")
    public ResponseEntity findProductAll(@PathVariable("id")int shoppingMallId){

        return new ResponseEntity(shoppingMallServiceAll.findAllProduct(shoppingMallId),HttpStatus.OK);
    }

    @GetMapping(value = "show-shoppingmall/product-show/{id}")
    public ResponseEntity showProduct(@PathVariable("id")int productId){

        return new ResponseEntity(shoppingMallServiceAll.findProductModel(productId),HttpStatus.OK);
    }

    /**
     * 판매자가 작성한 글의 리스트를 가져오는 엔드포인트
     * 해당 유저의 ID가 필요함
     * */
    @GetMapping(value = "find-all-seller-board/{shoppingMallId}")
    public ResponseEntity showSellerBoardList(@PathVariable("shoppingMallId")int shoppingMallId){

        return new ResponseEntity(shoppingMallServiceAll.findAllCommonModel(shoppingMallId),HttpStatus.OK);
    }

    /**
     * 판매자가 작성한 글을 가져오는 엔드포인트
     * */
    @GetMapping(value = "show-seller-board/{id}")
    public ResponseEntity showSellerBoard(@PathVariable("id")int boardId){

        return new ResponseEntity(shoppingMallServiceAll.findCommonModel(boardId),HttpStatus.OK);
    }


}
