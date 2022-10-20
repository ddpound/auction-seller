package com.example.auctionseller.controller;

import com.example.auctionseller.service.SellerService;
import com.example.modulecommon.makefile.MakeFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequiredArgsConstructor
@Log4j2
@RequestMapping(value = "seller")
@RestController
public class SellerController {

    private final SellerService sellerService;

    private final MakeFile makeFile;

    @GetMapping("check-mall")
    public ResponseEntity shoppingMallCheck(HttpServletRequest request) {

        return new ResponseEntity(sellerService.checkShoppingMall(request), HttpStatus.OK);
    }



}
