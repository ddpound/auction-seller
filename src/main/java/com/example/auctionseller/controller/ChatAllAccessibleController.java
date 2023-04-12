package com.example.auctionseller.controller;

import com.example.auctionseller.model.ShoppingMallModel;
import com.example.auctionseller.model.frontdto.ShoppingMallData;
import com.example.auctionseller.service.ShoppingMallService;
import com.example.auctionseller.service.ShoppingMallServiceAll;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * 채팅 어플에서 해당 어플리케이션에 접근할때 사용하는 컨트롤러
 * */
@RequiredArgsConstructor
@RequestMapping(value = "auth")
@RestController
public class ChatAllAccessibleController {

    private final ShoppingMallService shoppingMallService;

    private final ShoppingMallServiceAll shoppingMallServiceAll;


    /**
     * 구매자가 구매내역을 검색하면서 동시에 원하는 쇼핑몰 값을 가져옴
     * */
    @GetMapping(value = "find-shopping-mall/{sellerId}")
    public ResponseEntity findShppingMallData(@PathVariable int sellerId){

        Optional<ShoppingMallModel> findShoppingMallModel = shoppingMallServiceAll.findShoppingMall(sellerId);

        if(findShoppingMallModel.isPresent()){
            return new ResponseEntity<>(ShoppingMallData.builder()
                    .shoppingMallName(findShoppingMallModel.get().getShoppingMallName())
                    .userId(findShoppingMallModel.get().getUserId())
                    .build(), HttpStatus.OK);
        }

        return new ResponseEntity<>("Sorry Data is not found", HttpStatus.NOT_FOUND);
    }

}
