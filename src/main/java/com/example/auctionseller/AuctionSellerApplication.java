package com.example.auctionseller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.example.auctionseller","com.example.modulecommon"})
public class AuctionSellerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuctionSellerApplication.class, args);
    }

}
