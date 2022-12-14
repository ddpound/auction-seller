package com.example.auctionseller.model.frontdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReservationDetails {

    private int id;

    // 제품 아이디
    private int productId;

    private int quantity;

    private int shoppingMallId;

    // 구매자 아이디
    private int buyerId;

    private String buyerNickName;

    private String address;

    private List<OptionDto> optionList;


}
