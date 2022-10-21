package com.example.auctionseller.frontmodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShoppingMallFront {

    private int id;

    private String shoppingMallName;

    private String shppingMallExplanation;

    private String thumnail;

    @Column(nullable = false, unique = true)
    private String username;
}
