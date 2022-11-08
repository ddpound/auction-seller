package com.example.auctionseller.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.auctionseller.frontmodel.ShoppingMallFront;
import com.example.auctionseller.model.ShoppingMallModel;
import com.example.auctionseller.repository.ShoppingMallModelRepositry;
import com.example.auctionseller.userinterface.AuctionUserInterFace;
import com.example.modulecommon.frontModel.UserModelFront;
import com.example.modulecommon.jwtutil.JWTUtil;
import com.example.modulecommon.makefile.MakeFile;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Service
public class SellerService {

    private final ShoppingMallModelRepositry shoppingMallModelRepositry;

    private final MakeFile makeFile;

    private final JWTUtil jwtUtil;

    private final AuctionUserInterFace auctionUserInterFace;

    @Transactional(readOnly = true)
    public ShoppingMallFront checkShoppingMall(HttpServletRequest request){
        // 유저 서비스에 FeignClient 요청을 해야해서 두가지 다 받아와야함

        String jwtHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String jwtRHeader = request.getHeader("RefreshToken");

        String token = jwtHeader.replace("Bearer ", "");
        String reToken = jwtHeader.replace("Bearer ", "");


        ResponseEntity<UserModelFront> findFrontUserModel = auctionUserInterFace
                .findUserModelFront(jwtHeader,jwtRHeader, JWT.decode(token).getClaim("userId").asInt());


        if(findFrontUserModel.getBody() != null){
            ShoppingMallModel shoppingMallModel = shoppingMallModelRepositry.findByUsername(findFrontUserModel.getBody().getUserName());

            if(shoppingMallModel !=null){

                // 프론트 반환할때 비밀번호가 나가지않게... 근데 다른 좋은방법을 생각해보자
                return ShoppingMallFront.builder()
                        .id(shoppingMallModel.getId())
                        .shoppingMallName(shoppingMallModel.getShoppingMallName())
                        .shppingMallExplanation(shoppingMallModel.getShoppingMallExplanation())
                        .thumnail(shoppingMallModel.getThumbnailUrlPath())
                        .username(findFrontUserModel.getBody().getUserName())
                        .build();

            }else {
                return null;
            }
        }


        return null;
    }


}
