package com.example.auctionseller.sellercommon;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.modulecommon.jwtutil.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
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


    private final JWTUtil jwtUtil;

    /**
     * 1은 String username
     * 2는 int userId
     * 토큰값을 받음과 동시에 verify검증해주고
     * username에 붙은 [] 를 제거해주면서 반환함
     * userId 도 반납함
     * */
    public Map<Integer, Object> tokenGetUsername(HttpServletRequest request){

        String jwtHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        String token = jwtHeader.replace("Bearer ", "");


        DecodedJWT decodedJWT = JWT.decode(token);

        // request헤더로 긁어온 username값에는 [] 이게 붙음
        String username = decodedJWT
                .getClaim("username")
                .toString()
                .replaceAll("[\\[|\\]]","")
                .replaceAll("\"","");
        int userid = decodedJWT.getClaim("userId").asInt();

        Map<Integer, Object> returnMap = new HashMap();

        returnMap.put(1 , username);
        returnMap.put(2, userid);

        return returnMap;
    }



}
