package com.example.auctionseller.controller;

import com.example.auctionseller.model.SubscriberRequestMessage;
import com.example.auctionseller.model.frontdto.SubscriberMessageDTO;
import com.example.auctionseller.service.SellerService;
import com.example.auctionseller.service.SubScriberService;
import com.example.modulecommon.makefile.MakeFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Log4j2
@RequestMapping(value = "seller")
@RestController
public class SellerController {

    private final SellerService sellerService;

    private final SubScriberService subScriberService;

    private final MakeFile makeFile;

    @GetMapping("check-mall")
    public ResponseEntity shoppingMallCheck(HttpServletRequest request) {

        return new ResponseEntity(sellerService.checkShoppingMall(request), HttpStatus.OK);
    }

    @GetMapping("check-sub-message/{shuserId}")
    public ResponseEntity<List<SubscriberMessageDTO>> checkSubMessage(HttpServletRequest request,
                                                                      @PathVariable int shuserId) {

        // 중간에 검증과정이 있어야함
        return new ResponseEntity(subScriberService.findAllSubsciberRequestMessage(shuserId,request), HttpStatus.OK);
    }

}
