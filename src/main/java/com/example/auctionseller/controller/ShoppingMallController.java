package com.example.auctionseller.controller;

import com.example.auctionseller.service.ShoppingMallService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
@RequestMapping(value = "seller")
@RestController
public class ShoppingMallController {

    private final ShoppingMallService shoppingMallService;

    @PostMapping("make-shopping-mall")
    public ResponseEntity makeMyShoppingMall(@RequestParam("shoppingMallName") String shoppingmallName,
                                             @RequestParam("thumbnail") MultipartFile multipartFile,
                                             @RequestParam("explantion") String shoppingMallExplanation,
                                             HttpServletRequest request) throws IOException {

        int resultNum = shoppingMallService.SaveNewShoppingMall(multipartFile,
                shoppingmallName,
                shoppingMallExplanation, request);

        if (resultNum == 1) {
            return new ResponseEntity("succeess-save-shppingmall", HttpStatus.OK);
        } else if (resultNum == -2) {
            // 중복된 쇼핑몰 이름
            return new ResponseEntity("Already-ShoppingMall-Name", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity("", HttpStatus.BAD_REQUEST);
        }
    }


}
