package com.example.auctionseller.userinterface;

import com.example.modulecommon.frontModel.UserModelFront;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;


@FeignClient(name = "auction-user")
public interface AuctionUserInterFace {

    @GetMapping("/user/info/{userid}")
    ResponseEntity<UserModelFront> findUserModelFront(@RequestHeader("Authorization")String token,
                                                      @RequestHeader("RefreshToken") String reToken,
                                                      @PathVariable int userid);

}
