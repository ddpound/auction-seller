package com.example.auctionseller.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.auctionseller.model.ShoppingMallModel;
import com.example.auctionseller.repository.ShoppingMallModelRepositry;
import com.example.modulecommon.jwtutil.JWTUtil;
import com.example.modulecommon.makefile.MakeFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
@Service
public class ShoppingMallService {

    private final ShoppingMallModelRepositry shoppingMallModelRepositry;

    private final MakeFile makeFile;

    private final JWTUtil jwtUtil;

    public int SaveNewShoppingMall(MultipartFile multipartFile,
                                   String shoppingmallName,
                                   String shoppingMallExplanation,
                                   HttpServletRequest request) {

        String jwtHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = jwtHeader.replace("Bearer ", "");
        DecodedJWT decodedJWT = jwtUtil.getTokenRole(token);

        String username = decodedJWT.getClaim("username").toString();

        // 12길이보다 길면
        if(shoppingmallName.length() > 12){
            return -5;
        }


        ShoppingMallModel shoppingMallModel = shoppingMallModelRepositry.findByShoppingMallName(shoppingmallName);

        if(shoppingMallModel != null ){
            // 중복된 쇼핑몰이름
            return -2;
        }

        // 파일저장
        // 1. url 사진 경로
        // 2. 컴퓨터 사진 파일 경로
        Map<Integer,String> fileNames = makeFile.makeFileImage(decodedJWT.getClaim("userId").asInt(), multipartFile,request);


        ShoppingMallModel shoppingMallModelSave =
                ShoppingMallModel.builder()
                        .shoppingMallName(shoppingmallName)
                        .shoppingMallExplanation(shoppingMallExplanation)
                        .thumbnailUrlPath(fileNames.get(1))
                        .thumbnailFilePath(fileNames.get(2))
                        .filefolderPath(fileNames.get(3))
                        .username(username)
                        .build();

        shoppingMallModelRepositry.save(shoppingMallModelSave);

        return 1;
    }

}
