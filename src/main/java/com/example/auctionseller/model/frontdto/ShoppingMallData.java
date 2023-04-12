package com.example.auctionseller.model.frontdto;

import lombok.*;


/**
 * Chat 어플리케이션이 구매내역을 요구했을 때 전달해야하는 DTO
 *
 *
 * */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShoppingMallData {

    // 판매자 Id
    private int userId;

    private String shoppingMallName;

    private String shoppingMallExplanation;

}
