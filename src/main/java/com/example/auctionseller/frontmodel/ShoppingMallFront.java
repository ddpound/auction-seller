package com.example.auctionseller.frontmodel;

import com.example.modulecommon.model.UserModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShoppingMallFront {
    private int id;

    private String shoppingMallName;

    private String shppingMallExplanation;

    private String thumnail;

    private UserModel userModel;
}
