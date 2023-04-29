package com.example.auctionseller.sellercommon;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.auctionseller.userinterface.AuctionUserInterFace;
import com.example.modulecommon.allstatic.MyTokenProperties;
import com.example.modulecommon.frontModel.UserModelFront;
import com.example.modulecommon.jwtutil.CookieJWTUtil;
import com.example.modulecommon.jwtutil.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


/**
 * 같은 코드를 반복해서 만드는 클래스
 * 토큰 값을 받아왔을때 username, 아니면 role을 긁어오거나하는 클래스
 * */
@Log4j2
@RequiredArgsConstructor
@Component
public class SellerReturnTokenUsername {


    private final AuctionUserInterFace auctionUserInterFace;

    private final CookieJWTUtil cookieJWTUtil;

    private final JWTUtil jwtUtil;

    private final MyTokenProperties myTokenProperties;
    /**
     * 1은 String username
     * 2는 int userId
     * 토큰값을 받음과 동시에 verify검증해주고
     * username에 붙은 [] 를 제거해주면서 반환함
     * userId 도 반납함
     * */
    public Map<Integer, Object> tokenGetUsername(HttpServletRequest request){

        String cookieToken = cookieJWTUtil.requestListCookieGetString(myTokenProperties.getJWT_COOKIE_NAME(), request);

        // 원래대로라면 헤더에서 토큰값을 추출했지만 쿠키값에 넣어서 주석처리함
//        String jwtHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//        String token = jwtHeader.replace("Bearer ", "");


        DecodedJWT decodedJWT = JWT.decode(cookieToken);


        // request헤더로 긁어온 username값에는 [] 이게 붙음
//        String username = decodedJWT
//                .getClaim("username")
//                .toString()
//                .replaceAll("[\\[|\\]]","")
//                .replaceAll("\"","");

        int userid = decodedJWT.getClaim("userId").asInt();

        Map<Integer, Object> returnMap = new HashMap();

        // 더이상 claim에 username은 없어서 여기서 repository로 받아와 주기만 하면될듯
        ResponseEntity<UserModelFront> userdata = auctionUserInterFace
                .findUserModelFront(myTokenProperties.getJWT_COOKIE_NAME()+"="+cookieToken, userid);

        if(userdata.getBody() != null ){
            returnMap.put(1, userdata.getBody().getUserName());
            returnMap.put(2, userid);
            return returnMap;
        }else {
            return null;
        }
    }

}
