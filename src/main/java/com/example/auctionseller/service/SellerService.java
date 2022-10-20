package com.example.auctionseller.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.auctionseller.frontmodel.ShoppingMallFront;
import com.example.auctionseller.model.ShoppingMallModel;
import com.example.auctionseller.repository.ShoppingMallModelRepositry;
import com.example.modulecommon.jwtutil.JWTUtil;
import com.example.modulecommon.makefile.MakeFile;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Service
public class SellerService {

    private final ShoppingMallModelRepositry shoppingMallModelRepositry;

    private final MakeFile makeFile;

    private final JWTUtil jwtUtil;

    @Transactional(readOnly = true)
    public ShoppingMallFront checkShoppingMall(HttpServletRequest request){
        String jwtHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        String token = jwtHeader.replace("Bearer ", "");

        DecodedJWT decodedJWT = jwtUtil.getTokenRole(token);

        System.out.println(decodedJWT.getClaim("username").asString());
        UserModel finduserModel = userModelRepository.findByUsername(decodedJWT.getClaim("username").asString());

        ShoppingMallModel shoppingMallModel = shoppingMallModelRepositry.findByUserModel(finduserModel);

        if(shoppingMallModel !=null){
            UserModel userModel = shoppingMallModel.getUserModel();
            userModel.setPassword("");

            // 프론트 반환할때 비밀번호가 나가지않게... 근데 다른 좋은방법을 생각해보자
            ShoppingMallFront shoppingMallFront = ShoppingMallFront.builder()
                    .id(shoppingMallModel.getId())
                    .shoppingMallName(shoppingMallModel.getShoppingMallName())
                    .shppingMallExplanation(shoppingMallModel.getShoppingMallExplanation())
                    .thumnail(shoppingMallModel.getThumbnailUrlPath())
                    .userModel(userModel)
                    .build();

            return shoppingMallFront;

        }else {
            return null;
        }

    }


}
