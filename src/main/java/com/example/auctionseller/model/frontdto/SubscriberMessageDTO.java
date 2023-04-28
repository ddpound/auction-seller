package com.example.auctionseller.model.frontdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubscriberMessageDTO {

    private int userId;

    private String shoppingMallName;

    private int shppingMallUserId;

    private String username;

    private String message;



}
