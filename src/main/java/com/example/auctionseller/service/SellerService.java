package com.example.auctionseller.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.auctionseller.config.MyTokenProperties;
import com.example.auctionseller.frontmodel.ShoppingMallFront;
import com.example.auctionseller.model.ShoppingMallModel;
import com.example.auctionseller.repository.ShoppingMallModelRepositry;
import com.example.auctionseller.userinterface.AuctionUserInterFace;
import com.example.modulecommon.frontModel.UserModelFront;
import com.example.modulecommon.jwtutil.CookieJWTUtil;
import com.example.modulecommon.jwtutil.JWTUtil;
import com.example.modulecommon.makefile.MakeFile;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Service
public class SellerService {

    private final ShoppingMallModelRepositry shoppingMallModelRepositry;

    private final AuctionUserInterFace auctionUserInterFace;

    private final CookieJWTUtil cookieJWTUtil;

    private final MyTokenProperties myTokenProperties;


    @Transactional(readOnly = true)
    public ShoppingMallFront checkShoppingMall(HttpServletRequest request){
        // 유저 서비스에 FeignClient 요청을 해야해서 두가지 다 받아와야함

        Cookie[] cookies =  request.getCookies();

        for (Cookie c: cookies
             ) {
            System.out.println(c.getName() +" : "+c.getValue());
        }
        System.out.println(myTokenProperties.getJWT_COOKIE_NAME() +" 토큰이름이 뭐라고?");
        System.out.println(myTokenProperties.getREFRESH_COOKIE_NAME() +" 토큰이름이 뭐라고?");

        String cookieToken = cookieJWTUtil.requestListCookieGetString(myTokenProperties.getJWT_COOKIE_NAME(),request);

        ResponseEntity<UserModelFront> findFrontUserModel = auctionUserInterFace
                .findUserModelFront(myTokenProperties.getJWT_COOKIE_NAME()+"="+cookieToken,JWT.decode(cookieToken).getClaim("userId").asInt());


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
