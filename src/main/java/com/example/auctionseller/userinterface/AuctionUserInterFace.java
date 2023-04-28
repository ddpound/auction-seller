package com.example.auctionseller.userinterface;

import com.example.modulecommon.frontModel.UserModelFront;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@FeignClient(name = "auction-user")
public interface AuctionUserInterFace {

    @GetMapping("/user/info/{userid}")
    ResponseEntity<UserModelFront> findUserModelFront(@RequestHeader("Authorization")String token,
                                                      @RequestHeader("RefreshToken") String reToken,
                                                      @PathVariable int userid);

    @GetMapping("/user/info/{userid}")
    ResponseEntity<UserModelFront> findUserModelFront(@PathVariable int userid);

    @GetMapping("/user/info/{userid}")
    ResponseEntity<UserModelFront> findUserModelFront(@RequestHeader("Cookie") String cookie,
                                                      @PathVariable int userid);

}
