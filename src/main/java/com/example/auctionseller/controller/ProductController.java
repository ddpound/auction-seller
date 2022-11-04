package com.example.auctionseller.controller;

import com.example.auctionseller.sellercommon.SellerReturnTokenUsername;
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
import java.util.ArrayList;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("seller")
@RestController
public class ProductController {

    private final ShoppingMallService shoppingMallService;


    private final ProductService productService;

    private final SellerReturnTokenUsername sellerReturnTokenUsername;

    private final MakeFile makeFile;


    @PostMapping(value = "save-product/{modify}")
    public ResponseEntity saveProduct(@RequestParam("productname") String productname,
                                      @RequestParam("productprice") int productprice,
                                      @RequestParam("productquantity") int productquantity,
                                      @RequestParam("content") String content,
                                      @RequestParam(value = "ProductID", required = false) Integer ProductID,
                                      @RequestParam(value="thumbnail1", required=false) MultipartFile file1,
                                      @RequestParam(value="thumbnail2", required=false) MultipartFile file2,
                                      @RequestParam(value="thumbnail3", required=false) MultipartFile file3,
                                      @PathVariable(value = "modify" , required = false) boolean modify,
                                      HttpServletRequest request) {



        List<MultipartFile> fileList = new ArrayList<MultipartFile>();

        // 좀더 간결한 코드 필요
        if( file1 != null )
            fileList.add(file1);
        if( file2 != null )
            fileList.add(file2);
        if( file3 != null )
            fileList.add(file3);

        int resultNum = productService.saveProduct(ProductID,
                productname,productprice,productquantity,content,fileList,request,modify);

        if(resultNum == 1 ){
            return new ResponseEntity<>("OK", HttpStatus.OK);
        }

        if(resultNum == -3 ){
            return new ResponseEntity<>("You used more than 10 pictures", HttpStatus.OK);
        }

        if(resultNum == -4 || resultNum == -5 ){
            return new ResponseEntity<>("Picture Error", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Server Or Client Request Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(value = "show-shoppingmall/product-show/{id}")
    public ResponseEntity showProduct(@PathVariable("id")int productlId,
                                      HttpServletRequest request){

        // 서비스 만들기
        return new ResponseEntity(productService.findProduct(productlId), HttpStatus.OK);
    }

    @DeleteMapping(value = "delete-product/{id}")
    public ResponseEntity deleteProduct(@PathVariable("id")int productId,
                                        HttpServletRequest request){

        int resultNum = productService.deleteProduct(productId,request);

        if(resultNum ==1 ){
            return new ResponseEntity("success delete", HttpStatus.OK);

        }
        if(resultNum == -2 ){
            return new ResponseEntity("fail delete", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity("fail delete", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
