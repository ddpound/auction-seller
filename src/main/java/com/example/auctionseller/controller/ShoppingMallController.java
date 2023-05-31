package com.example.auctionseller.controller;

import com.example.auctionseller.sellercommon.SellerReturnTokenUsername;
import com.example.auctionseller.service.ProductService;
import com.example.auctionseller.service.ShoppingMallService;
import com.example.modulecommon.makefile.MakeFile;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
@RequestMapping(value = "seller")
@RestController
public class ShoppingMallController {

    private final ShoppingMallService shoppingMallService;


    private final ProductService productService;

    private final SellerReturnTokenUsername sellerReturnTokenUsername;

    private final MakeFile makeFile;

    /**
     * 새로운 쇼핑몰을 만드는 엔드포인트
     * @param shoppingmallName 쇼핑몰 이름
     * @param multipartFile MultipartFile 자료형의 파라미터
     * @param shoppingMallExplanation 쇼핑몰 설명
     * @param request 꼭 넣어줘야함, 토큰값 긁어와야함
     * */
    @PostMapping("make-shopping-mall")
    public ResponseEntity makeMyShoppingMall(@RequestParam("shoppingMallName") String shoppingmallName,
                                             @RequestParam("thumbnail") MultipartFile multipartFile,
                                             @RequestParam("explantion") String shoppingMallExplanation,
                                             HttpServletRequest request) throws IOException {
        log.info("try save shopping Mall : " + shoppingmallName);

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

    @PostMapping("modify-shopping-mall")
    public ResponseEntity modifyMyShoppingMall(@RequestParam("shoppingMallName") String shoppingmallName,
                                               @RequestParam(value = "thumbnail", required = false) MultipartFile multipartFile,
                                               @RequestParam(value = "thumbnail2", required = false) String urlFilePath,
                                               @RequestParam("explantion") String shoppingMallExplanation,
                                               HttpServletRequest request) throws IOException {

        int resultNum = shoppingMallService.modifyShoppingMall(
                multipartFile,
                shoppingmallName,
                shoppingMallExplanation,
                urlFilePath,
                request);

        if (resultNum == 1) {
            return new ResponseEntity("", HttpStatus.OK);
        } else if (resultNum == -2) {
            // 중복된 쇼핑몰 이름
            return new ResponseEntity("Already-ShoppingMall-Name", HttpStatus.OK);
        } else if (resultNum == -3) {
            // 중복된 쇼핑몰 이름
            return new ResponseEntity("user-have-not-ShoppingMall", HttpStatus.OK);
        } else {
            return new ResponseEntity("", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "temporary-image-save", produces = "application/json")
    public JsonObject boardImageTemporarySave(
            @RequestParam("file") MultipartFile multipartFile,
            HttpServletRequest request) {

        Map<Integer, Object> returnmap = sellerReturnTokenUsername.tokenGetUsername(request);


        return makeFile.makeTemporaryfiles(multipartFile, (Integer)returnmap.get(2), request);
    }





}
