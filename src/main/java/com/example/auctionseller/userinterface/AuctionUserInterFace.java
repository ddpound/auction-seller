package com.example.auctionseller.userinterface;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "auction-user")
public interface AuctionUserInterFace {

    @GetMapping("/auction-user/user/info")
    List<String> getUser();

}
